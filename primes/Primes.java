/**
 * primes
 *
 * Displays all prime numbers less than or equal to a supplied value.
 *
 * I chose to implement a basic Sieve of Erostothanes rather than something more sophisticated
 * like the Sieve of Atkin) as the prompt didn't indicate if storage or speed were crucial for
 * this exercise.
 *
 * As far as using Java, I chose it because I'm comfortable with the language and didn't see any
 * major benefits PHP would offer for this task.  The method I wrote seems fairly clean; I didn't
 * find myself struggling against any of Java's peculiarities when writing it.
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
        
        for (int prime : primes(max))
            System.out.println(prime);
    }
    
    private static List<Integer> primes(int max) {
        if (max <= 1)
            return Collections.emptyList();
        
        // Allocate the sieve to hold every values from 0 to max (inclusive) with 'true' indicating
        // the value is composite ... although this is a tad wasteful (locs 0 and 1 are skipped), it
        // simplifies indexing, as the index is always equal to the value it represents
        boolean[] sieve = new boolean[max + 1];
        
        // Create the sieve starting at index 2, storing discovered primes as encountered
        List<Integer> found = new ArrayList<Integer>();
        for (int candidate = 2; candidate < sieve.length; candidate++) {
            // If the location's value is true, it was earlier marked as composite
            if (sieve[candidate])
                continue;
            
            // candidate is prime
            found.add(candidate);
            
            // mark all multiples of the prime as composite
            for (int composite = candidate * 2; composite < sieve.length; composite += candidate)
                sieve[composite] = true;
        }
        
        return found;
    }
}

