/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String args[]) {
        int k = 1;
        if (args.length != 0) {k = Integer.parseInt(args[0]); }
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }
        Iterator<String> iter = rq.iterator();
        int i = 0;
        while ((iter.hasNext())  && (i++ < k)) {
            StdOut.println(iter.next());
        }
    }
}