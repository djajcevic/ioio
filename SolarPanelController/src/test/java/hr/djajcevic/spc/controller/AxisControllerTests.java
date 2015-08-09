package hr.djajcevic.spc.controller;

import hr.djajcevic.spc.ioio.looper.AxisController;
import ioio.lib.api.*;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.api.exception.IncompatibilityException;
import org.junit.Test;

/**
 * @author djajcevic | 09.08.2015.
 */
public class AxisControllerTests {

    @Test
    public void initialization() throws ConnectionLostException {
        AxisController controller = new AxisController(new AxisController.Delegate() {

            @Override
            public void stepCompleted(final int currentStep) {
                System.out.println("step completed: " + currentStep);
            }

            @Override
            public void reachedStartPosition() {
                System.out.println("reachedStartPosition");
            }

            @Override
            public void reachedEndPosition() {
                System.out.println("reachedEndPosition");
            }

            @Override
            public boolean shouldStop(final boolean positiveDirection, int currentStep) {
                return currentStep >= 50 && positiveDirection;
            }
        }, new IOIO() {
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
//                        return pin == 5;
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
                return null;
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
                return null;
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

            }

            @Override
            public void endBatch() throws ConnectionLostException {

            }

            @Override
            public void sync() throws ConnectionLostException, InterruptedException {

            }
        }, AxisController.Axis.X);

        controller.initialize();
        try {
            controller.move(true, 30);
            controller.move(false);
            controller.move(true);
            controller.move(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
