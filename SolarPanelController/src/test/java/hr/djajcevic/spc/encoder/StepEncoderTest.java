package hr.djajcevic.spc.encoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author djajcevic | 24.06.2015.
 */
public class StepEncoderTest {

    SequentialEncoder encoder;
    static final int STEP_COUNT = 260;
    static final float STEP_IN_DEGREE = 5;

    @Before
    public void setUp() throws Exception {
        encoder = new SequentialEncoder(STEP_COUNT, STEP_IN_DEGREE, 5);
    }

    @Test
    public void movedForward() {
        int counter = 20;
        for (int i = 0; i < counter; i++) {
            encoder.rotate(ServoMotorStepEncoder.Direction.FORWARD);
        }
        Assert.assertEquals(counter, encoder.currentStep());
        Assert.assertEquals(counter * STEP_IN_DEGREE, encoder.currentDegrees(), 0);
    }

    @Test
    public void movedBackward() {
        encoder.setAtEnd();
        int counter = 20;
        for (int i = 0; i < counter; i++) {
            encoder.rotate(ServoMotorStepEncoder.Direction.BACKWARD);
        }
        int expectedStep = STEP_COUNT - counter;
        Assert.assertEquals(expectedStep, encoder.currentStep());
        Assert.assertEquals(expectedStep * STEP_IN_DEGREE, encoder.currentDegrees(), 0);
    }

    @Test(expected = ServoReachedEndException.class)
    public void reachEnd() {
        encoder.setAtStart();
        for (int i = 0; i < STEP_COUNT + 5; i++) {
            encoder.rotate(ServoMotorStepEncoder.Direction.FORWARD);
        }
    }

    @Test(expected = ServoReachedStartException.class)
    public void reachStart() {
        encoder.setAtEnd();
        for (int i = 0; i < STEP_COUNT + 5; i++) {
            encoder.rotate(ServoMotorStepEncoder.Direction.BACKWARD);
        }
    }
}
