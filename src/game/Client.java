package game;

import music.Instrument;
import music.MusicElement;
import music.Note;
import music.Simultaneous;
import music.SoundElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {
    // Thread for reading.
    // Thread for writing.
    // Thread safe queue for collecting messages to send.
    private static ConcurrentLinkedQueue<String> messagesToSend = new ConcurrentLinkedQueue<String>();
    private static Optional<Socket> serverSocket;
    private static Instrument piano;
    
    public static void connectToServer( String host, int port ) throws UnknownHostException, IOException {
        // TODO: Change this out for an actual instrument. Or consider switchng to a different design.
        piano = Instrument.piano();
        // TODO: Change the above.
        
        System.out.println("Connected to server");
        serverSocket = Optional.of(new Socket(host, port));
        receiveMessages();
        sendMessages();
    }
    
    /**
     * Receives messages from server. The types of messages that can be received are:
     *      -note messages telling the client what note was played
     *      -instrument messages telling the client which instruments are picked
     * 
     * Note messages have the following form:
     *      play NOTE+
     *      NOTE := value duration volume
     *      value is the pitch of the note using MIDI numbers
     *      duration is how long the note is in milliseconds
     *      volume is how long the note is using MIDI measures
     *      
     * Instrument messages have the following form:
     *      instrument instrumentName
     * 
     */
    private static void receiveMessages() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(serverSocket.get().getInputStream()));
                    for ( String line = br.readLine(); line != null; line = br.readLine() ) {
                        Queue<String> tokens = new LinkedList<String>(Arrays.asList(line.split(" ")));
                        String token = tokens.remove();
                        if ( token.equals("play") ) {
                            int programNumber = Integer.parseInt(tokens.remove());
                            List<MusicElement> simultaneousNotes = new ArrayList<MusicElement>();
                            while ( !tokens.isEmpty() ) {
                                int value = Integer.parseInt(tokens.remove());
                                int duration = Integer.parseInt(tokens.remove());
                                int volume = Integer.parseInt(tokens.remove());
                                Note note = new Note(value, duration, volume);
                                simultaneousNotes.add( note );
                            }
                            if ( simultaneousNotes.size() == 1 ) {
                                SoundElement simultaneous = new Simultaneous( simultaneousNotes );
                                simultaneous.bePlayed( piano );
                            }
                        }
                    }
                    br.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }
    
    private static void sendMessages() throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(serverSocket.get().getOutputStream()));
                    while (true) {
                        if ( !messagesToSend.isEmpty() ) {
                            String message = messagesToSend.remove();
                            bw.write(message + "\n");
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }
    
    public static boolean isConnected() {
        return serverSocket.isPresent();
    }
}
