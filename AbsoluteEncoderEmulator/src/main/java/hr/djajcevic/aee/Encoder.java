package hr.djajcevic.aee;

import java.io.Serializable;

/**
 * @author djajcevic | 17.05.2015.
 */
public interface Encoder extends Serializable {

    public static enum Direction implements Serializable {
        FORWARD, BACKWARD
    }

    int rotate(final Direction direction);

}
