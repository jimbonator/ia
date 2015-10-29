/**
 * primes
 *
 * Displays all prime numbers less than or equal to a supplied value.
 *
 * Jim Nelson <jimbonator@gmail.com>
 */

import java.util.*;

public class Primes extends Object {
    public static void main(String[] args) {
        int max;
        try {
            max = Integer.decode(args[0]);
        } catch (Exception e) {
            System.out.println("primes [max]");
            
            return;
        }
        
        List<Integer> found = primes(max);
        if (found == null)
            return;
        
        for (int prime : found)
            System.out.println(prime);
    }
    
    private static List<Integer> primes(int max) {
        if (max <= 1)
            return null;
        
        // Allocate the sieve to hold every values from 0 to max and initialize every location with
        // its index value ... although this is a tad wasteful (locs 0 and 1 are skipped), it
        // simplifies indexing, as the index is always equal to the value stored there
        int[] sieve = new int[max + 1];
        for (int index = 0; index < sieve.length; index++)
            sieve[index] = index;
        
        // Create the sieve starting at index 2, storing discovered primes as encountered
        List<Integer> found = new ArrayList<Integer>();
        for (int candidate = 2; candidate < sieve.length; candidate++) {
            // If the location's value is -1, it was earlier marked as composite
            if (sieve[candidate] == -1)
                continue;
            
            // add to final list of primes
            found.add(candidate);
            
            // mark all multiples of the prime as composite
            for (int composite = candidate * 2; composite < sieve.length; composite += candidate)
                sieve[composite] = -1;
        }
        
        return found;
    }
}

