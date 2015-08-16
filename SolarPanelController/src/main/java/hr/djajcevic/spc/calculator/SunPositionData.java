package hr.djajcevic.spc.calculator;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by djajcevic on 18.04.15..
 */
public class SunPositionData {

    public double jce;
    public double del_psi;
    public double epsilon;
    public double nu;
    public double latitude;
    public double longitude;
    public double elevation;
    public double pressure;
    public double temperature;
    public double atmos_refract;


    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    public double delta_ut1;
    public double timezone;
    public double h;
    public double xi;
    public double delta;
    public double del_alpha;
    public double delta_prime;
    public double alpha;
    public double r;
    public double alpha_prime;
    public double h_prime;
    public double jd;
    public double e0;
    public double del_e;
    public double e;
    public double zenith;
    public double azimuth_astro;
    public double azimuth;
    public double incidence;
    public double azm_rotation;
    public double slope;
    public double jc;
    public double jde;
    public double delta_t;
    public double jme;
    public double l;
    public double b;
    public double theta;
    public double beta;
    public double x0;
    public double x1;
    public double x2;
    public double x3;
    public double x4;
    public double del_epsilon;
    public double epsilon0;
    public double del_tau;
    public double lamda;
    public double nu0;
    public double eot;
    public double srha;
    public double ssha;
    public double sta;
    public double suntransit;
    public double sunrise;
    public double sunset;

    public SunPositionData() {
        setTime(Calendar.getInstance());
        this.delta_ut1 = 0;
        this.delta_t = 67;
    }


    public SunPositionData(SunPositionData other) {
        this.jce = other.jce;
        this.del_psi = other.del_psi;
        this.epsilon = other.epsilon;
        this.nu = other.nu;
        this.latitude = other.latitude;
        this.longitude = other.longitude;
        this.elevation = other.elevation;
        this.pressure = other.pressure;
        this.temperature = other.temperature;
        this.atmos_refract = other.atmos_refract;
        this.year = other.year;
        this.month = other.month;
        this.day = other.day;
        this.hour = other.hour;
        this.minute = other.minute;
        this.second = other.second;
        this.delta_ut1 = other.delta_ut1;
        this.timezone = other.timezone;
        this.h = other.h;
        this.xi = other.xi;
        this.delta = other.delta;
        this.del_alpha = other.del_alpha;
        this.delta_prime = other.delta_prime;
        this.alpha = other.alpha;
        this.r = other.r;
        this.alpha_prime = other.alpha_prime;
        this.h_prime = other.h_prime;
        this.jd = other.jd;
        this.e0 = other.e0;
        this.del_e = other.del_e;
        this.e = other.e;
        this.zenith = other.zenith;
        this.azimuth_astro = other.azimuth_astro;
        this.azimuth = other.azimuth;
        this.incidence = other.incidence;
        this.azm_rotation = other.azm_rotation;
        this.slope = other.slope;
        this.jc = other.jc;
        this.jde = other.jde;
        this.delta_t = other.delta_t;
        this.jme = other.jme;
        this.l = other.l;
        this.b = other.b;
        this.theta = other.theta;
        this.beta = other.beta;
        this.x0 = other.x0;
        this.x1 = other.x1;
        this.x2 = other.x2;
        this.x3 = other.x3;
        this.x4 = other.x4;
        this.del_epsilon = other.del_epsilon;
        this.epsilon0 = other.epsilon0;
        this.del_tau = other.del_tau;
        this.lamda = other.lamda;
        this.nu0 = other.nu0;
        this.eot = other.eot;
        this.srha = other.srha;
        this.ssha = other.ssha;
        this.sta = other.sta;
        this.suntransit = other.suntransit;
        this.sunrise = other.sunrise;
        this.sunset = other.sunset;
    }

    public void setTime(Calendar calendar) {
        TimeZone timeZone = calendar.getTimeZone();
        this.day = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        int dstSavings = timeZone.getDSTSavings() / 3600000;
        this.hour = calendar.get(Calendar.HOUR_OF_DAY) - dstSavings;
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);
        this.timezone = dstSavings;
    }

    @Override
    public String toString() {
        return "SunPositionData{" +
                "sunset=" + sunset +
                ", sunrise=" + sunrise +
                ", azimuth=" + azimuth +
                ", zenith=" + zenith +
                ", timezone=" + timezone +
                ", second=" + second +
                ", minute=" + minute +
                ", hour=" + hour +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", elevation=" + elevation +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
