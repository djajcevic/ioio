package hr.djajcevic.spc.ioio.looper.process;

import ioio.lib.api.exception.ConnectionLostException;
import lombok.Data;

/**
 * @author djajcevic | 11.08.2015.
 */
@Data
public abstract class AbstractProcessManager {

    SystemManager managerRepository;

    public abstract void initialize() throws ConnectionLostException, InterruptedException;

    public abstract void performManagementActions() throws ConnectionLostException, InterruptedException;

}
