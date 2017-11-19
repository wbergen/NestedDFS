package ndfs.mcndfs_2_naive;

import java.util.HashMap;
import java.util.Map;
import graph.State;
import java.lang.NullPointerException;

/**
 * This class provides a color map for graph states.
 */
public class Count {

    private final Map<State, Integer> map = new HashMap<State, Integer>();
    private Mylock lock = new Mylock();
    
    /* Get a count value for a specific state*/
    public int GetCount(State state) {

        // The initial color is white, and is not explicitly represented.
        try{
            return map.get(state);
        }catch(NullPointerException e){
            return 0;
        }
        
    }
    
    /*
        Decrement and increment a count for a specific state
    */
    public void inc(State state) {

        try{
            map.put(state, (map.get(state) + 1));
        }catch(NullPointerException e){
            map.put(state, 1);
        }
    }
    public void dec(State state) {
        try{
            if (map.get(state) > 1) {
                map.put(state, (map.get(state) - 1));          
            } else {
                map.remove(state);
            }
        }catch(NullPointerException e){
            System.out.println("[Count object] Warning: decrementing a count already null!");
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