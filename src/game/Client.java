package game;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {
    // Thread for reading.
    // Thread for writing.
    // Thread safe queue for collecting messages to send.
    private static ConcurrentLinkedQueue<String> messagesToSend = new ConcurrentLinkedQueue<String>();
    private static ConcurrentLinkedQueue<String> messagesToProcess = new ConcurrentLinkedQueue<String>();
    private static Optional<Socket> serverSocket;
    
    public static void connectToServer( String host, int port ) throws UnknownHostException, IOException {
        System.out.println("Connected to server");
        serverSocket = Optional.of(new Socket(host, port));
        receiveMessages();
        sendMessages();
    }
    
    /**
     * Tells whether or not there are any messages to process
     * @return boolean, true if there are no more messages to process and false otherwise.
     */
    public static boolean noMoreMessagesToProcess() {
    	return messagesToProcess.isEmpty();
    }
    
    /**
     * Picks off a message sent from server that needs to be processed
     * @return a message that needs to be processed
     */
    public static String removeMessageToProcess() {
    	return messagesToProcess.remove();
    }
    
    /**
     * Adds a message to a queue of messages that will be sent to server.
     * @param message
     */
    public static void addMessageToSend(String message) {
    	messagesToSend.add(message);
    }
    
    /**
     * Returns whether or not the client is currently connected to a server.
     * @return whether or not the client is connected to a server.
     */
    public static boolean isConnected() {
        return serverSocket.isPresent();
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
                        messagesToProcess.add(line);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }
    
    /**
     * Sends all enqueued message requests to the server.
     * @throws IOException
     */
    private static void sendMessages() throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(serverSocket.get().getOutputStream()));
                    while (true) {
                        if ( !messagesToSend.isEmpty() ) {
                            String message = messagesToSend.remove();
                            bw.write(message + "\n");
                            bw.flush();
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }
}
