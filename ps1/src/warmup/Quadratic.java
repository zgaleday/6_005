package warmup;

import java.util.Set;
import java.util.HashSet;

public class Quadratic {

    /**
     * Find the integer roots of a quadratic equation, ax^2 + bx + c = 0.
     * @param a coefficient of x^2
     * @param b coefficient of x
     * @param c constant term.  Requires that a, b, and c are not ALL zero.
     * @return all integers x such that ax^2 + bx + c = 0.
     */
    public static Set<Integer> roots(int a, int b, int c) {
        Set<Integer> rootCollection = new HashSet<Integer>();
        boolean perfectSquare = false;
        long root = -1;                                // Inside of square root of quad formula
        double insideRoot = (b * b) - (4.0 * a * c);     // Calculates the (b^2 -4ac)^.5 portion of quadratic formula
        long denominator = 2 * a;                        // Calculates the denominator of the quad equation
        for (double i = 0; i <= insideRoot; i++) {
            if (i * i == insideRoot) { 
                root = (long) i;
                perfectSquare = true;
                break;
            }
        }
        if (perfectSquare) {
            long topPos = -b + root;                 // Num of quad equation with addition
            long topNeg = -b - root;                 // Num of quad equation with subtraction
            if (topPos / denominator ==  (double) topPos / denominator)
                rootCollection.add((int)(topPos / denominator));
            if (topNeg / denominator == (double) topNeg / denominator)  
                rootCollection.add((int)(topNeg / denominator));
        }
        return rootCollection;
    }

    
    /**
     * Main function of program.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int r1 =  45_000; // a root ~45,000 means c is ~2,000,000,000, which is close to the maximum integer 2^31 - 1
        int r2 = -45_000;
        Set<Integer> result = roots(1, -r1-r2, r1*r2);
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
