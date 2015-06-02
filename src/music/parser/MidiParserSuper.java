package music.parser;

import java.io.*;
import java.util.*;

import javax.sound.midi.*;

import util.Pair;
import music.Music;
import music.Voice;
import music.midi_music.ChannelAccurateMidiVoice;
import music.midi_music.MidiControlChange;
import music.midi_music.MidiInstrument;
import music.midi_music.MidiNote;
import music.midi_music.SuperAccurateMidiVoice;
import music.midi_music.SuperMidiInstrument;

public class MidiParserSuper {
	private static final int DEFAULT_MICROSPQN = 500000;
	private static final double DEFAULT_BPQN = 1.0;
	private static final int DEFAULT_PROGRAM_NUMBER = 0;
	private static final int SET_TEMPO = 0x51;
	private static final int PERCUSSION_CHANNEL = 9;
    
    // Assumption about music: a channel will have at most one program change for any midi file.
    public static Music parse( String musicTitle, String filename ) throws MidiUnavailableException, InvalidMidiDataException, IOException {
    	// Get the midi sequence information
    	InputStream in = ClassLoader.getSystemResourceAsStream( filename );
    	Sequence sequence = MidiSystem.getSequence( in );
    	int resolution = sequence.getResolution();
    	
    	MidiEvents events = getMidiEvents(sequence);
    	Map<Integer,MidiSoundEvents> channelToSoundEvents = getChannelToSoundEvents(events);
    	Map<Integer,MidiControlEvents> channelToControlEvents = getChannelToControlEvents(events);
    	Map<Integer,Set<Integer>> programNumberToChannels = getProgramNumberToChannels(channelToSoundEvents);
    	Map<Integer,MidiSoundEvents> programNumberToSoundEvents = getProgramNumberToSoundEvents(channelToSoundEvents);
    	Map<Integer,MidiControlEvents> programNumberToControlEvents = getProgramNumberToControlEvents(channelToControlEvents, programNumberToChannels);
    	Map<Long,Integer> tickToTime = getTickToTime(events, resolution);
    			
    	List<Voice> voices = getVoices(programNumberToSoundEvents, programNumberToControlEvents, tickToTime, programNumberToChannels, resolution);
    	
    	Music music = new Music(musicTitle, voices);
    	
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
    					 command == ShortMessage.PROGRAM_CHANGE ||
    					 command == ShortMessage.CONTROL_CHANGE ) {
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
    
    private static Map<Integer,MidiControlEvents> getChannelToControlEvents(MidiEvents midiEvents) {
    	Map<Integer,MidiControlEvents> channelToEvents = new HashMap<Integer,MidiControlEvents>();
    	
    	for ( MidiEvent event : midiEvents ) {
    		MidiMessage message = event.getMessage();
    		if ( message instanceof ShortMessage ) {
    			ShortMessage sm = (ShortMessage) message;
    			int channel = sm.getChannel();
    			int command = sm.getCommand();
    			if ( command == ShortMessage.CONTROL_CHANGE ) {
    				MidiControlEvents controlEvents = new MidiControlEvents();
    				if ( channelToEvents.containsKey(channel) ) {
    					controlEvents = channelToEvents.get(channel);
    					controlEvents.add(event);
    				} else {
    					controlEvents.add(event);
    					channelToEvents.put(channel,controlEvents);
    				}
    			}
    		}
    	}
    	// Make sure to sort all the MidiSoundEvents
    	for ( MidiControlEvents midiControlEvents : channelToEvents.values() ) {
    		midiControlEvents.sort();
    	}
    	System.out.println(channelToEvents.size());
    	return channelToEvents;
    }
    
    private static Map<Integer,Set<Integer>> getProgramNumberToChannels(Map<Integer,MidiSoundEvents> channelToEvents) {
    	Map<Integer,Set<Integer>> programNumberToChannels = new HashMap<Integer,Set<Integer>>();
    	
    	boolean[] occupiedChannels = new boolean[16];
    	
    	for ( int channel : channelToEvents.keySet() ) {
    		if ( channel == PERCUSSION_CHANNEL ) {
    			occupiedChannels[channel] = true;
    			int programNumber = Integer.MAX_VALUE;
    			Set<Integer> channels = new HashSet<Integer>();
    			channels.add(channel);
    			programNumberToChannels.put(programNumber, channels);
    		} else {
	    		occupiedChannels[channel] = true;
	    		MidiSoundEvents events = channelToEvents.get(channel);
	    		lookForProgramNumber: for ( MidiEvent event : events ) {
	    			ShortMessage sm = (ShortMessage) event.getMessage();
	    			if ( sm.getCommand() == ShortMessage.PROGRAM_CHANGE ) {
	    				int programNumber = sm.getData1();
	    				if ( programNumberToChannels.containsKey(programNumber) ) {
	    					programNumberToChannels.get(programNumber).add(channel);
	    					break lookForProgramNumber;
	    				} else {
	    					Set<Integer> channels = new HashSet<Integer>();
	    					channels.add(channel);
	    					programNumberToChannels.put(programNumber,channels);
	    					break lookForProgramNumber;
	    				}
	    			}
	    		}
    		}
    		// At this point in the code, there is no programChange.
    		// If the channel is percussion, then it is reasonable for there to be
    		// not program change. Otherwise, we assume the program number is 0.
    		if ( channel == PERCUSSION_CHANNEL ) {
    			int programNumber = Integer.MAX_VALUE;
				Set<Integer> channels = new HashSet<Integer>();
				channels.add(channel);
				programNumberToChannels.put(programNumber,channels);
    		} else {
    			if ( programNumberToChannels.containsKey(DEFAULT_PROGRAM_NUMBER) ) {
    				Set<Integer> channels = programNumberToChannels.get(DEFAULT_PROGRAM_NUMBER);
    				channels.add(channel);
    			} else {
    				Set<Integer> channels = new HashSet<Integer>();
    				channels.add(channel);
    				programNumberToChannels.put(DEFAULT_PROGRAM_NUMBER, channels);
    			}
    		}
    	}
    	// We do not allocate channels here to be more accurate with the midi file.
    	Set<Integer> programNumbers = programNumberToChannels.keySet();
    	// If there is a percussion make sure to reset program number to 
    	// something appropriate
    	if ( programNumbers.contains(Integer.MAX_VALUE) ) {
    		Set<Integer> channels = programNumberToChannels.get(Integer.MAX_VALUE);
    		int programNumber = 0;
    		while ( programNumbers.contains(programNumber) ) {
    			programNumber++;
    		}
    		programNumberToChannels.remove(Integer.MAX_VALUE);
    		programNumberToChannels.put(programNumber,channels);
    	}
    	return programNumberToChannels;
    }
    
	private static Map<Integer,MidiSoundEvents> getProgramNumberToSoundEvents(Map<Integer,MidiSoundEvents> channelToEvents) {
		Map<Integer,MidiSoundEvents> programNumberToEvents = new HashMap<Integer,MidiSoundEvents>();
		
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
		// Redistribute the channels to handle percussion instrument and 
		// also improving the quality of the sound
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
	
	private static Map<Integer,MidiControlEvents> getProgramNumberToControlEvents(Map<Integer,MidiControlEvents> channelToEvents, Map<Integer,Set<Integer>> programNumberToChannels) {
		Map<Integer,MidiControlEvents> programNumberToEvents = new HashMap<Integer,MidiControlEvents>();
		
		for ( int programNumber : programNumberToChannels.keySet() ) {
			MidiControlEvents events = new MidiControlEvents();
			Set<Integer> channels = programNumberToChannels.get(programNumber);
			for ( int channel : channels ) {
				if ( channelToEvents.containsKey(channel) ) {
					MidiControlEvents controlEvents = channelToEvents.get(channel);
					for ( MidiEvent event : controlEvents ) {
						events.add(event);
					}
				}
			}
			programNumberToEvents.put(programNumber,events);
		}
		
		for ( MidiControlEvents midiControlEvents : programNumberToEvents.values() ) {
			midiControlEvents.sort();
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
	
	private static MidiEvent findNoteOnEvent(List<MidiEvent> noteOnEvents, int value, int channel) {
		for ( MidiEvent noteOnEvent : noteOnEvents ) {
			MidiMessage noteOnMessage = noteOnEvent.getMessage();
			ShortMessage noteOnSM = (ShortMessage) noteOnMessage;
			int noteOnValue = noteOnSM.getData1();
			int noteOnChannel = noteOnSM.getChannel();
			if ( noteOnValue == value && noteOnChannel == channel ) {
				return noteOnEvent;
			}
		}
		throw new RuntimeException("No note on to match to");
	}
	
	private static List<MidiNote> getMidiNotes(List<Pair<MidiNote,Long>> notesAndTicks) {
		notesAndTicks.sort(new Comparator<Pair<MidiNote,Long>>() {
			@Override
			public int compare(Pair<MidiNote, Long> o1, Pair<MidiNote, Long> o2) {
				long tick1 = o1.getRight();
				long tick2 = o2.getRight();
				return (int) (tick1 - tick2);
			}
		});
		List<MidiNote> notes = new ArrayList<MidiNote>();
		for ( Pair<MidiNote,Long> noteAndTick : notesAndTicks ) {
			notes.add(noteAndTick.getLeft());
		}
		return notes;
	}
	
	private static List<Integer> getTimesUntilNextElement(List<Pair<MidiNote,Long>> notesAndTicks, Map<Long,Integer> tickToTime, int startTime) {
		List<Integer> timesUntilNextElement = new ArrayList<Integer>();
		timesUntilNextElement.add(startTime);
		
		for ( int i=0; i < notesAndTicks.size()-1; i++ ) {
			long firstTick = notesAndTicks.get(i).getRight();
			long secondTick = notesAndTicks.get(i+1).getRight();
			int firstTime = tickToTime.get(firstTick);
			int secondTime = tickToTime.get(secondTick);
			int timeUntilNextElement = secondTime - firstTime;
			timesUntilNextElement.add(timeUntilNextElement);
		}
		
		return timesUntilNextElement;
	}
	
	private static List<MidiControlChange> getMidiControlChanges(MidiControlEvents events) {
		List<MidiControlChange> controlChanges = new ArrayList<MidiControlChange>();
		
		for ( MidiEvent event : events ) {
			MidiMessage message = event.getMessage();
			ShortMessage sm = (ShortMessage) message;
			int channel = sm.getChannel();
			int controller = sm.getData1();
			int value = sm.getData2();
			MidiControlChange controlChange = new MidiControlChange(channel, controller, value);
			controlChanges.add(controlChange);
		}
		
		return controlChanges;
	}
	
	private static List<Integer> getTimesUntilNextCommand(MidiControlEvents events, Map<Long,Integer> tickToTime) {
		List<Integer> timesUntilNextCommand = new ArrayList<Integer>();
		
		int oldTime = 0;
		for ( MidiEvent event : events ) {
			long tick = event.getTick();
			int currentTime = tickToTime.get(tick);
			int timeElapsed = currentTime - oldTime;
			timesUntilNextCommand.add(timeElapsed);
		}
		
		return timesUntilNextCommand;
	}
	
	
	private static List<Voice> getVoices(Map<Integer,MidiSoundEvents> programNumberToSoundEvents, Map<Integer,MidiControlEvents> programNumberToControlEvents, Map<Long,Integer> tickToTime, Map<Integer,Set<Integer>> programNumberToChannels, int resolution) {
		List<Voice> voices = new ArrayList<Voice>();
		
		for ( int programNumber : programNumberToSoundEvents.keySet() ) {
			// Get the notes for each hand in order to make the voices for each program number
			List<Pair<MidiNote,Long>> notesAndTicks = new ArrayList<Pair<MidiNote,Long>>();
			int startTime = 0;
			
			// handle the sound events
			MidiSoundEvents events = programNumberToSoundEvents.get(programNumber);
			List<MidiEvent> noteOnEvents = new LinkedList<MidiEvent>();
			for ( MidiEvent event : events ) {
				MidiMessage message = event.getMessage();
				if ( message instanceof ShortMessage ) {
					ShortMessage sm = (ShortMessage) message;
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
							int channel = sm.getChannel();
							MidiEvent noteOnEvent = findNoteOnEvent(noteOnEvents, value, channel);
							noteOnEvents.remove(noteOnEvent);
							int noteOnTime = tickToTime.get(noteOnEvent.getTick());
							MidiMessage noteOnMessage = noteOnEvent.getMessage();
							ShortMessage noteOnSM = (ShortMessage) noteOnMessage;
							int noteOnVelocity = noteOnSM.getData1();
							int noteOffTime = tickToTime.get(event.getTick());
							int timeElapsed = noteOffTime - noteOnTime;
							MidiNote note = new MidiNote(value,timeElapsed,noteOnVelocity,channel);
							note.setTick(noteOnEvent.getTick());
							if ( notesAndTicks.isEmpty() ) {
								startTime = noteOnTime;
							}
							notesAndTicks.add(new Pair<MidiNote,Long>(note,noteOnEvent.getTick()));
						}
					}
				}
			}
			// get midi control changes
			MidiControlEvents controlEvents = programNumberToControlEvents.get(programNumber);
			
			Set<Integer> channels = programNumberToChannels.get(programNumber);
			int[] channelsAsArray = new int[channels.size()];
			int count = 0;
			for ( int channel : channels ) {
				channelsAsArray[count++] = channel;
			}
					
			SuperMidiInstrument instrument = new SuperMidiInstrument(programNumber,channelsAsArray);
			List<MidiNote> notes = getMidiNotes(notesAndTicks);
			List<Integer> timesUntilNextElement = getTimesUntilNextElement(notesAndTicks, tickToTime, startTime);
			List<MidiControlChange> controlChanges = getMidiControlChanges(controlEvents);
			List<Integer> timesUntilNextCommand = getTimesUntilNextCommand(controlEvents, tickToTime);
			SuperAccurateMidiVoice voice = new SuperAccurateMidiVoice(notes, timesUntilNextElement, controlChanges, timesUntilNextCommand, instrument);
			instrument.teachInstrument(voice);
			voices.add(voice);
		}
		return voices;
	}
}

