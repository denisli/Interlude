package music.parser;

import java.io.*;
import java.util.*;

import javax.sound.midi.*;

import util.Pair;
import music.GeneralInstrument;
import music.Instrument;
import music.MidiVoice;
import music.Music;
import music.Note;
import music.Voice;

public class MidiParser {
	private static final int DEFAULT_MICROSPQN = 500000;
	private static final double DEFAULT_BPQN = 1.0;
	private static final int DEFAULT_PROGRAM_NUMBER = 0;
	private static final int SET_TEMPO = 0x51;
	private static final int PERCUSSION_CHANNEL = 9;

	// Assumption about music: a channel will have at most one program change
	// for any midi file.
	public static Music parse(String musicTitle, String filename)
			throws MidiUnavailableException, InvalidMidiDataException,
			IOException {
		// Get the midi sequence information
		InputStream in = ClassLoader.getSystemResourceAsStream(filename);
		Sequence sequence = MidiSystem.getSequence(in);
		int resolution = sequence.getResolution();

		MidiEvents events = getMidiEvents(sequence);
		Map<Integer, MidiSoundEvents> channelToSoundEvents = getChannelToSoundEvents(events);
		Map<Integer, Set<Integer>> programNumberToChannels = getProgramNumberToChannels(channelToSoundEvents);
		Map<Integer, MidiSoundEvents> programNumberToSoundEvents = getProgramNumberToSoundEvents(channelToSoundEvents);
		Map<Long, Integer> tickToTime = getTickToTime(events, resolution);

		List<Voice> voices = getVoices(programNumberToSoundEvents, tickToTime,
				programNumberToChannels, resolution);

		Music music = new Music(musicTitle, voices);

		return music;
	}

	// Gets the sequence of events related to tempo and sound.
	private static MidiEvents getMidiEvents(Sequence sequence) {
		MidiEvents midiEvents = new MidiEvents();
		for (Track track : sequence.getTracks()) {
			for (int i = 0; i < track.size(); i++) {
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;
					int command = sm.getCommand();
					if (command == ShortMessage.NOTE_ON
							|| command == ShortMessage.NOTE_OFF
							|| command == ShortMessage.PROGRAM_CHANGE) {
						midiEvents.add(event);
					}
				} else if (message instanceof MetaMessage) {
					MetaMessage mm = (MetaMessage) message;
					int type = mm.getType();
					if (type == SET_TEMPO) {
						midiEvents.add(event);
					}
				}
			}
		}
		midiEvents.sort();
		return midiEvents;
	}

	private static Map<Integer, MidiSoundEvents> getChannelToSoundEvents(
			MidiEvents midiEvents) {
		Map<Integer, MidiSoundEvents> channelToEvents = new HashMap<Integer, MidiSoundEvents>();

		for (MidiEvent event : midiEvents) {
			MidiMessage message = event.getMessage();
			if (message instanceof ShortMessage) {
				ShortMessage sm = (ShortMessage) message;
				int channel = sm.getChannel();
				int command = sm.getCommand();
				if (command == ShortMessage.NOTE_OFF) {
					MidiSoundEvents soundEvents = new MidiSoundEvents();
					if (channelToEvents.containsKey(channel)) {
						soundEvents = channelToEvents.get(channel);
						soundEvents.add(event);
					} else {
						soundEvents.add(event);
						channelToEvents.put(channel, soundEvents);
					}
				} else if (command == ShortMessage.NOTE_ON) {
					MidiSoundEvents soundEvents = new MidiSoundEvents();
					if (channelToEvents.containsKey(channel)) {
						soundEvents = channelToEvents.get(channel);
						soundEvents.add(event);
					} else {
						soundEvents.add(event);
						channelToEvents.put(channel, soundEvents);
					}
				} else if (command == ShortMessage.PROGRAM_CHANGE) {
					MidiSoundEvents soundEvents = new MidiSoundEvents();
					if (channelToEvents.containsKey(channel)) {
						soundEvents = channelToEvents.get(channel);
						soundEvents.add(event);
					} else {
						soundEvents.add(event);
						channelToEvents.put(channel, soundEvents);
					}
				}
			}
		}
		// Make sure to sort all the MidiSoundEvents
		for (MidiSoundEvents midiSoundEvents : channelToEvents.values()) {
			midiSoundEvents.sort();
		}
		return channelToEvents;
	}

	private static Map<Integer, Set<Integer>> getProgramNumberToChannels(
			Map<Integer, MidiSoundEvents> channelToEvents) {
		Map<Integer, Set<Integer>> programNumberToChannels = new HashMap<Integer, Set<Integer>>();

		final int NUM_AVAILABLE_CHANNELS = 15;
		boolean[] occupiedChannels = new boolean[16];
		int numOccupiedChannels = 0;

		lookForProgramNumber: for (int channel : channelToEvents.keySet()) {
			if (channel == PERCUSSION_CHANNEL) {
				occupiedChannels[channel] = true;
				numOccupiedChannels++;
				int programNumber = Integer.MAX_VALUE;
				Set<Integer> channels = new HashSet<Integer>();
				channels.add(channel);
				programNumberToChannels.put(programNumber, channels);
			} else {
				occupiedChannels[channel] = true;
				numOccupiedChannels++;
				MidiSoundEvents events = channelToEvents.get(channel);
				for (MidiEvent event : events) {
					ShortMessage sm = (ShortMessage) event.getMessage();
					if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE) {
						// System.out.println("Ever here?");
						int programNumber = sm.getData1();
						if (programNumberToChannels.containsKey(programNumber)) {
							programNumberToChannels.get(programNumber).add(
									channel);
							continue lookForProgramNumber;
						} else {
							Set<Integer> channels = new HashSet<Integer>();
							channels.add(channel);
							programNumberToChannels
									.put(programNumber, channels);
							continue lookForProgramNumber;
						}
					}
				}
			}
			// At this point in the code, there is no programChange.
			// If the channel is percussion, then it is reasonable for there to
			// be
			// not program change. Otherwise, we assume the program number is 0.
			if (channel == PERCUSSION_CHANNEL) {
				int programNumber = Integer.MAX_VALUE;
				Set<Integer> channels = new HashSet<Integer>();
				channels.add(channel);
				programNumberToChannels.put(programNumber, channels);
			} else {
				System.out
						.println("No program number was assigned to a non-percussion channel.");
				if (programNumberToChannels.containsKey(DEFAULT_PROGRAM_NUMBER)) {
					Set<Integer> channels = programNumberToChannels
							.get(DEFAULT_PROGRAM_NUMBER);
					channels.add(channel);
				} else {
					Set<Integer> channels = new HashSet<Integer>();
					channels.add(channel);
					programNumberToChannels.put(DEFAULT_PROGRAM_NUMBER,
							channels);
				}
			}
		}
		// Allocate channels so that we can further improve the probability of
		// avoiding
		// notes canceling each other.
		Set<Integer> programNumbers = programNumberToChannels.keySet();
		int numProgramNumbers = programNumbers.size();
		for (int programNumber : programNumbers) {
			if (programNumberToChannels.get(programNumber).contains(
					PERCUSSION_CHANNEL))
				continue;
			allocateChannels: while (numOccupiedChannels < NUM_AVAILABLE_CHANNELS) {
				int numChannelsToAllocate = (int) Math
						.ceil((double) (NUM_AVAILABLE_CHANNELS - numOccupiedChannels)
								/ numProgramNumbers);
				for (int channel = 0; channel < occupiedChannels.length; channel++) {
					boolean isOccupied = occupiedChannels[channel];
					if (channel == PERCUSSION_CHANNEL) {
						continue;
					}
					if (!isOccupied) {
						occupiedChannels[channel] = true;
						programNumberToChannels.get(programNumber).add(channel);
						numChannelsToAllocate--;
						numOccupiedChannels++;
						if (numChannelsToAllocate == 0) {
							break allocateChannels;
						}
					}
				}
			}
		}
		// If there is a percussion make sure to reset program number to
		// something appropriate
		if (programNumbers.contains(Integer.MAX_VALUE)) {
			Set<Integer> channels = programNumberToChannels
					.get(Integer.MAX_VALUE);
			int programNumber = 0;
			while (programNumbers.contains(programNumber)) {
				programNumber++;
			}
			programNumberToChannels.remove(Integer.MAX_VALUE);
			programNumberToChannels.put(programNumber, channels);
		}
		return programNumberToChannels;
	}

	private static Map<Integer, MidiSoundEvents> getProgramNumberToSoundEvents(
			Map<Integer, MidiSoundEvents> channelToEvents) {
		Map<Integer, MidiSoundEvents> programNumberToEvents = new HashMap<Integer, MidiSoundEvents>();

		for (Integer channel : channelToEvents.keySet()) {
			MidiSoundEvents events = channelToEvents.get(channel);
			// Check if the first message is a program change message
			MidiEvent firstEvent = events.get(0);
			ShortMessage firstSM = (ShortMessage) firstEvent.getMessage();
			int programNumber = DEFAULT_PROGRAM_NUMBER;
			if (channel == PERCUSSION_CHANNEL) {
				programNumber = Integer.MAX_VALUE;
			} else {
				if (firstSM.getCommand() == ShortMessage.PROGRAM_CHANGE) {
					programNumber = firstSM.getData1();
				}
			}
			MidiSoundEvents programSoundEvents = new MidiSoundEvents();
			if (programNumberToEvents.containsKey(programNumber)) {
				programSoundEvents = programNumberToEvents.get(programNumber);
			} else {
				programNumberToEvents.put(programNumber, programSoundEvents);
			}
			for (MidiEvent event : events) {
				programSoundEvents.add(event);
			}
		}
		// Redistribute the channels to handle percussion instrument and
		// also improving the quality of the sound
		if (programNumberToEvents.containsKey(Integer.MAX_VALUE)) {
			MidiSoundEvents events = programNumberToEvents
					.get(Integer.MAX_VALUE);
			programNumberToEvents.remove(Integer.MAX_VALUE);
			// Put the percussion events into a legitimate program number.
			for (int i = 0; i < 128; i++) {
				if (!programNumberToEvents.containsKey(i)) {
					programNumberToEvents.put(i, events);
					break;
				}
			}
		}
		// Make sure to sort the MidiSoundEvents again
		for (MidiSoundEvents midiSoundEvents : programNumberToEvents.values()) {
			midiSoundEvents.sort();
		}
		return programNumberToEvents;
	}

	// midiEvents is sorted sequence of MidiEvents
	private static Map<Long, Integer> getTickToTime(MidiEvents midiEvents,
			int resolution) {
		Map<Long, Integer> tickToTime = new HashMap<Long, Integer>();

		int ticksPerBeat = resolution;
		long currentTick = 0;
		int currentTime = 0;
		double BPQN = DEFAULT_BPQN;
		double currentMillisecondsPerTick = DEFAULT_MICROSPQN
				/ (ticksPerBeat * BPQN * 1000);

		for (MidiEvent event : midiEvents) {
			// First get the time of the event
			long tick = event.getTick();
			int time = currentTime
					+ (int) ((tick - currentTick) * currentMillisecondsPerTick);
			tickToTime.put(tick, time);
			// Update the time and current time
			currentTime = time;
			currentTick = tick;
			// Update the current milliseconds per tick if necessary
			MidiMessage message = event.getMessage();
			if (message instanceof MetaMessage) {
				MetaMessage mm = (MetaMessage) message;
				int type = mm.getType();
				if (type == SET_TEMPO) {
					byte[] bytes = mm.getMessage();
					int MPQNByte1 = bytes[3] & 0xFF;
					int MPQNByte2 = bytes[4] & 0xFF;
					int MPQNByte3 = bytes[5] & 0xFF;

					int MICROSPQN = MPQNByte1 * 0xFF * 0xFF + MPQNByte2 * 0xFF
							+ MPQNByte3;
					currentMillisecondsPerTick = MICROSPQN
							/ (ticksPerBeat * BPQN * 1000);
				}
			}
		}
		return tickToTime;
	}

	private static MidiEvent findNoteOnEvent(List<MidiEvent> noteOnEvents,
			int value) {
		for (MidiEvent noteOnEvent : noteOnEvents) {
			MidiMessage noteOnMessage = noteOnEvent.getMessage();
			ShortMessage noteOnSM = (ShortMessage) noteOnMessage;
			int noteOnValue = noteOnSM.getData1();
			if (noteOnValue == value) {
				return noteOnEvent;
			}
		}
		// throw new RuntimeException("No note on to match to");
		System.out.println("No note on to match to");
		return null;
	}

	private static List<Note> getNotes(List<Pair<Note, Long>> notesAndTicks) {
		notesAndTicks.sort(new Comparator<Pair<Note, Long>>() {
			@Override
			public int compare(Pair<Note, Long> o1, Pair<Note, Long> o2) {
				long tick1 = o1.getRight();
				long tick2 = o2.getRight();
				return (int) (tick1 - tick2);
			}
		});
		List<Note> notes = new ArrayList<Note>();
		for (Pair<Note, Long> noteAndTick : notesAndTicks) {
			notes.add(noteAndTick.getLeft());
		}
		return notes;
	}

	private static List<Integer> getTimesUntilNextElement(
			List<Pair<Note, Long>> notesAndTicks,
			Map<Long, Integer> tickToTime, int startTime) {
		List<Integer> timesUntilNextElement = new ArrayList<Integer>();
		timesUntilNextElement.add(startTime);

		for (int i = 0; i < notesAndTicks.size() - 1; i++) {
			long firstTick = notesAndTicks.get(i).getRight();
			long secondTick = notesAndTicks.get(i + 1).getRight();
			int firstTime = tickToTime.get(firstTick);
			int secondTime = tickToTime.get(secondTick);
			int timeUntilNextElement = secondTime - firstTime;
			timesUntilNextElement.add(timeUntilNextElement);
		}

		return timesUntilNextElement;
	}

	private static List<Voice> getVoices(
			Map<Integer, MidiSoundEvents> programNumberToSoundEvents,
			Map<Long, Integer> tickToTime,
			Map<Integer, Set<Integer>> programNumberToChannels, int resolution) {
		List<Voice> voices = new ArrayList<Voice>();

		for (int programNumber : programNumberToSoundEvents.keySet()) {
			// Get the notes for each hand in order to make the voices for each
			// program number
			List<Pair<Note, Long>> notesAndTicks = new ArrayList<Pair<Note, Long>>();
			int startTime = 0;

			MidiSoundEvents events = programNumberToSoundEvents
					.get(programNumber);
			List<MidiEvent> noteOnEvents = new LinkedList<MidiEvent>();
			for (MidiEvent event : events) {
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;
					int command = sm.getCommand();
					if (command == ShortMessage.NOTE_ON
							|| command == ShortMessage.NOTE_OFF) {
						boolean isNoteOn = true;
						if (command == ShortMessage.NOTE_ON) {
							int velocity = sm.getData2();
							if (velocity != 0) {
								noteOnEvents.add(event);
							} else {
								isNoteOn = false;
							}
						} else {
							isNoteOn = false;
						}
						if (!isNoteOn) {
							int value = sm.getData1();
							MidiEvent noteOnEvent = findNoteOnEvent(
									noteOnEvents, value);
							if (noteOnEvent == null)
								continue; // ignore the null note
							noteOnEvents.remove(noteOnEvent);
							int noteOnTime = tickToTime.get(noteOnEvent
									.getTick());
							MidiMessage noteOnMessage = noteOnEvent
									.getMessage();
							ShortMessage noteOnSM = (ShortMessage) noteOnMessage;
							int noteOnVelocity = noteOnSM.getData1();
							int noteOffTime = tickToTime.get(event.getTick());
							int timeElapsed = noteOffTime - noteOnTime;
							Note note = new Note(value, timeElapsed,
									noteOnVelocity);
							note.setTick(noteOnEvent.getTick());
							if (notesAndTicks.isEmpty()) {
								startTime = noteOnTime;
							}
							notesAndTicks.add(new Pair<Note, Long>(note,
									noteOnEvent.getTick()));
						}
					}
				}
			}
			Set<Integer> channels = programNumberToChannels.get(programNumber);
			int[] channelsAsArray = new int[channels.size()];
			int count = 0;
			for (int channel : channels) {
				channelsAsArray[count++] = channel;
			}

			Instrument instrument = new GeneralInstrument(programNumber,
					channelsAsArray);
			List<Note> notes = getNotes(notesAndTicks);
			List<Integer> timesUntilNextElement = getTimesUntilNextElement(
					notesAndTicks, tickToTime, startTime);
			Voice voice = new MidiVoice(notes, timesUntilNextElement,
					instrument);
			voices.add(voice);
		}
		return voices;
	}
}
