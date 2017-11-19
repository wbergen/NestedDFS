package ndfs.mcndfs_1_naive;

import java.util.HashMap;
import java.util.Map;

import graph.State;

/**
 * This class provides a color map for graph states.
 */
public class Count {

    private final Map<State, int> map = new HashMap<State, int>();

    /**
     * Returns <code>true</code> if the specified state has the specified color,
     * <code>false</code> otherwise.
     *
     * @param state
     *            the state to examine.
     * @param color
     *            the color
     * @return whether the specified state has the specified color.
     */
    public int GetCount(State state) {

        // The initial color is white, and is not explicitly represented.
        int return_value = map.get(state)
        if ( return_value == null) {
            return 0;
        } else {
            return return_value;
        }
    }

    /**
     * Gives the specified state the specified color.
     *
     * @param state
     *            the state to color.
     * @param color
     *            color to give to the state.
     */

    public void inc(State state) {
        if (map.get(state)) {
            map.put(state, (map.get(state) + 1));
        } else {
            map.put(state, 1);
        }
    }
    public void dec(State state) {
        if (map.get(state) > 1) {
            map.put(state, (map.get(state) - 1));
            
        } else {
            map.remove(state);
        }
    }
}