package simulation;

public class PID {

    private final double P = 1.0;
    private final double I = 0.001;
    private final double D = 10.0;
    private final double setPoint;
    private double min, max;
    private double p_err, d_err, i_err, err_last = 0;
    public PID(double target, double min, double max) {
        setPoint = target;
        this.min = min;
        this.max = max;
    }

    public double compute(double input, double dt, boolean verbose) {
        p_err = input - setPoint;        // Proportional error
        d_err = (p_err - err_last); //*dt;   // Derivative error - error over time
        i_err += (p_err); // / dt);           // Integral error - total amount of time where error != 0;
        err_last = p_err;                // A reference to the last error;

        double output = -1 * ((P * p_err) + (I * i_err) + (D * d_err));
        if (verbose) {
            System.out.println("pid output: " + output);
        }
        return clamp(output);
    }


    private double clamp(double t) {

        if (t > max) {t = max;}
        if (t < min) {t = min;}
        return t;
    }
}
