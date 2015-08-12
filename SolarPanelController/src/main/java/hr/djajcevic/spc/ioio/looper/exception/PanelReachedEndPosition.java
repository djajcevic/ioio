package hr.djajcevic.spc.ioio.looper.exception;

import hr.djajcevic.spc.ioio.looper.AxisController;

/**
 * @author djajcevic | 12.08.2015.
 */
public class PanelReachedEndPosition extends RuntimeException {
    private static final long serialVersionUID = 8729956502606318816L;

    public PanelReachedEndPosition(final AxisController.Axis axis) {
        super(axis.toString());
    }
}
