/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;


public class Deque<Item> implements Iterable<Item> { 
    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    private Node first;
    private Node last;
    private int N;
    public Deque() {                  // construct an empty deque
        first = null;
        last = null;
        N = 0;
    }
    public boolean isEmpty() {        // is the deque empty?
        return (first == null);
    }
    public int size() {               // return the number of items on the deque
        return N;
    }
    public void addFirst(Item item) {
        Node oldFirst = first;
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = oldFirst;
        newNode.prev = null;
        first = newNode;
        if (oldFirst == null) last = first;
        if (oldFirst != null) oldFirst.prev = first;
        N++;
    }
    public void addLast(Item item) {
        Node oldLast = last;
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = null;
        newNode.prev = oldLast;
        last = newNode;
        if (oldLast == null) first = last;
        if (oldLast != null) oldLast.next = last;
        N++;
    }
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (first != null) first.prev = null;
        if (first == null) last = null;
        N--;
        return item;
    }
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (last != null) last.next = null;
        if (last == null) first = null;
        N--;
        return item;
    }
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }
    private class DequeIterator implements Iterator<Item> {
        private Node start = first;
        public boolean hasNext() {return (start != null); }
        public Item next() {
            if (start == null) throw new java.util.NoSuchElementException();
            Item item = start.item;
            start = start.next;
            return item;
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
            dq.addFirst(value);
        }
        int dqSize = dq.size();
        StdOut.println("DQ Length: " + dqSize);
        for (int i = 0; i < dqSize; i++) {
            StdOut.println(dq.removeLast());
        }
    }
}