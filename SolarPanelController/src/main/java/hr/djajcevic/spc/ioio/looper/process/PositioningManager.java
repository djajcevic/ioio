package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.ioio.looper.AxisController;
import ioio.lib.api.exception.ConnectionLostException;
import lombok.Data;

/**
 * @author djajcevic | 11.08.2015.
 */
@Data
public class PositioningManager extends AbstractManager {

    private AxisController xAxisController;
    private AxisController yAxisController;

    @Override
    public void initialize() throws ConnectionLostException {
        xAxisController = managerRepository.getXAxisController();
        yAxisController = managerRepository.getYAxisController();
    }

    @Override
    public void performManagementActions() throws ConnectionLostException, InterruptedException {
        // 30 degrees left
        xAxisController.move(false, 30);
        // 30 degrees down
        yAxisController.move(false, 30);
        // 30 degrees right
        xAxisController.move(true, 30);
        // 30 degrees up
        yAxisController.move(true, 30);
    }
}
