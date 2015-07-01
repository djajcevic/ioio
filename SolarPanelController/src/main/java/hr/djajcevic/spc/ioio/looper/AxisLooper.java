package hr.djajcevic.spc.ioio.looper;

import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author djajcevic | 01.07.2015.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor()
public class AxisLooper extends BaseIOIOLooper {

    public enum Axis {
        X, Y
    }

    @NonNull
    private Axis axis;

    private int stepsToMoveLeft;

    @Override
    protected void setup() throws ConnectionLostException, InterruptedException {
        switch (axis) {
            case X:
                // create x digital outputs
                break;
            case Y:
                // create y digital outputs
                break;
        }
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
//        while (stepsToMoveLeft == 0) {
//            Thread.sleep(20);
//        }
    }

    public void moveLeft() {
        System.out.println(axis + ": moving by one step left");
    }

    public void moveRight() {
        System.out.println(axis + ": moving by one step right");
    }
}
