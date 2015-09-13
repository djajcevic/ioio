package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.calculator.SunPositionData;
import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.ioio.looper.exception.CurrentTimeAfterSunsetException;
import hr.djajcevic.spc.ioio.looper.exception.CurrentTimeBeforeSunriseException;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.exception.ConnectionLostException;

import java.util.Calendar;

/**
 * @author djajcevic | 11.08.2015.
 */
public class PositioningProcessManager extends AbstractProcessManager {

    public static final int MAX_NEGATIVE_STEP_COUNT = 0;
    private AxisController xAxisController;
    private AxisController yAxisController;

    private SunPositionData sunPositionData;

    private int xAxisStepDegree = 1;
    private int yAxisStepDegree = 1;

    @Override
    public void initialize() throws ConnectionLostException {
        xAxisController = managerRepository.getXAxisController();
        yAxisController = managerRepository.getYAxisController();
        sunPositionData = managerRepository.getSunPositionData();
        xAxisStepDegree = Configuration.getConfigInt(Configuration.SERVO_X_STEP_DEGREE);
        yAxisStepDegree = Configuration.getConfigInt(Configuration.SERVO_Y_STEP_DEGREE);
    }

    @Override
    public void performManagementActions() throws ConnectionLostException, InterruptedException {
        Double headingDegrees = managerRepository.getCompassData().getHeadingDegrees();
        Double azimuth = sunPositionData.azimuth;
        Double altitude = 90 - sunPositionData.zenith;

        SunPositionData sunPositionData = managerRepository.getSunPositionData();

        Calendar sunrise = sunPositionData.sunriseCalendar;
        System.out.println("Sunrise: " + sunrise.getTime());

        Calendar sunset = sunPositionData.sunsetCalendar;
        System.out.println("Sunset: " + sunset.getTime());

        Calendar gpsTime = managerRepository.getGpsData().getTime();

        System.out.println("Current time: " + gpsTime.getTime());

        if (gpsTime.after(sunset)) {
            System.out.printf("passed sunset");
            throw new CurrentTimeAfterSunsetException();
        } else if (gpsTime.before(sunrise)) {
            System.out.printf("before sunrise");
            throw new CurrentTimeBeforeSunriseException();
        }

        System.out.println("Real azimuth: " + azimuth);
        int nextXPositionAngle = calculateNextXPositionAngle(headingDegrees, azimuth);

        System.out.println("Next X position angle `" + nextXPositionAngle + "` for Sun azimuth `" + azimuth + "`");

        int nextXPositionStepCount = nextXPositionAngle / xAxisStepDegree;

        int maxSteps = xAxisController.getMaxSteps();
        int currentStep = xAxisController.getCurrentStep();
        int finalStepCount = nextXPositionStepCount;

        if (nextXPositionStepCount < 0 && (currentStep - Math.abs(nextXPositionStepCount)) <= MAX_NEGATIVE_STEP_COUNT) {
            finalStepCount = 360 - Math.abs(nextXPositionStepCount);
        } else if ((currentStep + nextXPositionStepCount) > maxSteps) {
            throw new RuntimeException("Invalid next step count (exceeds max step count): " + nextXPositionStepCount);
        }

        xAxisController.move(finalStepCount > 0, finalStepCount);
        Configuration.saveCurrentXStep(xAxisController.getCurrentStep());

        int nextYPositionAngle = calculateNextYPositionAngle((double) yAxisController.getCurrentStep(), altitude);
        System.out.println("Next Y position angle `" + nextYPositionAngle + "` for Sun altitude `" + altitude + "`");

        int nextYPositionStepCount = nextYPositionAngle / yAxisStepDegree;

        if (nextYPositionStepCount < currentStep && currentStep == 0) {
            System.out.println("Next Y position angle is unreachable. Skipping...");
            return;
        }
        yAxisController.move(nextYPositionStepCount > 0, nextYPositionStepCount);
    }

    public static int calculateNextXPositionAngle(final Double headingDegrees, final Double azimuth) {
        int x = (int)(azimuth - headingDegrees);
        int y = (int)(azimuth - 360 - headingDegrees);
        int xAbs = Math.abs(x);
        int yAbs = Math.abs(y);
        int minAbs = Math.min(xAbs, yAbs);
        return minAbs == xAbs ? x : y;
    }

    public static int calculateNextYPositionAngle(final Double currentPosition, final Double altitude) {
        int x = (int)(altitude - currentPosition);
        int y = (int)(altitude - 90 - currentPosition);
        int xAbs = Math.abs(x);
        int yAbs = Math.abs(y);
        int minAbs = Math.min(xAbs, yAbs);
        return minAbs == xAbs ? x : y;
    }



}
