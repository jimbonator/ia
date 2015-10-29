/**
 * most-ip
 *
 * Displays the top 'n' IPv4 addresses stored in a log file based on number of accesses.
 *
 * The heart of this program is the process() method, which reads the file line-by-line, uses a
 * simple regular expression to parse the IP address from the start of the line (which also provides
 * basic verification of its format), and keeps count of the number of times seen via a Map of
 * IP addresses to integers.  Then the Map is converted to a TreeSet of IpCount's, which sorts the
 * addresses by the times seen.
 *
 * Jim Nelson <jimbonator@gmail.com>
 */

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class MostIp extends Object {
    public static final int TOP_COUNT = 10;
    
    // Internal Comparable class associating an IPv4 address with the number of accesses counted
    // in the log file.
    private class IpCount extends Object implements Comparable<IpCount> {
        public final String ip;
        public final int seen;
        
        public IpCount(String ip, int seen) {
            this.ip = ip;
            this.seen = seen;
        }
        
        // Sorts from highest to lowest, using the IPv4 addresses' lexiographic (not numeric!)
        // order to stabilize the sort.
        public int compareTo(IpCount other) {
            // seen is sorted in descending other
            int cmp = other.seen - seen;
            if (cmp != 0)
                return cmp;
            
            // IP address in ascending order
            return ip.compareTo(other.ip);
        }
    }
    
    // Basic IPv4 regular expression ... address must be first on the line with all 4 dotted fields
    // listed and whitespace after the last numeral.
    //
    // This does not cover IPv6 nor does it verify that each class of the address is within proper
    // range.
    private static final Pattern regex = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}\\h");
    
    private final File file;
    private final TreeSet<IpCount> ipcounts = new TreeSet<IpCount>();
    
    public static void main(String[] args) {
        File file;
        try {
            file = new File(args[0]);
        } catch (Exception e) {
            System.out.println("most-ip [file]");
            
            return;
        }
        
        MostIp most_ip = new MostIp(file);
        try {
            most_ip.run();
        } catch (Exception e) {
            System.err.println(e.toString());
            
            return;
        }
        
        most_ip.dumpTop(TOP_COUNT, System.out);
    }
    
    public MostIp(File file) {
        this.file = file;
    }
    
    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            process(reader);
        } finally {
            reader.close();
        }
    }
    
    // Read the file one line at a time, processing each line and preparing the ipcounts table
    private void process(BufferedReader reader) throws IOException {
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
            Integer seen = iptable.get(ip);
            iptable.put(ip, seen != null ? seen + 1 : 1);
        }
        
        // Now populate the TreeSet<IpCount>, which orders by seen count
        for (String ip : iptable.keySet())
            ipcounts.add(new IpCount(ip, iptable.get(ip)));
    }
    
    // Print the top 'n' IP addresses found in the file ... all ties within the top 'n' are printed
    private void dumpTop(int count, PrintStream pr) {
        int lastSeen = -1, pos = 0;
        for (IpCount ipcount : ipcounts) {
            // if the seen count is not the same as the current, treat as a new position within the
            // top elements ... this ensures ties are dumped
            if (lastSeen != ipcount.seen) {
                lastSeen = ipcount.seen;
                if (++pos > count)
                    return;
            }
            
            pr.printf("%d %s\n", ipcount.seen, ipcount.ip);
        }
    }
}

