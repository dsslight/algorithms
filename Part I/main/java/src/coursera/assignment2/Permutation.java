package coursera.assignment2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        final int count = Integer.parseInt(args[0]);
        final RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int maxCount = count * 5;
        while (!StdIn.isEmpty() && maxCount-- > 0) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < count; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}