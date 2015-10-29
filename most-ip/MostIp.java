/**
 * most-ip
 *
 * Displays the top 'n' IPv4 addresses stored in a log file based on number of accesses.
 *
 * Jim Nelson <jimbonator@gmail.com>
 */

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class MostIp extends Object {
    /**
     * Internal Comparable class associating an IPv4 address with the number of accesses counted
     * in the log file.
     */
    private class IpCount extends Object implements Comparable<IpCount> {
        public final String ip;
        public final int count;
        
        public IpCount(String ip, int count) {
            this.ip = ip;
            this.count = count;
        }
        
        /**
         * Sorts from highest to lowest, using the IPv4 addresses' lexiographic (not numeric!)
         * order to stabilize the sort.
         */
        public int compareTo(IpCount other) {
            // count is sorted in descending other
            int cmp = other.count - count;
            if (cmp != 0)
                return cmp;
            
            // IP address in ascending order
            return ip.compareTo(other.ip);
        }
    }
    
    // Basic IPv4 regular expression ... address must be first on the line with all 4 fields
    // listed.  This does not cover IPv6 nor does it verify that each class of the address is
    // within proper range.
    private static final Pattern regex = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}\\h");
    
    private final File file;
    private final TreeSet<IpCount> ipcounts = new TreeSet<IpCount>();
    
    public static void main(String[] args) {
        if (args.length != 2) {
            help();
            
            return;
        }
        
        MostIp most_ip = new MostIp(new File(args[0]));
        try {
            most_ip.run();
        } catch (Exception e) {
            System.err.println(e.toString());
            
            return;
        }
        
        most_ip.dumpTop(Integer.decode(args[1]), System.out);
    }
    
    private static void help() {
        System.out.println("most-ip [file] [count]");
    }
    
    public MostIp(File file) {
        this.file = file;
    }
    
    public void run() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            process(reader);
        } finally {
            reader.close();
        }
    }
    
    // Read the file one line at a time, processing each line and preparing the ipcounts table
    private void process(BufferedReader reader) throws Exception {
        HashMap<String, Integer> iptable = new HashMap<String, Integer>();
        
        // For each line, match IPv4 address at start of line and add it to iptable, which maps
        // the String to an integer count
        for (;;) {
            String line = reader.readLine();
            if (line == null)
                break;
            
            Matcher matcher = regex.matcher(line);
            if (!matcher.find())
                continue;
            
            String ip = matcher.group(0);
            
            // If IP address present, increment its count, otherwise add it the first time with
            // an initial count
            Integer count = iptable.get(ip);
            iptable.put(ip, count != null ? count + 1 : 1);
        }
        
        // Now populate the IpCount TreeSet, which will order each element by its count
        for (String ip : iptable.keySet())
            ipcounts.add(new IpCount(ip, iptable.get(ip)));
    }
    
    // Print the top 'n' IP addresses found in the file ... all ties within the top 'n' are printed
    private void dumpTop(int count, PrintStream pr) {
        int lastCount = -1, pos = 0;
        for (IpCount ipcount : ipcounts) {
            // if the seen count has changed, treat as a new position within the top elements
            if (lastCount != ipcount.count) {
                lastCount = ipcount.count;
                if (++pos > count)
                    return;
            }
            
            pr.printf("%d %s\n", ipcount.count, ipcount.ip);
        }
    }
}

