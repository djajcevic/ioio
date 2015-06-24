package hr.djajcevic.spc.encoder;

import java.io.Serializable;

/**
 * @author djajcevic | 17.05.2015.
 */
public interface ServoMotorStepEncoder extends Serializable {

    public static enum Direction implements Serializable {
        FORWARD, BACKWARD
    }

    /**
     * Rotate in specified <strong>direction</strong>.
     *
     * @param direction rotation direction
     * @return current step
     * @throws ServoReachedStartException
     * @throws ServoReachedEndException
     */
    int rotate(final Direction direction) throws ServoReachedStartException, ServoReachedEndException;

    /**
     * @return current step count
     */
    int currentStep();

    /**
     * @return current step in degrees
     */
    float currentDegrees();

    /**
     * Resets all counters. Position is START.
     */
    void reset();

    /**
     * Set encoder to show far end (currentStep = stepCount)
     */
    void setAtEnd();

    /**
     * Set encoder to show start (currentStep = 0)
     */
    void setAtStart();

}
