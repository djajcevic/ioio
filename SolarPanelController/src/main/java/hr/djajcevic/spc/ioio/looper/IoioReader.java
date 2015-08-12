package hr.djajcevic.spc.ioio.looper;

import ioio.lib.api.exception.ConnectionLostException;

import java.io.IOException;

/**
 * @author djajcevic | 11.08.2015.
 */
public interface IOIOReader {
    void readData() throws ConnectionLostException, IOException, InterruptedException;

    void initialize() throws ConnectionLostException, InterruptedException;
}
