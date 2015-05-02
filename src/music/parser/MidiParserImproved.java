package music.parser;

import game.settings.GameplayType;
import game.settings.GameplayTypeSetting;

import java.io.*;
import java.util.*;

import util.Pair;

import javax.sound.midi.*;

import music.GeneralInstrument;
import music.Handedness;
import music.Instrument;
import music.InstrumentPiece;
import music.InstrumentType;
import music.MidiVoice;
import music.Music;
import music.Note;
import music.Voice;

public class MidiParserImproved {
	private static final int DEFAULT_MICROSPQN = 500000;
	private static final double DEFAULT_BPQN = 1.0;
	private static final int DEFAULT_PROGRAM_NUMBER = 0;
	private static final int MIDDLE_C = 60;
	private static final int SET_TEMPO = 0x51;
	private static final int PERCUSSION_CHANNEL = 9;
    
    // Assumption about music: a channel will have at most one program change for any midi file.
    public static Music parse( String musicTitle, String filename ) throws MidiUnavailableException, InvalidMidiDataException, IOException {
    	// Get the midi sequence information
    	InputStream in = ClassLoader.getSystemResourceAsStream( filename );
    	Sequence sequence = MidiSystem.getSequence( in );
    	int resolution = sequence.getResolution();
    	
    	MidiEvents events = getMidiEvents(sequence);
    	List<InstrumentPiece> instrumentPieces = getInstrumentPieces(events, resolution);
    	
    	Music music = new Music(musicTitle, instrumentPieces);
    	
    	return music;
    }
    
    // Gets the sequence of events related to tempo and sound.
    private static MidiEvents getMidiEvents(Sequence sequence) {
    	MidiEvents midiEvents = new MidiEvents();
    	for ( Track track : sequence.getTracks() ) {
    		for ( int i=0; i < track.size(); i++ ) {
    			MidiEvent event = track.get(i);
    			MidiMessage message = event.getMessage();
    			if ( message instanceof ShortMessage ) {
    				ShortMessage sm = (ShortMessage) message;
    				int command = sm.getCommand();
    				if ( command == ShortMessage.NOTE_ON ||
    					 command == ShortMessage.NOTE_OFF ||
    					 command == ShortMessage.PROGRAM_CHANGE ) {
    					midiEvents.add(event);
    				}
    			} else if ( message instanceof MetaMessage) {
    				MetaMessage mm = (MetaMessage) message;
    				int type = mm.getType();
    				if ( type == SET_TEMPO ) {
    					midiEvents.add(event);
    				}
    			}
    		}
    	}
    	midiEvents.sort();
    	return midiEvents;
    }
    
    private static Map<Integer,MidiSoundEvents> getChannelToSoundEvents(MidiEvents midiEvents) {
    	Map<Integer,MidiSoundEvents> channelToEvents = new HashMap<Integer,MidiSoundEvents>();
    	for ( MidiEvent event : midiEvents ) {
    		MidiMessage message = event.getMessage();
    		if ( message instanceof ShortMessage ) {
    			ShortMessage sm = (ShortMessage) message;
    			int channel = sm.getChannel();
    			int command = sm.getCommand();
    			if ( command == ShortMessage.NOTE_OFF ) {
    				MidiSoundEvents soundEvents = new MidiSoundEvents();
    				if ( channelToEvents.containsKey(channel) ) {
    					soundEvents = channelToEvents.get(channel);
    					soundEvents.add(event);
    				} else {
    					soundEvents.add(event);
    					channelToEvents.put(channel,soundEvents);
    				}
    			} else if ( command == ShortMessage.NOTE_ON ) {
    				MidiSoundEvents soundEvents = new MidiSoundEvents();
    				if ( channelToEvents.containsKey(channel) ) {
    					soundEvents = channelToEvents.get(channel);
    					soundEvents.add(event);
    				} else {
    					soundEvents.add(event);
    					channelToEvents.put(channel,soundEvents);
    				}
    			} else if ( command == ShortMessage.PROGRAM_CHANGE ) {
    				MidiSoundEvents soundEvents = new MidiSoundEvents();
    				if ( channelToEvents.containsKey(channel) ) {
    					soundEvents = channelToEvents.get(channel);
    					soundEvents.add(event);
    				} else {
    					soundEvents.add(event);
    					channelToEvents.put(channel,soundEvents);
    				}
    			}
    		}
    	}
    	// Make sure to sort all the MidiSoundEvents
    	for ( MidiSoundEvents midiSoundEvents : channelToEvents.values() ) {
    		midiSoundEvents.sort();
    	}
    	return channelToEvents;
    }
    
	private static Map<Integer,MidiSoundEvents> getProgramNumberToSoundEvents(MidiEvents midiEvents) {
		Map<Integer,MidiSoundEvents> programNumberToEvents = new HashMap<Integer,MidiSoundEvents>();
		
		Map<Integer,MidiSoundEvents> channelToEvents = getChannelToSoundEvents(midiEvents);
		for ( Integer channel : channelToEvents.keySet() ) {
			MidiSoundEvents events = channelToEvents.get(channel);
			// Check if the first message is a program change message
			MidiEvent firstEvent = events.get(0);
			ShortMessage firstSM = (ShortMessage) firstEvent.getMessage();
			int programNumber = DEFAULT_PROGRAM_NUMBER;
			if ( channel == PERCUSSION_CHANNEL ) {
				programNumber = Integer.MAX_VALUE;
			} else {
				if ( firstSM.getCommand() == ShortMessage.PROGRAM_CHANGE ) {
					programNumber = firstSM.getData1();
				}
			}
			MidiSoundEvents programSoundEvents = new MidiSoundEvents();
			if ( programNumberToEvents.containsKey(programNumber) ) {
				programSoundEvents = programNumberToEvents.get(programNumber);
			} else {
				programNumberToEvents.put(programNumber,programSoundEvents);
			}
			for ( MidiEvent event : events ) {
				programSoundEvents.add(event);
			}
		}
		if ( programNumberToEvents.containsKey(Integer.MAX_VALUE) ) {
			MidiSoundEvents events = programNumberToEvents.get(Integer.MAX_VALUE);
			programNumberToEvents.remove(Integer.MAX_VALUE);
			// Put the percussion events into a legitimate program number.
			for ( int i=0; i < 128; i++ ) {
				if ( !programNumberToEvents.containsKey(i) ) {
					programNumberToEvents.put(i, events);
					break;
				}
			}
		}
		// Make sure to sort the MidiSoundEvents again
		for ( MidiSoundEvents midiSoundEvents : programNumberToEvents.values() ) {
			midiSoundEvents.sort();
		}
		return programNumberToEvents;
	}
	
	// midiEvents is sorted sequence of MidiEvents
	private static Map<Long,Integer> getTickToTime(MidiEvents midiEvents, int resolution) {
		Map<Long,Integer> tickToTime = new HashMap<Long,Integer>();
		
		int ticksPerBeat = resolution;
		long currentTick = 0;
		int currentTime = 0;
		double BPQN = DEFAULT_BPQN;
		double currentMillisecondsPerTick = DEFAULT_MICROSPQN / (ticksPerBeat * BPQN * 1000);
		
		 
		for ( MidiEvent event : midiEvents ) {
			// First get the time of the event
			long tick = event.getTick();
			int time = currentTime + (int) ( (tick - currentTick) * currentMillisecondsPerTick );
			tickToTime.put(tick, time);
			// Update the time and current time
			currentTime = time;
			currentTick = tick;
			// Update the current milliseconds per tick if necessary
			MidiMessage message = event.getMessage();
			if ( message instanceof MetaMessage ) {
				MetaMessage mm = (MetaMessage) message;
				int type = mm.getType();
				if ( type == SET_TEMPO ) {
					byte[] bytes = mm.getMessage();
					int MPQNByte1 = bytes[3] & 0xFF;
					int MPQNByte2 = bytes[4] & 0xFF;
					int MPQNByte3 = bytes[5] & 0xFF;
					
					int MICROSPQN = MPQNByte1 * 0xFF * 0xFF + MPQNByte2 * 0xFF + MPQNByte3;
					currentMillisecondsPerTick = MICROSPQN / (ticksPerBeat * BPQN * 1000);
				}
			}
		}
		return tickToTime;
	}
	
	private static Handedness handedness( int programNumber, int value ) {
		if ( GameplayTypeSetting.gameplayType() == GameplayType.ONE_HANDED ) {
            return Handedness.SINGLE;
        } else {
            if ( Instrument.typeOfInstrument( programNumber ) == InstrumentType.SINGLE ) {
                return Handedness.SINGLE;
            } else {
                if ( value > MIDDLE_C ) {
                    return Handedness.LEFT;
                } else {
                    return Handedness.RIGHT;
                }
            }
        }
	}
	
	private static MidiEvent findNoteOnEvent(List<MidiEvent> noteOnEvents, int value) {
		for ( MidiEvent noteOnEvent : noteOnEvents ) {
			MidiMessage noteOnMessage = noteOnEvent.getMessage();
			ShortMessage noteOnSM = (ShortMessage) noteOnMessage;
			int noteOnValue = noteOnSM.getData1();
			if ( noteOnValue == value ) {
				return noteOnEvent;
			}
		}
		throw new RuntimeException("No note on to match to");
	}
	
	private static List<InstrumentPiece> getInstrumentPieces(MidiEvents midiEvents, int resolution) {
		List<InstrumentPiece> instrumentPieces = new ArrayList<InstrumentPiece>();
		
		Map<Integer,MidiSoundEvents> programNumberToSoundEvents = getProgramNumberToSoundEvents(midiEvents);
		Map<Long,Integer> tickToTime = getTickToTime(midiEvents, resolution);
		
		for ( int programNumber : programNumberToSoundEvents.keySet() ) {
			// Get the notes for each hand in order to make the voices for each program number
			Map<Handedness,List<Note>> handednessToNotes = new HashMap<Handedness,List<Note>>();
			Map<Handedness,Integer> handednessToStartTime = new HashMap<Handedness,Integer>();
			Map<Handedness,List<Integer>> handednessToTimesUntilNextElement = new HashMap<Handedness,List<Integer>>();
			Map<Handedness,Integer> handednessToTimeOfPreviousElement = new HashMap<Handedness,Integer>();
			Set<Integer> channels = new HashSet<Integer>();
			
			MidiSoundEvents events = programNumberToSoundEvents.get(programNumber);
			List<MidiEvent> noteOnEvents = new LinkedList<MidiEvent>();
			for ( MidiEvent event : events ) {
				MidiMessage message = event.getMessage();
				if ( message instanceof ShortMessage ) {
					ShortMessage sm = (ShortMessage) message;
					int channel = sm.getChannel();
					channels.add(channel);
					int command = sm.getCommand();
					if ( command == ShortMessage.NOTE_ON || command == ShortMessage.NOTE_OFF ) {
						boolean isNoteOn = true;
						if ( command == ShortMessage.NOTE_ON ) {
							int velocity = sm.getData2();
							if ( velocity != 0 ) {
								noteOnEvents.add(event);
							} else {
								isNoteOn = false;
							}
						} else {
							isNoteOn = false;
						}
						if ( !isNoteOn ) {
							int value = sm.getData1();
							MidiEvent noteOnEvent = findNoteOnEvent(noteOnEvents, value);
							noteOnEvents.remove(noteOnEvent);
							int noteOnTime = tickToTime.get(noteOnEvent.getTick());
							MidiMessage noteOnMessage = noteOnEvent.getMessage();
							ShortMessage noteOnSM = (ShortMessage) noteOnMessage;
							int noteOnVelocity = noteOnSM.getData1();
							int noteOffTime = tickToTime.get(event.getTick());
							int timeElapsed = noteOffTime - noteOnTime;
							Handedness handedness = handedness(programNumber,value);
							Note note = new Note(value,timeElapsed,noteOnVelocity);
							if ( handednessToNotes.containsKey(handedness) ) {
								List<Note> notes = handednessToNotes.get(handedness);
								notes.add(note);
							} else {
								handednessToStartTime.put(handedness,noteOnTime);
								List<Note> notes = new ArrayList<Note>();
								notes.add(note);
								handednessToNotes.put(handedness, notes);
							}
							if ( handednessToTimeOfPreviousElement.containsKey(handedness) ) {
								int timeOfPreviousElement = handednessToTimeOfPreviousElement.get(handedness);
								int timeUntilNextElement = noteOnTime - timeOfPreviousElement;
								List<Integer> timesUntilNextElement = handednessToTimesUntilNextElement.get(handedness);
								timesUntilNextElement.add(timeUntilNextElement);
								handednessToTimeOfPreviousElement.put(handedness, noteOnTime);
							} else {
								handednessToTimeOfPreviousElement.put(handedness, noteOnTime);
								handednessToTimesUntilNextElement.put(handedness, new ArrayList<Integer>());
							}
						}
					}
				}
			}
			List<Voice> voices = new ArrayList<Voice>();
			List<Integer> startTimes = new ArrayList<Integer>();
			int[] channelsAsArray = new int[channels.size()];
			int count = 0;
			for ( int channel : channels ) {
				channelsAsArray[count++] = channel;
			}
			Instrument instrument = new GeneralInstrument(programNumber,channelsAsArray);
			for ( Handedness handedness : handednessToNotes.keySet() ) {
				List<Note> notes = handednessToNotes.get(handedness);
				int startTime = handednessToStartTime.get(handedness);
				startTimes.add(startTime);
				List<Integer> timesUntilNextElement = handednessToTimesUntilNextElement.get(handedness);
				Voice voice = new MidiVoice(notes, timesUntilNextElement, handedness, instrument);
				voices.add(voice);
			}
			InstrumentPiece instrumentPiece = new InstrumentPiece(voices, startTimes, instrument);
			instrumentPieces.add(instrumentPiece);
		}
		return instrumentPieces;
	}
}
