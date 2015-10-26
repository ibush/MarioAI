package cs221;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by ibush on 10/25/15.
 */
public class StateActionPair {
    private int[] state;
    private boolean[] action;

    public StateActionPair(int[] state, boolean[] action) {
        this.state = state;
        this.action = action;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StateActionPair)) return false;
        if (object == this) return true;
        StateActionPair other = (StateActionPair) object;
        return new EqualsBuilder()
                    .append(state, other.state)
                    .append(action, other.action)
                    .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(state)
                .append(action)
                .toHashCode();
    }

    public int[] getState() {
        return state;
    }

    public boolean[] getAction() {
        return action;
    }
}