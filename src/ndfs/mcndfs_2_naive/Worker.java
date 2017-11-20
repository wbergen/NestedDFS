package ndfs.mcndfs_2_naive;

import java.io.File;
import java.io.FileNotFoundException;

import graph.Graph;
import graph.GraphFactory;
import graph.State;

import java.util.ArrayList;
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
    private int tId;
    private boolean allred;
    private int nW;
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
    public Worker(File promelaFile, int i, int nW) throws FileNotFoundException {

        this.graph = GraphFactory.createGraph(promelaFile);
        this.colors = new Colors();
        this.tId = i;
        this.allred = true;
        this.nW = nW;
    }

    // partition the successor nodes among the threads 
    private ArrayList<graph.State> post(graph.State s){
        
        ArrayList<graph.State>  list = new ArrayList<graph.State>(graph.post(s));
        int size = list.size();

        if (size == 1 || size == 0){
            return list;
        }

        float ratio = list.size()/(this.nW);
        int dratio;
        int uratio;
        int startIdx;
        int endIdx;

        if(ratio <= 1){
            ratio = 1;
        }

        dratio = (int)Math.floor(ratio);
        uratio = (int)Math.ceil(ratio);

        //System.out.println("ratio: " + ratio + " dratio: " + dratio + " uratio: " + uratio + " id: " + this.tId);

        startIdx = dratio * this.tId;
        endIdx = startIdx + uratio;

        //System.out.println("start: " + startIdx + " end: " + endIdx + " size: " + size);
        return list = new ArrayList<graph.State>(list.subList(startIdx, endIdx));
    }

    private void dfsRed(graph.State s, int i) throws CycleFoundException {

        colors.color(s, Color.PINK);

        for (graph.State t : post(s)) {
            red.lock();
                if (colors.hasColor(t, Color.CYAN)) {
                    red.unlock();
                    throw new CycleFoundException();
                } else if (!colors.hasColor(t, Color.PINK) && !red.hasColor(t)) {
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

    }

    private void dfsBlue(graph.State s, int i) throws CycleFoundException {
        allred = true;

        colors.color(s, Color.CYAN);
        //System.out.println(graph.post(s).getClass().getName());

        for (graph.State t : post(s)) {
            if(colors.hasColor(t, Color.CYAN) && (t.isAccepting() || s.isAccepting())){
                throw new CycleFoundException();
            }
            red.lock();
            if (colors.hasColor(t, Color.WHITE) && !red.hasColor(t)) {
                red.unlock();
                dfsBlue(t, i);
            }else{
                red.unlock();
            }
            red.lock();
            if(!red.hasColor(t)){
                allred = false;
            }
            red.unlock();
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