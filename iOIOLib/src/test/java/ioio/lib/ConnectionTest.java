package ioio.lib;


import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

/**
 * @author djajcevic | 03.05.2015.
 */
public class ConnectionTest {

    public class TestApp extends IOIOConsoleApp {

        private DigitalOutput statLed;

        @Override
        protected void run(final String[] args) throws Exception {
            statLed.write(true);
            Thread.sleep(1000);
            statLed.write(false);
        }

        @Override
        public IOIOLooper createIOIOLooper(final String connectionType, final Object extra) {
            return new BaseIOIOLooper() {
                @Override
                protected void setup() throws ConnectionLostException, InterruptedException {
                    super.setup();
                    statLed = ioio_.openDigitalOutput(IOIO.LED_PIN, true);
                }
            };
        }
    }

}
