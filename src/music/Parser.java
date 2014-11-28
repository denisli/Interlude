package music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Parser {
    public static Music fileToMusic(File file) throws FileNotFoundException {
        Music music = new Music();
        BufferedReader br = new BufferedReader(new FileReader(file) );
        try {
            // first line of file contains the title and tempo of music (space-separated)
            String[] titleAndTempo = br.readLine().split(" ");
            music.setTitle(titleAndTempo[0]); music.setTempo(Integer.valueOf(titleAndTempo[1]));
            
            
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return music;
    }
}
