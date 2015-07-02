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

    private int step = 0;
    private int maxStep = 50;

    private boolean atSomewhere = true, atEnd = false, atStart = false;

    @Override
    protected void setup() throws ConnectionLostException, InterruptedException {
        switch (axis) {
            case X:
                // create x digital outputs
                // check if at start or end
                break;
            case Y:
                // create y digital outputs
                // check if at start or end
                break;
        }
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
//        while (step == 0) {
//            Thread.sleep(20);
//        }
    }

    public void moveLeft() {
        if (step == 0) {
            atStart = true;
            atEnd = false;
            return;
        }
//        atStart = false;
        step--;
        System.out.println(axis + ":" + step);
    }

    public void moveRight() {
        if (step == maxStep) {
            atEnd = true;
            atStart = false;
            return;
        }
//        atEnd = false;
        step++;
        System.out.println(axis + ":" + step);
    }
}
