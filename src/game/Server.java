package game;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static boolean operating = false;
    private static DatagramSocket serverSocket; 
    private static Set<DatagramSocket> FRIENDS;
    
    public static void start() throws SocketException {
        operating = true;
        serverSocket = new DatagramSocket(4444);
    }
    
    private static void serve() {
        new Thread(new Runnable() {
            public void run() {
                while ( operating ) {
                    
                }
            }
        }).start();
    }
    
    public static boolean isOperating() {
        return operating;
    }
    
    public static void sendMessage( String message ) {
        
    }
}
