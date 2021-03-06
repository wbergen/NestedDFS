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
    private final Colors colors;
    private final Scolor pink;
    private int tId;    
    /*
        Shared non atomic memory
    */
    private static Scolor red = new Scolor();
    private static Count count = new Count();
    private static boolean result = false;


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
        this.colors = new Colors();
        this.pink = new Scolor();
        this.tId = i;
    }
    /*private List<T> post(graph.State s){
        return graph.post(s);
    }*/
    private void dfsRed(graph.State s, int i) throws CycleFoundException {

        pink.scolor(s,true);
    
        for (graph.State t : graph.post(s)) {
            red.lock();
                if (colors.hasColor(t, Color.CYAN)) {
                    red.unlock();
                    throw new CycleFoundException();
                } else if (pink.hasColor(t) && !red.hasColor(t)) {
                    red.unlock();
                    dfsRed(t, i);
                }else{
                    red.unlock();
                }
        }

        if(s.isAccepting()) {
            count.lock();
            count.dec(s);
            count.unlock();

            while(count.GetCount(s) != 0){ ; }
        }
        red.lock();
        red.scolor(s, true);
        red.unlock();

        pink.scolor(s, false);
    }

    private void dfsBlue(graph.State s, int i) throws CycleFoundException {

        colors.color(s, Color.CYAN);

        for (graph.State t : graph.post(s)) {
            red.lock();
            if (colors.hasColor(t, Color.WHITE) && !red.hasColor(t)) {
                red.unlock();
                dfsBlue(t, i);
            }else{
                red.unlock();
            }
        }
        if (s.isAccepting()) {
            count.lock();
            count.inc(s);
            count.unlock();

            dfsRed(s, i);
        } 
        colors.color(s, Color.BLUE);
    }

    private void nndfs(graph.State s, int i) throws CycleFoundException {
        dfsBlue(s, i);
    }

    public void run() {
        System.out.println("I'm thread" + this.tId);
        try {
            nndfs(graph.getInitialState(), this.tId);
        } catch (CycleFoundException e) {
            System.out.println("I'm thread" + this.tId + " I found a cycle");
            result = true;
        }
    }

    public boolean getResult() {
        return result;
    }
}