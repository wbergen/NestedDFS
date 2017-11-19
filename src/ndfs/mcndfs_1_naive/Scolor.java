package ndfs.mcndfs_1_naive;

import java.util.HashMap;
import java.util.Map;

import graph.State;
import java.lang.NullPointerException;

/**
 * This class provides a color map for graph states.
 */
public class Scolor {

    private final Map<State, Boolean> map = new HashMap<State, Boolean>();
    private Mylock lock = new Mylock();

    /* Get if a state has a color*/
    public boolean hasColor(State state) {

        // The initial color is white, and is not explicitly represented.
        try{
            return (map.get(state) == true);
        }catch(NullPointerException e){
            return false;
        }

    }

    /* Synchronized wrapper for Hascolor*/
    public boolean synchasColor(State state) {

        // The initial color is white, and is not explicitly represented.
        synchronized(this){
            this.hasColor(state);
        }
        return false;
    }

    /*  Set or remove a color for a state */
    public void scolor(State state, boolean color) {
        if (color == false) {
            map.remove(state);
        } else {
            map.put(state, color);
        }
    }

    /* Lock unlock object functions    */
    public void lock(){
        lock.lock();
    }
    public void unlock(){
        lock.unlock();
    }
}