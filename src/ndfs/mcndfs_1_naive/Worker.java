package ndfs.mcndfs_1_naive;

import java.io.File;
import java.io.FileNotFoundException;

import graph.Graph;
import graph.GraphFactory;
import graph.State;

/**
 * This is a straightforward implementation of Figure 1 of
 * <a href="http://www.cs.vu.nl/~tcs/cm/ndfs/laarman.pdf"> "the Laarman
 * paper"</a>.
 */
public class Worker extends Thread {


    /*
        private per thread memory
    */
    private final Graph graph;
    private final Colors colors = new Colors();
    private final Scolor pink = new Scolor();
    
    /*
        Shared non atomic memory
    */
    private static Scolor red = new Scolor();
    private static Count count = new Count();
    private static boolean result = false;
    private int tId;

    // Throwing an exception is a convenient way to cut off the search in case a
    // cycle is found.
    private static class CycleFoundException extends Exception {
    }

    /**
     * Constructs a Worker object using the specified Promela file.
     *
     * @param promelaFile
     *            the Promela file.
     * @throws FileNotFoundException
     *             is thrown in case the file could not be read.
     */
    public Worker(File promelaFile, int i) throws FileNotFoundException {

        this.graph = GraphFactory.createGraph(promelaFile);
<<<<<<< HEAD
=======
        this.tId = i;

>>>>>>> 7345037f1c949de54d5d74acec8b8ee65b7e6779
    }

    /*
        proc dfs_red ( s , i )
            s . p i n k [i] := t r u e
            f o r a l l t i n p o s t ri ( s ) do
            i f t . c o l o r [i]=c y a n
            report cycle & exit a l l
            i f ¬ t . p i n k [i] ∧ ¬ t . r e d
            dfs_red ( t , i )
            if s ∈A
            s . c o u n t := s . c o u n t − 1
            a w a i t s . c o u n t =0
            s . r e d := t r u e
            s . p i n k [i] := f a l s e

    */

    // private void dfsRed(State s) throws CycleFoundException {

    //     for (State t : graph.post(s)) {
    //         if (colors.hasColor(t, Color.CYAN)) {
    //             throw new CycleFoundException();
    //         } else if (colors.hasColor(t, Color.BLUE)) {
    //             colors.color(t, Color.RED);
    //             dfsRed(t);
    //         }
    //     }
    // }

    private void dfsRed(State s, int i) throws CycleFoundException {

        pink.Scolor(s, true);

        for (State t : graph.post(s)) {
            // if (colors[i].hash
            if (colors[i].hasColor(t, Color.CYAN)) {
                throw new CycleFoundException();
            } else if (pink[i].hasColor(t, false) && !colors.hasColor(t. Color.RED)) {
                // colors.color(t, Color.RED);
                dfsRed(t, i);
            }
        }

        if (s.isAccepting()) {
            s.count--;
            while (s.count != 0) {
                ;
            }
        }

        colors[i].color(s, Color.RED);
        pink[i].scolor(s, true);
    }

    // private void dfsBlue(State s) throws CycleFoundException {

    //     colors.color(s, Color.CYAN);
    //     for (State t : graph.post(s)) {
    //         if (colors.hasColor(t, Color.WHITE)) {
    //             dfsBlue(t);
    //         }
    //     }
    //     if (s.isAccepting()) {
    //         dfsRed(s);
    //         colors.color(s, Color.RED);
    //     } else {
    //         colors.color(s, Color.BLUE);
    //     }
    // }

    private void dfsBlue(State s, int i) throws CycleFoundException {

        // colors.color(s, Color.CYAN);
        colors[i].color(s, CYAN);

        for (State t : graph.post(s)) {
            if (colors.hasColor(t, Color.WHITE) && (!colors.hasColor(t, Color.RED))) {
                dfsBlue(t, i);
            }
        }
        if (s.isAccepting()) {
            s.count++; 
            dfsRed(s, i);
        }
            // colors.color(s, Color.RED);
        // } else {
        // colors.color(s, Color.BLUE);
        // s.colors[i] = BLUE;
        colors[i].colors(s, Color.BLUE);
        // }
    }

    // private void nndfs(State s) throws CycleFoundException {
    //     dfsBlue(s);
    // }

    private void nndfs(State s, int i) throws CycleFoundException {
        dfsBlue(s, i);
    }

    public void run() {
        try {
            nndfs(graph.getInitialState(), this.tId);
        } catch (CycleFoundException e) {
            result = true;
        }

        //     if(e instanceof CycleFoundException){
        //         result = true;   
        //     } else if (e instanceof InterruptedException) {
        //     }
        // }
    }

    public boolean getResult() {
        return result;
    }
}