/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double[] percThreshold;
    private int numTrials;
    private int numSites;
    public PercolationStats(int n, int trials){    
        percThreshold = new double[trials];
        numTrials = trials;
        // perform trials independent experiments on an n-by-n grid
        for (int i = 0; i< trials; i++) {
            Percolation p = new Percolation(n);
                        
            // Run Monte Carlo
            numSites = 0;
            while (!(p.percolates())) {
                int objIndex = StdRandom.uniform(1, n*n + 1);
                int col = ((objIndex - 1) % n) + 1;
                int row = (objIndex - col) / n + 1;
                if (!(p.isOpen(row, col))) {
                    p.open(row, col);
                    numSites += 1;
                }
            }
            percThreshold[i] = ((double)numSites)/(n*n);
        }
    }
    public double mean(){                          
// sample mean of percolation threshold
        return StdStats.mean(percThreshold);
    }
    public double stddev(){                        
// sample standard deviation of percolation threshold
        return StdStats.stddev(percThreshold);
    }
    public double confidenceLo(){                  
// low  endpoint of 95% confidence interval
        return (mean() - 1.96*stddev()/Math.sqrt(numTrials));
    }
    public double confidenceHi() {                  
// high endpoint of 95% confidence interval
        return (mean() + 1.96*stddev()/Math.sqrt(numTrials));
    }

    public static void main(String[] args)  {      
        // test client (described below)
        int n = 5;
        int trials = 10;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }
        PercolationStats p = new PercolationStats(n, trials);
        StdOut.println("mean =" + p.mean());
        StdOut.println("stddev =" + p.stddev());
        StdOut.println("95% confidence interval =[" + p.confidenceLo() + ", " + p.confidenceHi() +"]");
    }
}