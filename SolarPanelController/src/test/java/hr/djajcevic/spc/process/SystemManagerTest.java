package hr.djajcevic.spc.process;

import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import hr.djajcevic.spc.ioio.looper.process.PositioningProcessManager;
import hr.djajcevic.spc.ioio.looper.process.SystemManager;
import hr.djajcevic.spc.ioio.looper.process.SystemManagerListener;
import ioio.lib.api.*;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.api.exception.IncompatibilityException;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author djajcevic | 09.08.2015.
 */
public class SystemManagerTest {

    @Test
    public void loop() throws ConnectionLostException {
        IOIO ioio = new IOIO() {
            @Override
            public void waitForConnect() throws ConnectionLostException, IncompatibilityException {

            }

            @Override
            public void disconnect() {

            }

            @Override
            public void waitForDisconnect() throws InterruptedException {

            }

            @Override
            public State getState() {
                return null;
            }

            @Override
            public void softReset() throws ConnectionLostException {

            }

            @Override
            public void hardReset() throws ConnectionLostException {

            }

            @Override
            public String getImplVersion(final VersionType v) {
                return null;
            }

            @Override
            public DigitalInput openDigitalInput(final DigitalInput.Spec spec) throws ConnectionLostException {
                return null;
            }

            @Override
            public DigitalInput openDigitalInput(final int pin) throws ConnectionLostException {
                return new DigitalInput() {
                    @Override
                    public boolean read() throws InterruptedException, ConnectionLostException {
//                        return pin == 5 || pin == 10;
                        return false;
                    }

                    @Override
                    public void waitForValue(final boolean value) throws InterruptedException, ConnectionLostException {

                    }

                    @Override
                    public void close() {
                        System.out.println("closed " + this + " input");
                    }
                };
            }

            @Override
            public DigitalInput openDigitalInput(final int pin, final DigitalInput.Spec.Mode mode) throws ConnectionLostException {
                return null;
            }

            @Override
            public DigitalOutput openDigitalOutput(final DigitalOutput.Spec spec, final boolean startValue) throws ConnectionLostException {
                return null;
            }

            @Override
            public DigitalOutput openDigitalOutput(final int pin, final DigitalOutput.Spec.Mode mode, final boolean startValue) throws ConnectionLostException {
                return null;
            }

            @Override
            public DigitalOutput openDigitalOutput(final int pin, final boolean startValue) throws ConnectionLostException {
                return null;
            }

            @Override
            public DigitalOutput openDigitalOutput(final int pin) throws ConnectionLostException {
                return new DigitalOutput() {
                    @Override
                    public void write(final boolean val) throws ConnectionLostException {

                    }

                    @Override
                    public void close() {
                        System.out.println("closed " + this + " output");
                    }
                };
            }

            @Override
            public AnalogInput openAnalogInput(final int pin) throws ConnectionLostException {
                return null;
            }

            @Override
            public PwmOutput openPwmOutput(final DigitalOutput.Spec spec, final int freqHz) throws ConnectionLostException {
                return null;
            }

            @Override
            public PwmOutput openPwmOutput(final int pin, final int freqHz) throws ConnectionLostException {
                return new PwmOutput() {
                    @Override
                    public void setDutyCycle(final float dutyCycle) throws ConnectionLostException {

                    }

                    @Override
                    public void setPulseWidth(final int pulseWidthUs) throws ConnectionLostException {

                    }

                    @Override
                    public void setPulseWidth(final float pulseWidthUs) throws ConnectionLostException {

                    }

                    @Override
                    public void close() {
                        System.out.println("closed " + this + " output");
                    }
                };
            }

            @Override
            public PulseInput openPulseInput(final DigitalInput.Spec spec, final PulseInput.ClockRate rate, final PulseInput.PulseMode mode, final boolean doublePrecision) throws ConnectionLostException {
                return null;
            }

            @Override
            public PulseInput openPulseInput(final int pin, final PulseInput.PulseMode mode) throws ConnectionLostException {
                return null;
            }

            @Override
            public Uart openUart(final DigitalInput.Spec rx, final DigitalOutput.Spec tx, final int baud, final Uart.Parity parity, final Uart.StopBits stopbits) throws ConnectionLostException {
                return null;
            }

            @Override
            public Uart openUart(final int rx, final int tx, final int baud, final Uart.Parity parity, final Uart.StopBits stopbits) throws ConnectionLostException {
                return new Uart() {
                    @Override
                    public InputStream getInputStream() {
                        return new BufferedInputStream((SystemManager.class.getClassLoader().getResourceAsStream("gps.data.txt")));
                    }

                    @Override
                    public OutputStream getOutputStream() {
                        return null;
                    }

                    @Override
                    public void close() {

                    }
                };
            }

            @Override
            public SpiMaster openSpiMaster(final DigitalInput.Spec miso, final DigitalOutput.Spec mosi, final DigitalOutput.Spec clk, final DigitalOutput.Spec[] slaveSelect, final SpiMaster.Config config) throws ConnectionLostException {
                return null;
            }

            @Override
            public SpiMaster openSpiMaster(final int miso, final int mosi, final int clk, final int[] slaveSelect, final SpiMaster.Rate rate) throws ConnectionLostException {
                return null;
            }

            @Override
            public SpiMaster openSpiMaster(final int miso, final int mosi, final int clk, final int slaveSelect, final SpiMaster.Rate rate) throws ConnectionLostException {
                return null;
            }

            @Override
            public TwiMaster openTwiMaster(final int twiNum, final TwiMaster.Rate rate, final boolean smbus) throws ConnectionLostException {
                return new TwiMaster() {
                    @Override
                    public boolean writeRead(final int address, final boolean tenBitAddr, final byte[] writeData, final int writeSize, final byte[] readData, final int readSize) throws ConnectionLostException, InterruptedException {
                        return false;
                    }

                    @Override
                    public Result writeReadAsync(final int address, final boolean tenBitAddr, final byte[] writeData, final int writeSize, final byte[] readData, final int readSize) throws ConnectionLostException {
                        return null;
                    }

                    @Override
                    public void close() {

                    }
                };
            }

            @Override
            public IcspMaster openIcspMaster() throws ConnectionLostException {
                return null;
            }

            @Override
            public CapSense openCapSense(final int pin) throws ConnectionLostException {
                return null;
            }

            @Override
            public CapSense openCapSense(final int pin, final float filterCoef) throws ConnectionLostException {
                return null;
            }

            @Override
            public Sequencer openSequencer(final Sequencer.ChannelConfig[] config) throws ConnectionLostException {
                return null;
            }

            @Override
            public void beginBatch() throws ConnectionLostException {
                System.out.println("Starting batch");
            }

            @Override
            public void endBatch() throws ConnectionLostException {
                System.out.println("Ending batch");
            }

            @Override
            public void sync() throws ConnectionLostException, InterruptedException {

            }
        };

        SystemManager systemManager = new SystemManager();
        try {
            systemManager.getListeners().add(new SystemManagerListener() {
                @Override
                public void boardConnected(final IOIO ioio) {
                    System.out.println("boardConnected: " + ioio);
                }

                @Override
                public void boardDisconnected() {
                    System.out.println("boardDisconnected");
                }

                @Override
                public void incompatibleBoard(final IOIO ioio) {
                    System.out.println("incompatibleBoard: " + ioio);
                }

                @Override
                public void xAxisStepCompleted(final int currentStep) {
                    System.out.println("xAxisStepCompleted: " + currentStep);
                }

                @Override
                public void yAxisStepCompleted(final int currentStep) {
                    System.out.println("yAxisStepCompleted: " + currentStep);
                }

                @Override
                public void xAxisReachedStartPosition() {
                    System.out.println("xAxisReachedStartPosition");
                }

                @Override
                public void yAxisReachedStartPosition() {
                    System.out.println("yAxisReachedStartPosition");
                }

                @Override
                public void xAxisReachedEndPosition() {
                    System.out.println("xAxisReachedEndPosition");
                }

                @Override
                public void yAxisReachedEndPosition() {
                    System.out.println("yAxisReachedEndPosition");
                }

                @Override
                public void gpsPositionLocked(final GPSData data) {
                    System.out.println("GPS locked: " + data);
                }

                @Override
                public void compassDataReady(final CompassData data) {
                    System.out.println("Compass ready: " + data);
                }
            });
            systemManager.setup(ioio);
            systemManager.loop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void calculateNextPosition() {
        System.out.println(PositioningProcessManager.calculateNextXPositionAngle(0.0, 269.0));
        System.out.println(PositioningProcessManager.calculateNextYPositionAngle(50.0, 90.0));
    }
}
