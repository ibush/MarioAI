package cs221;

/**
 * Created by ibush on 10/25/15.
 */
public class StateActionPair {
    private int[] state;
    private boolean[] actions;

    public StateActionPair(int[] state, boolean[] actions) {
        this.state = state;
        this.actions = actions;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StateActionPair)) return false;
        StateActionPair other = (StateActionPair) object;
        return other.actions == this.actions && other.state == this.state;
    }

    public int[] getState() {
        return state;
    }

    public boolean[] getActions() {
        return actions;
    }
}