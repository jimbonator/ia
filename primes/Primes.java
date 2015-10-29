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
        if (args.length != 1)
            return;
        
        List<Integer> found = primes(Integer.decode(args[0]));
        if (found == null)
            return;
        
        for (int prime : found)
            System.out.printf("%d ", prime);
        
        System.out.println();
    }
    
    private static List<Integer> primes(int max) {
        if (max <= 1)
            return null;
        
        // Allocate the sieve to hold [2...max] values and initialize every location with its value
        // Note that the mapping of value -> array index requires subtracting by 2
        int[] sieve = new int[max - 1];
        for (int value = 2; value <= max; value++)
            sieve[value - 2] = value;
        
        // Create the sieve
        List<Integer> found = new ArrayList<Integer>();
        for (int candidate = 2; candidate <= max; candidate++) {
            // If the location is a positive int, it's prime; if marked with -1, it's composite
            if (sieve[candidate - 2] == -1)
                continue;
            
            // add to final list of primes
            found.add(candidate);
            
            // mark all multiples of this value as composite
            for (int composite = candidate * 2; composite <= max; composite += candidate)
                sieve[composite - 2] = -1;
        }
        
        return found;
    }
}

