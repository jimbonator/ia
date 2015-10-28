/**
 * most-ip
 *
 * Jim Nelson <jimbonator@gmail.com>
 */

import java.io.*;
import java.util.stream.*;

public class MostIp extends Object {
    private final File file;
    
    public static void main(String[] args) {
        if (args.length == 0) {
            help();
            
            return;
        }
        
        for (String arg : args) {
            try {
                new MostIp(new File(arg)).run();
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
    
    private static void help() {
        System.out.println("most-ip [file...]");
    }
    
    public MostIp(File file) {
        this.file = file;
    }
    
    public void run() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            Stream<String> lines = reader.lines();
        } finally {
            reader.close();
        }
    }
}

