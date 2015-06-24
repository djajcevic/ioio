package hr.djajcevic.spc.encoder;

import org.apache.log4j.Logger;

/**
 * @author djajcevic | 24.06.2015.
 */
public class SequentialEncoder implements ServoMotorStepEncoder {

    private static final long serialVersionUID = -264426417807246458L;

    private static Logger LOGGER = Logger.getLogger(SequentialEncoder.class);

    private int stepCount;
    private float stepInDegrees;
    private int safeStepCount;

    private int currentStep;

    public SequentialEncoder(final int stepNumber, final float stepInDegrees, final int safeStepCount) {
        this.stepCount = stepNumber;
        this.stepInDegrees = stepInDegrees;
        this.safeStepCount = safeStepCount;
        currentStep = 0;
    }

    @Override
    public int rotate(final Direction direction) throws ServoReachedEndException {
        LOGGER.debug("[BEFORE] Direction: " + direction + ", currentStep: " + currentStep + ", stepCount: " + stepCount);
        switch (direction) {
            case FORWARD:
                currentStep++;
                if ((currentStep + safeStepCount) > stepCount) {
                    throw new ServoReachedEndException("Current step reached " + currentStep + " step!");
                }
                break;
            case BACKWARD:
                currentStep--;
                boolean a = (currentStep - safeStepCount) <= 0;
                boolean b = currentStep >= safeStepCount;
                if (a && b) {
                    throw new ServoReachedStartException("Current step reached " + currentStep + " step!");
                }
                break;
        }
        LOGGER.debug("[AFTER] Direction: " + direction + ", currentStep: " + currentStep + ", stepCount: " + stepCount);
        return currentStep;
    }

    @Override
    public int currentStep() {
        return currentStep;
    }

    @Override
    public float currentDegrees() {
        return stepInDegrees * currentStep;
    }

    @Override
    public void reset() {
        currentStep = 0;
    }

    @Override
    public void setAtEnd() {
        currentStep = stepCount;
    }

    @Override
    public void setAtStart() {
        currentStep = 0;
    }

}
