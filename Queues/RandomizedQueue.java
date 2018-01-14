/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int first;
    private int last;
    private Item[] rq;
    private int n;
    public RandomizedQueue() {          // construct an empty randomized queue
        rq = (Item[]) new Object[1];
        last = -1;
        n = 0;
    }
    public boolean isEmpty() {          // is the randomized queue empty?
        return (n == 0);
    }
    public int size() {                 // return the number of items on the randomized queue
        return n;
    }
    public void enqueue(Item item) {    // add the item
        if (item == null) throw new IllegalArgumentException();
        if (n == rq.length) resize (2*rq.length);
        rq[++last] = item;
        n++;
    }
    public Item dequeue() {
        // remove and return a random item
        if (isEmpty()) throw new NoSuchElementException();
        int r = StdRandom.uniform(0, n);
        Item item = rq[r];
        rq[r] = rq[last--];
        if (--n < rq.length/4) resize (rq.length/2);
        return item;
    }
    public Item sample() {
        // return a random item (but do not remove it)
        if (isEmpty()) throw new NoSuchElementException();
        int r = StdRandom.uniform(0, n);
        return rq[r];
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int i = 0;
        while (i < n) {
            copy[i] = rq[i++];
        }
        rq = copy;
    }
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] perm = StdRandom.permutation(n);
        private int iCount = 0;
        public boolean hasNext() {return ((n > 0) && (iCount < n)); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return rq[perm[iCount++]];
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    public static void main(String[] args) {
        // unit testing (optional)
        int n = 5;
        if (args.length != 0) {n = Integer.parseInt(args[0]); }
        if (n <= 0) throw new IllegalArgumentException();
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++) {
            int value = StdIn.readInt();
            rq.enqueue(value);
        }
        
        Iterator<Integer> iter1 = rq.iterator();
        Iterator<Integer> iter2 = rq.iterator();
        
        while (iter1.hasNext()) {
            StdOut.println("I1: " + iter1.next() + " I2: " + iter2.next());
        }
//        int rqSize = rq.size();
//        StdOut.println("RQ Length: " + rqSize);
//        for (int i = 0; i < rqSize; i++) {
//            StdOut.println(rq.dequeue());
//        }
    }
}