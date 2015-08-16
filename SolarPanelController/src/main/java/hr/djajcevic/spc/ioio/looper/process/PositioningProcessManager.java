package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.calculator.SunPositionData;
import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.exception.ConnectionLostException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

        Calendar calendar = GregorianCalendar.getInstance();
        TimeZone aDefault = TimeZone.getDefault();
        boolean b = aDefault.inDaylightTime(new Date());
        int correction = b ? 1 : 0;
        calendar.setTimeZone(aDefault);

        double min = 60.0 * (sunPositionData.sunrise - (int) (sunPositionData.sunrise));
        double sec = 60.0 * (min - (int) min);
        calendar.set(Calendar.HOUR_OF_DAY, (int) sunPositionData.sunrise + correction);
        calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);
        Calendar sunrise = Calendar.getInstance();
        sunrise.setTime(calendar.getTime());
        System.out.println("Sunrise: " + calendar.getTime());
        System.out.println("Sunrise: " + sunrise.getTime());

        min = 60.0 * (sunPositionData.sunset - (int) (sunPositionData.sunset));
        sec = 60.0 * (min - (int) min);
        calendar.set(Calendar.HOUR_OF_DAY, (int) sunPositionData.sunset + correction);
        calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);
        Calendar sunset = Calendar.getInstance();
        sunset.setTime(calendar.getTime());
        System.out.println("Sunset: " + calendar.getTime());
        System.out.println("Sunset: " + sunset.getTime());

        Calendar gpsTime = managerRepository.getGpsData().getTime();

        System.out.println("Current time: " + gpsTime.getTime());

        if (gpsTime.after(sunset)) {
            System.out.printf("passed sunset");
        } else if (gpsTime.before(sunrise)) {
            System.out.printf("before sunrise");
            return;
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
        int nextYPositionStepCount = nextYPositionAngle / yAxisStepDegree;

        System.out.println("Next Y position angle `" + nextYPositionAngle + "` for Sun altitude `" + altitude + "`");

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
