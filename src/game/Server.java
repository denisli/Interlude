package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Server is a thread safe implementation of a server in the Interlude game.
 *
 */
public class Server {
    /**
     * Thread Safety Argument:
     *      thread safety?
     */
    private static Optional<ServerSocket> serverSocket; 
    private static Set<Socket> friends;
    
    public static void start() throws UnknownHostException, IOException {
        serverSocket = Optional.of(new ServerSocket(4444));
        serve();
    }
    
    private static void serve() throws IOException {
        boolean operating = serverSocket.isPresent();
        while ( operating ) {
            Socket clientSocket = serverSocket.get().accept();
            
            new Thread(new Runnable() {
                public void run() {
                    try{ 
                        try {
                            handleClientMessages( clientSocket );
                        } finally {
                            clientSocket.close();
                        }
                    } catch ( IOException ioe ) {
                        
                    }
                }
            }).start(); 
        }
    }
    
    private static void handleClientMessages( Socket clientSocket ) throws IOException {
        BufferedReader br = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );
        
        for ( String line = br.readLine(); line != null; line = br.readLine() ) {
            for ( Socket friend : friends ) {
                BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(friend.getOutputStream()) );
                bw.write(line + "\n");
            }
        }
    }
    
    public static boolean isOperating() {
        synchronized (serverSocket) {
            return serverSocket.isPresent();
        }
    }
    
    public static void stopRunning() {
        serverSocket = Optional.empty();
    }
}
