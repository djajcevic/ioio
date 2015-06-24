package hr.djajcevic.spc;

import hr.djajcevic.spc.encoder.ServoMotorStepEncoder;

/**
 * @author djajcevic | 24.06.2015.
 */
public class SolarPanelControllerImpl implements SolarPanelController {

    private SystemInformation systemInformation;

    private ServoMotorStepEncoder xServoStepEncoder;
    private ServoMotorStepEncoder yServoStepEncoder;

    private PositioningListener positioningListener;

    public SolarPanelControllerImpl(final ServoMotorStepEncoder xServoStepEncoder, final ServoMotorStepEncoder yServoStepEncoder) {
        this.xServoStepEncoder = xServoStepEncoder;
        this.yServoStepEncoder = yServoStepEncoder;
    }

    @Override
    public void checkSystem() {

    }

    @Override
    public void parkSystem() {

    }

    @Override
    public void doPosition() {

    }

    @Override
    public void updateSystemInformation(final SystemInformation systemInformation) {
        this.systemInformation = systemInformation;
    }

    public PositioningListener getPositioningListener() {
        return positioningListener;
    }

    public void setPositioningListener(final PositioningListener positioningListener) {
        this.positioningListener = positioningListener;
    }
}
