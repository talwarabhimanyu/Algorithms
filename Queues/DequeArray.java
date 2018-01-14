/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/
/** This solution will NOT achieve constant worst case times, it will do    **/
/** constant amortized time. A Linked List implementation is prefered.      **/

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Deque<Item> implements Iterable<Item> {
    private int first;
    private int last;
    private Item[] dq;
    private int N = 0;
    
    public Deque() {
        // construct an empty deque
        dq = (Item[]) new Object[1];
        first = 0;
        last = 0;
    }
    public boolean isEmpty() {
        // is the deque empty?
        return (N == 0);
    }
    public int size() {                        
        // return the number of items on the deque
        return N;
    }
    public void addFirst(Item item) {
        // add the item to the front
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (N == dq.length) resize (2*dq.length);
        first = (dq.length + first - 1) % dq.length;
        dq[first] = item;
        N++;
    }
    public void addLast(Item item) {
        // add the item to the end
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (N == dq.length) resize (2*dq.length);
        last = (last + 1) % dq.length;
        dq[last] = item;
        N++;
    }
    public Item removeFirst() {
        // remove and return the item from the front
        if (isEmpty()) throw new java.util.NoSuchElementException();
        if (N < dq.length/4) resize (dq.length/2);
        Item item = dq[first];
        dq[first] = null;
        first = (first + 1) % dq.length;
        N--;
        return item;
    }
    public Item removeLast() {
        // remove and return the item from the end
        if (isEmpty()) throw new java.util.NoSuchElementException();
        if (N < dq.length/4) resize (dq.length/2);
        Item item = dq[last];
        dq[last] = null;
        last = (dq.length + last - 1) % dq.length;
        N--;
        return item;
    }
    
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int i = 0;
        while (i < N) {
            copy[i++] = dq[first];
            first = (first + 1) % dq.length;
        }
        first = 0;
        last = N - 1;
        dq = copy;
    }
   
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }
    private class DequeIterator implements Iterator<Item> {
        private int i = N;
        
        public boolean hasNext() {return (i > 0); }
        public Item next() { 
            return dq[first];
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    public static void main(String[] args){
        // unit testing (optional)
        int N = 5;
        if (args.length != 0) {N = Integer.parseInt(args[0]); }
        Deque<Integer> dq = new Deque<Integer>();
        for (int i = 0; i < N; i++) {
            int value = StdIn.readInt();
            dq.addLast(value);
        }
        int dqSize = dq.size();
        StdOut.println("DQ Length: " + dqSize);
        for (int i = 0; i < dqSize; i++) {
            StdOut.println(dq.removeLast());
        }
    }
}