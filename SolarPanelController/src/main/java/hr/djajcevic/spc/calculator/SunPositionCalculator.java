package hr.djajcevic.spc.calculator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static hr.djajcevic.spc.calculator.HelperMethods.*;
import static java.lang.Math.*;

/**
 * Created by djajcevic on 18.04.15..
 */
public class SunPositionCalculator {

    static int TERM_D = 0;
    static int TERM_M = 1;
    static int TERM_MPR = 2;
    static int TERM_F = 3;
    static int TERM_LB = 4;
    static int TERM_R = 5;
    static int TERM_COUNT = 6;

    static int COUNT = 60;

    ///////////////////////////////////////////////////////
    ///  Moon's Periodic Terms for Longitude and Distance
    ///////////////////////////////////////////////////////
    static double[][] ML_TERMS =
            {
                    {0, 0, 1, 0, 6288774, -20905355},
                    {2, 0, -1, 0, 1274027, -3699111},
                    {2, 0, 0, 0, 658314, -2955968},
                    {0, 0, 2, 0, 213618, -569925},
                    {0, 1, 0, 0, -185116, 48888},
                    {0, 0, 0, 2, -114332, -3149},
                    {2, 0, -2, 0, 58793, 246158},
                    {2, -1, -1, 0, 57066, -152138},
                    {2, 0, 1, 0, 53322, -170733},
                    {2, -1, 0, 0, 45758, -204586},
                    {0, 1, -1, 0, -40923, -129620},
                    {1, 0, 0, 0, -34720, 108743},
                    {0, 1, 1, 0, -30383, 104755},
                    {2, 0, 0, -2, 15327, 10321},
                    {0, 0, 1, 2, -12528, 0},
                    {0, 0, 1, -2, 10980, 79661},
                    {4, 0, -1, 0, 10675, -34782},
                    {0, 0, 3, 0, 10034, -23210},
                    {4, 0, -2, 0, 8548, -21636},
                    {2, 1, -1, 0, -7888, 24208},
                    {2, 1, 0, 0, -6766, 30824},
                    {1, 0, -1, 0, -5163, -8379},
                    {1, 1, 0, 0, 4987, -16675},
                    {2, -1, 1, 0, 4036, -12831},
                    {2, 0, 2, 0, 3994, -10445},
                    {4, 0, 0, 0, 3861, -11650},
                    {2, 0, -3, 0, 3665, 14403},
                    {0, 1, -2, 0, -2689, -7003},
                    {2, 0, -1, 2, -2602, 0},
                    {2, -1, -2, 0, 2390, 10056},
                    {1, 0, 1, 0, -2348, 6322},
                    {2, -2, 0, 0, 2236, -9884},
                    {0, 1, 2, 0, -2120, 5751},
                    {0, 2, 0, 0, -2069, 0},
                    {2, -2, -1, 0, 2048, -4950},
                    {2, 0, 1, -2, -1773, 4130},
                    {2, 0, 0, 2, -1595, 0},
                    {4, -1, -1, 0, 1215, -3958},
                    {0, 0, 2, 2, -1110, 0},
                    {3, 0, -1, 0, -892, 3258},
                    {2, 1, 1, 0, -810, 2616},
                    {4, -1, -2, 0, 759, -1897},
                    {0, 2, -1, 0, -713, -2117},
                    {2, 2, -1, 0, -700, 2354},
                    {2, 1, -2, 0, 691, 0},
                    {2, -1, 0, -2, 596, 0},
                    {4, 0, 1, 0, 549, -1423},
                    {0, 0, 4, 0, 537, -1117},
                    {4, -1, 0, 0, 520, -1571},
                    {1, 0, -2, 0, -487, -1739},
                    {2, 1, 0, -2, -399, 0},
                    {0, 0, 2, -2, -381, -4421},
                    {1, 1, 1, 0, 351, 0},
                    {3, 0, -2, 0, -340, 0},
                    {4, 0, -3, 0, 330, 0},
                    {2, -1, 2, 0, 327, 0},
                    {0, 2, 1, 0, -323, 1165},
                    {1, 1, -1, 0, 299, 0},
                    {2, 0, 3, 0, 294, 0},
                    {2, 0, -1, -2, 0, 8752}
            };

    ///////////////////////////////////////////////////////
    ///  Moon's Periodic Terms for Latitude
    ///////////////////////////////////////////////////////
    static double[][] MB_TERMS =
            {
                    {0, 0, 0, 1, 5128122, 0},
                    {0, 0, 1, 1, 280602, 0},
                    {0, 0, 1, -1, 277693, 0},
                    {2, 0, 0, -1, 173237, 0},
                    {2, 0, -1, 1, 55413, 0},
                    {2, 0, -1, -1, 46271, 0},
                    {2, 0, 0, 1, 32573, 0},
                    {0, 0, 2, 1, 17198, 0},
                    {2, 0, 1, -1, 9266, 0},
                    {0, 0, 2, -1, 8822, 0},
                    {2, -1, 0, -1, 8216, 0},
                    {2, 0, -2, -1, 4324, 0},
                    {2, 0, 1, 1, 4200, 0},
                    {2, 1, 0, -1, -3359, 0},
                    {2, -1, -1, 1, 2463, 0},
                    {2, -1, 0, 1, 2211, 0},
                    {2, -1, -1, -1, 2065, 0},
                    {0, 1, -1, -1, -1870, 0},
                    {4, 0, -1, -1, 1828, 0},
                    {0, 1, 0, 1, -1794, 0},
                    {0, 0, 0, 3, -1749, 0},
                    {0, 1, -1, 1, -1565, 0},
                    {1, 0, 0, 1, -1491, 0},
                    {0, 1, 1, 1, -1475, 0},
                    {0, 1, 1, -1, -1410, 0},
                    {0, 1, 0, -1, -1344, 0},
                    {1, 0, 0, -1, -1335, 0},
                    {0, 0, 3, 1, 1107, 0},
                    {4, 0, 0, -1, 1021, 0},
                    {4, 0, -1, 1, 833, 0},
                    {0, 0, 1, -3, 777, 0},
                    {4, 0, -2, 1, 671, 0},
                    {2, 0, 0, -3, 607, 0},
                    {2, 0, 2, -1, 596, 0},
                    {2, -1, 1, -1, 491, 0},
                    {2, 0, -2, 1, -451, 0},
                    {0, 0, 3, -1, 439, 0},
                    {2, 0, 2, 1, 422, 0},
                    {2, 0, -3, -1, 421, 0},
                    {2, 1, -1, 1, -366, 0},
                    {2, 1, 0, 1, -351, 0},
                    {4, 0, 0, 1, 331, 0},
                    {2, -1, 1, 1, 315, 0},
                    {2, -2, 0, -1, 302, 0},
                    {0, 0, 1, 3, -283, 0},
                    {2, 1, 1, -1, -229, 0},
                    {1, 1, 0, -1, 223, 0},
                    {1, 1, 0, 1, 223, 0},
                    {0, 1, -2, -1, -220, 0},
                    {2, 1, -1, -1, -220, 0},
                    {1, 0, 1, 1, -185, 0},
                    {2, -1, -2, -1, 181, 0},
                    {0, 1, 2, 1, -177, 0},
                    {4, 0, -2, -1, 176, 0},
                    {4, -1, -1, -1, 166, 0},
                    {1, 0, 1, -1, -164, 0},
                    {4, 0, 1, -1, 132, 0},
                    {1, 0, -1, -1, -119, 0},
                    {4, -1, 0, -1, 115, 0},
                    {2, -2, 0, 1, 107, 0}
            };

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
// Calculate required SPA parameters to get the right ascension (alpha) and declination (delta)
// Note: JD must be already calculated and in structure
////////////////////////////////////////////////////////////////////////////////////////////////
    static void calculate_geocentric_sun_right_ascension_and_declination(SunPositionData spa) {
        double[] x = new double[TERM_X_COUNT];

        spa.jc = julian_century(spa.jd);

        spa.jde = julian_ephemeris_day(spa.jd, spa.delta_t);
        spa.jce = julian_ephemeris_century(spa.jde);
        spa.jme = julian_ephemeris_millennium(spa.jce);

        spa.l = earth_heliocentric_longitude(spa.jme);
        spa.b = earth_heliocentric_latitude(spa.jme);
        spa.r = earth_radius_vector(spa.jme);

        spa.theta = geocentric_longitude(spa.l);
        spa.beta = geocentric_latitude(spa.b);

        x[TERM_X0] = spa.x0 = mean_elongation_moon_sun(spa.jce);
        x[TERM_X1] = spa.x1 = mean_anomaly_sun(spa.jce);
        x[TERM_X2] = spa.x2 = mean_anomaly_moon(spa.jce);
        x[TERM_X3] = spa.x3 = argument_latitude_moon(spa.jce);
        x[TERM_X4] = spa.x4 = ascending_longitude_moon(spa.jce);

        nutation_longitude_and_obliquity(spa.jce, x, spa.del_psi, spa.del_epsilon);

        spa.epsilon0 = ecliptic_mean_obliquity(spa.jme);
        spa.epsilon = ecliptic_true_obliquity(spa.del_epsilon, spa.epsilon0);

        spa.del_tau = aberration_correction(spa.r);
        spa.lamda = apparent_sun_longitude(spa.theta, spa.del_psi, spa.del_tau);
        spa.nu0 = greenwich_mean_sidereal_time(spa.jd, spa.jc);
        spa.nu = greenwich_sidereal_time(spa.nu0, spa.del_psi, spa.epsilon);

        spa.alpha = geocentric_right_ascension(spa.lamda, spa.epsilon, spa.beta);
        spa.delta = geocentric_declination(spa.beta, spa.epsilon, spa.lamda);
    }

    static void calculate_eot_and_sun_rise_transit_set(SunPositionData spa) {
        SunPositionData sun_rts = new SunPositionData(spa);
        double nu, m, h0, n;
        double[] alpha = new double[JD_COUNT], delta = new double[JD_COUNT];
        double[] m_rts = new double[SUN_COUNT], nu_rts = new double[SUN_COUNT], h_rts = new double[SUN_COUNT];
        double[] alpha_prime = new double[SUN_COUNT], delta_prime = new double[SUN_COUNT], h_prime = new double[SUN_COUNT];
        double h0_prime = -1 * (SUN_RADIUS + spa.atmos_refract);
        int i;

        m = sun_mean_longitude(spa.jme);
        spa.eot = eot(m, spa.alpha, spa.del_psi, spa.epsilon);

        sun_rts.hour = sun_rts.minute = sun_rts.second = 0;
        sun_rts.delta_ut1 = sun_rts.timezone = 0.0;

        sun_rts.jd = julian_day(sun_rts.year, sun_rts.month, sun_rts.day, sun_rts.hour,
                sun_rts.minute, sun_rts.second, sun_rts.delta_ut1, sun_rts.timezone);

        calculate_geocentric_sun_right_ascension_and_declination(sun_rts);
        nu = sun_rts.nu;

        sun_rts.delta_t = 0;
        sun_rts.jd--;
        for (i = 0; i < JD_COUNT; i++) {
            calculate_geocentric_sun_right_ascension_and_declination(sun_rts);
            alpha[i] = sun_rts.alpha;
            delta[i] = sun_rts.delta;
            sun_rts.jd++;
        }

        m_rts[SUN_TRANSIT] = approx_sun_transit_time(alpha[JD_ZERO], spa.longitude, nu);
        h0 = sun_hour_angle_at_rise_set(spa.latitude, delta[JD_ZERO], h0_prime);

        if (h0 >= 0) {

            approx_sun_rise_and_set(m_rts, h0);

            for (i = 0; i < SUN_COUNT; i++) {

                nu_rts[i] = nu + 360.985647 * m_rts[i];

                n = m_rts[i] + spa.delta_t / 86400.0;
                alpha_prime[i] = rts_alpha_delta_prime(alpha, n);
                delta_prime[i] = rts_alpha_delta_prime(delta, n);

                h_prime[i] = limit_degrees180pm(nu_rts[i] + spa.longitude - alpha_prime[i]);

                h_rts[i] = rts_sun_altitude(spa.latitude, delta_prime[i], h_prime[i]);
            }

            spa.srha = h_prime[SUN_RISE];
            spa.ssha = h_prime[SUN_SET];
            spa.sta = h_rts[SUN_TRANSIT];

            spa.suntransit = dayfrac_to_local_hr(m_rts[SUN_TRANSIT] - h_prime[SUN_TRANSIT] / 360.0,
                    spa.timezone);

            spa.sunrise = dayfrac_to_local_hr(sun_rise_and_set(m_rts, h_rts, delta_prime,
                    spa.latitude, h_prime, h0_prime, SUN_RISE), spa.timezone);

            spa.sunset = dayfrac_to_local_hr(sun_rise_and_set(m_rts, h_rts, delta_prime,
                    spa.latitude, h_prime, h0_prime, SUN_SET), spa.timezone);

        } else spa.srha = spa.ssha = spa.sta = spa.suntransit = spa.sunrise = spa.sunset = -99999;

    }

    public static int calculateSunPosition(SunPositionData spa) {
        int result;

        spa.jd = julian_day(spa.year, spa.month, spa.day, spa.hour,
                spa.minute, spa.second, spa.delta_ut1, spa.timezone);

        calculate_geocentric_sun_right_ascension_and_declination(spa);

        spa.h = observer_hour_angle(spa.nu, spa.longitude, spa.alpha);
        spa.xi = sun_equatorial_horizontal_parallax(spa.r);

        right_ascension_parallax_and_topocentric_dec(spa.latitude, spa.elevation, spa.xi,
                spa.h, spa.delta, spa.del_alpha, spa.delta_prime);

        spa.alpha_prime = topocentric_right_ascension(spa.alpha, spa.del_alpha);
        spa.h_prime = topocentric_local_hour_angle(spa.h, spa.del_alpha);

        spa.e0 = topocentric_elevation_angle(spa.latitude, spa.delta_prime, spa.h_prime);
        spa.del_e = atmospheric_refraction_correction(spa.pressure, spa.temperature,
                spa.atmos_refract, spa.e0);
        spa.e = topocentric_elevation_angle_corrected(spa.e0, spa.del_e);

        spa.zenith = topocentric_zenith_angle(spa.e);
        spa.azimuth_astro = topocentric_azimuth_angle_astro(spa.h_prime, spa.latitude,
                spa.delta_prime);
        spa.azimuth = topocentric_azimuth_angle(spa.azimuth_astro);

        spa.incidence = surface_incidence_angle(spa.zenith, spa.azimuth_astro,
                spa.azm_rotation, spa.slope);

        calculate_eot_and_sun_rise_transit_set(spa);

        return 0;
    }

    public double limit_degrees(double degrees) {
        double limited;

        degrees /= 360.0;
        limited = 360.0 * (degrees - Math.floor(degrees));
        if (limited < 0) limited += 360.0;

        return limited;
    }

    double rad2deg(double radians) {
        return (180.0 / PI) * radians;
    }

    double deg2rad(double degrees) {
        return (PI / 180.0) * degrees;
    }

    public double third_order_polynomial(double a, double b, double c, double d, double x) {
        return ((a * x + b) * x + c) * x + d;
    }

    public double fourth_order_polynomial(double a, double b, double c, double d, double e, double x) {
        return (((a * x + b) * x + c) * x + d) * x + e;
    }

    public double moon_mean_longitude(double jce) {
        return limit_degrees(fourth_order_polynomial(
                -1.0 / 65194000, 1.0 / 538841, -0.0015786, 481267.88123421, 218.3164477, jce));
    }

    public double moon_mean_elongation(double jce) {
        return limit_degrees(fourth_order_polynomial(
                -1.0 / 113065000, 1.0 / 545868, -0.0018819, 445267.1114034, 297.8501921, jce));
    }

    public double sun_mean_anomaly(double jce) {
        return limit_degrees(third_order_polynomial(
                1.0 / 24490000, -0.0001536, 35999.0502909, 357.5291092, jce));
    }

    public double moon_mean_anomaly(double jce) {
        return limit_degrees(fourth_order_polynomial(
                -1.0 / 14712000, 1.0 / 69699, 0.0087414, 477198.8675055, 134.9633964, jce));
    }

    public double moon_latitude_argument(double jce) {
        return limit_degrees(fourth_order_polynomial(
                1.0 / 863310000, -1.0 / 3526000, -0.0036539, 483202.0175233, 93.2720950, jce));
    }

    public void moon_periodic_term_summation(double d, double m, double m_prime, double f, double jce,
                                             double[][] terms, double sin_sum, double cos_sum) {
        int i;
        double e_mult, trig_arg;
        double e = 1.0 - jce * (0.002516 + jce * 0.0000074);

        sin_sum = 0;
        if (cos_sum != 0) cos_sum = 0;
        for (i = 0; i < COUNT; i++) {
            e_mult = pow(e, abs(terms[i][TERM_M]));
            trig_arg = deg2rad(terms[i][TERM_D] * d + terms[i][TERM_M] * m +
                    terms[i][TERM_F] * f + terms[i][TERM_MPR] * m_prime);
            sin_sum += e_mult * terms[i][TERM_LB] * sin(trig_arg);
            if (cos_sum != 0) {
                cos_sum += e_mult * terms[i][TERM_R] * cos(trig_arg);
            }
        }
    }

    public void moon_longitude_and_latitude(double jce, double l_prime, double f, double m_prime, double l, double b,
                                            double lamda_prime, double beta) {
        double a1 = 119.75 + 131.849 * jce;
        double a2 = 53.09 + 479264.290 * jce;
        double a3 = 313.45 + 481266.484 * jce;
        double delta_l = 3958 * sin(deg2rad(a1)) + 318 * sin(deg2rad(a2)) + 1962 * sin(deg2rad(l_prime - f));
        double delta_b = -2235 * sin(deg2rad(l_prime)) + 175 * sin(deg2rad(a1 - f)) + 127 * sin(deg2rad(l_prime - m_prime))
                + 382 * sin(deg2rad(a3)) + 175 * sin(deg2rad(a1 + f)) - 115 * sin(deg2rad(l_prime + m_prime));

        lamda_prime = limit_degrees(l_prime + (l + delta_l) / 1000000);
        beta = limit_degrees((b + delta_b) / 1000000);
    }

    public double moon_earth_distance(double r) {
        return 385000.56 + r / 1000;
    }

    public double moon_equatorial_horiz_parallax(double delta) {
        return rad2deg(asin(6378.14 / delta));
    }

    public double apparent_moon_longitude(double lamda_prime, double del_psi) {
        return lamda_prime + del_psi;
    }

    public double angular_distance_sun_moon(double zen_sun, double azm_sun, double zen_moon, double azm_moon) {
        double zs = deg2rad(zen_sun);
        double zm = deg2rad(zen_moon);

        return rad2deg(acos(cos(zs) * cos(zm) + sin(zs) * sin(zm) * cos(deg2rad(azm_sun - azm_moon))));
    }

    ///////////

    public double sun_disk_radius(double r) {
        return 959.63 / (3600.0 * r);
    }

////////////////////////////////////////////////////////////////////////
// Calculate Equation of Time (EOT) and Sun Rise, Transit, & Set (RTS)
////////////////////////////////////////////////////////////////////////

    public double moon_disk_radius(double e, double pi, double cap_delta) {
        return 358473400 * (1 + sin(deg2rad(e)) * sin(deg2rad(pi))) / (3600.0 * cap_delta);
    }

    //////////

    public void sul_area(double ems, double rs, double rm, double a_sul, double a_sul_pct) {
        double ems2 = ems * ems;
        double rs2 = rs * rs;
        double rm2 = rm * rm;
        double snum, ai, m, s, h;

        if (ems < (rs + rm)) {
            if (ems <= abs(rs - rm))
                ai = PI * rm2;
            else {
                snum = ems2 + rs2 - rm2;
                m = (ems2 - rs2 + rm2) / (2 * ems);
                s = snum / (2 * ems);
                h = sqrt(4 * ems2 * rs2 - snum * snum) / (2 * ems);
                ai = (rs2 * acos(s / rs) - h * s + rm2 * acos(m / rm) - h * m);
            }
        } else ai = 0;

        a_sul = PI * rs2 - ai;
        if (a_sul < 0) {
            a_sul = 0;
        }
        a_sul_pct = a_sul * 100.0 / (PI * rs2);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
// Calculate all MPA parameters and put into structure
// Note: All inputs values (listed in SPA header file) must already be in structure
///////////////////////////////////////////////////////////////////////////////////////////
    public void mpa_calculate(SunPositionData spa, MoonPositionData mpa) {
        mpa.l_prime = moon_mean_longitude(spa.jce);
        mpa.d = moon_mean_elongation(spa.jce);
        mpa.m = sun_mean_anomaly(spa.jce);
        mpa.m_prime = moon_mean_anomaly(spa.jce);
        mpa.f = moon_latitude_argument(spa.jce);

        moon_periodic_term_summation(mpa.d, mpa.m, mpa.m_prime, mpa.f, spa.jce, ML_TERMS, mpa.l, mpa.r);
        moon_periodic_term_summation(mpa.d, mpa.m, mpa.m_prime, mpa.f, spa.jce, MB_TERMS, mpa.b, 0);

        moon_longitude_and_latitude(spa.jce, mpa.l_prime, mpa.f, mpa.m_prime, mpa.l, mpa.b,
                mpa.lamda_prime, mpa.beta);

        mpa.cap_delta = moon_earth_distance(mpa.r);
        mpa.pi = moon_equatorial_horiz_parallax(mpa.cap_delta);

        mpa.lamda = apparent_moon_longitude(mpa.lamda_prime, spa.del_psi);

        mpa.alpha = geocentric_right_ascension(mpa.lamda, spa.epsilon, mpa.beta);
        mpa.delta = geocentric_declination(mpa.beta, spa.epsilon, mpa.lamda);

        mpa.h = observer_hour_angle(spa.nu, spa.longitude, mpa.alpha);

        right_ascension_parallax_and_topocentric_dec(spa.latitude, spa.elevation, mpa.pi,
                mpa.h, mpa.delta, mpa.del_alpha, mpa.delta_prime);
        mpa.alpha_prime = topocentric_right_ascension(mpa.alpha, mpa.del_alpha);
        mpa.h_prime = topocentric_local_hour_angle(mpa.h, mpa.del_alpha);

        mpa.e0 = topocentric_elevation_angle(spa.latitude, mpa.delta_prime, mpa.h_prime);
        mpa.del_e = atmospheric_refraction_correction(spa.pressure, spa.temperature,
                spa.atmos_refract, mpa.e0);
        mpa.e = topocentric_elevation_angle_corrected(mpa.e0, mpa.del_e);

        mpa.zenith = topocentric_zenith_angle(mpa.e);
        mpa.azimuth_astro = topocentric_azimuth_angle_astro(mpa.h_prime, spa.latitude, mpa.delta_prime);
        mpa.azimuth = topocentric_azimuth_angle(mpa.azimuth_astro);
    }


    public static void main(String[] args) {
        SunPositionData spa = new SunPositionData();

        spa.delta_ut1 = 0;
        spa.delta_t = 67;
//        spa.longitude = -105.1786;
//        spa.latitude = 39.742476;
        spa.latitude = 45.837;
        spa.longitude = 16.0389;
        spa.elevation = 150;
        spa.pressure = 820;

        calculateSunPosition(spa);

        Calendar calendar = GregorianCalendar.getInstance();
        TimeZone aDefault = TimeZone.getDefault();
        boolean b = aDefault.inDaylightTime(new Date());
        int correction = b ? 1 : 0;
        calendar.setTimeZone(aDefault);

        double min = 60.0 * (spa.sunrise - (int) (spa.sunrise));
        double sec = 60.0 * (min - (int) min);
        calendar.set(Calendar.HOUR_OF_DAY, (int) spa.sunrise + correction);
        calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);
        System.out.println("Sunrise: " + calendar.getTime());

        min = 60.0 * (spa.sunset - (int) (spa.sunset));
        sec = 60.0 * (min - (int) min);
        calendar.set(Calendar.HOUR_OF_DAY, (int) spa.sunset + correction);
        calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);
        System.out.println("Sunset: " + calendar.getTime());

        System.out.println(spa);

        System.out.println(topocentric_azimuth_angle(0.005866799229077125));
        System.out.println(topocentric_azimuth_angle(-1.2922775524868455));
    }

}
