package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.calculator.SunPositionData;
import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * @author djajcevic | 11.08.2015.
 */
public class PositioningProcessManager extends AbstractProcessManager {

    private AxisController xAxisController;
    private AxisController yAxisController;

    private SunPositionData sunPositionData;

    @Override
    public void initialize() throws ConnectionLostException {
        xAxisController = managerRepository.getXAxisController();
        yAxisController = managerRepository.getYAxisController();
        sunPositionData = managerRepository.getSunPositionData();
    }

    @Override
    public void performManagementActions() throws ConnectionLostException, InterruptedException {
        // 30 degrees left
        xAxisController.move(false);
        Configuration.saveCurrentXStep(xAxisController.getCurrentStep());
        // 30 degrees down
        yAxisController.move(false);
        Configuration.saveCurrentYStep(yAxisController.getCurrentStep());
    }
}
