package Drone;

import simulation.Moon;
import simulation.PID;

import java.util.PrimitiveIterator;

/**
 * SpaceShip class:
 * Represents the Israeli `Bereshit` Spaceship, which was sent
 * on a space mission to land successfully on the moon.
 * @see simulation.Moon
 * */
public class SpaceShip {

    public static final double WEIGHT_EMP = 165; // kg
    public static final double WEIGHT_FUEL = 420; // kg
    public static final double WEIGHT_FULL = WEIGHT_EMP + WEIGHT_FUEL; // kg
    // https://davidson.weizmann.ac.il/online/askexpert/%D7%90%D7%99%D7%9A-%D7%9E%D7%98%D7%99%D7%A1%D7%99%D7%9D-%D7%97%D7%9C%D7%9C%D7%99%D7%AA-%D7%9C%D7%99%D7%A8%D7%97
    public static final double MAIN_ENG_F = 430; // N
    public static final double SECOND_ENG_F = 25; // N
    public static final double MAIN_BURN = 0.15; //liter per sec, 12 liter per m'
    public static final double SECOND_BURN = 0.009; //liter per sec 0.6 liter per m'
    public static final double ALL_BURN = MAIN_BURN + 8*SECOND_BURN;

    // starting point:
    double vs = 24.8;
    double hs = 932;
    double dist = 181*1000;
    double ang = 58.3; // zero is vertical (as in landing)
    double alt = 13748; // 2:25:40 (as in the simulation) // https://www.youtube.com/watch?v=JJ0VfRL9AMs
    double time = 0;
    double dt = 1; // sec
    double acc=0; // Acceleration rate (m/s^2)
    double fuel = 121;
    double weight = WEIGHT_EMP + fuel;
    double NN = 0.7; // rate[0,1]
    State state = State.Flying;

    enum State {
        Flying, PreparingToLand, Landing
    }
    private boolean start;

    public void missionStart() {
        if (!start) {
            start = true;
            mainLoop.start();
        }
    }

    private final Thread mainLoop;

    public SpaceShip() {
        this.state = State.PreparingToLand;
        mainLoop = new Thread(this::main_loop);
    }

    public static double accMax(double weight) {
        return acc(weight, true,8);
    }

    public static double acc(double weight, boolean main, int seconds) {
        double t = 0;
        if(main) {t += MAIN_ENG_F;}
        t += seconds*SECOND_ENG_F;
        return t/weight;
    }

    private PID pid;

    private void main_loop() {
        pid = new PID(0, 0, 1);
        while(alt>0) {
            if(alt>2000) {	// maintain a vertical speed of [20-25] m/s
            // over 2 km above the ground

                if(vs > 25) {NN+=0.3;} // more power for braking
                if(vs < 20) {NN-=0.3;} // less power for braking
            }
            else {
            // lower than 2 km - horizontal speed should be close to zero
                if(ang > 3) {ang-=3;} // rotate to vertical position.
                else {ang = 0;}
                NN=0.5; // brake slowly, a proper PID controller here is needed!
                if(hs < 2) {hs=0;}
                if(alt<125) { // very close to the ground!
                    NN=1; // maximum braking!
                    if(vs<5) {NN=0.7;} // if it is slow enough - go easy on the brakes
                }
            }
            if(alt<5) { // no need to stop
                NN=0.4;
            }
            update();
        }
    }


    private void update() {
        // main computations
        if (time % 100 == 0) {
            System.out.println("   time  |  vs   |   hs   |   dist   |   alt    |  ang  | weight |  NN  |  fuel  |  acc |");
            System.out.println("====================================================================================");
        }
        double ang_rad = Math.toRadians(ang);
        double h_acc = Math.sin(ang_rad)*acc;
        double v_acc = Math.cos(ang_rad)*acc;
        double vacc = Moon.getAcc(hs);
        time+=dt;
        double dw = dt*ALL_BURN*NN;
        if(fuel>0) {
            fuel -= dw;
            weight = WEIGHT_EMP + fuel;
            acc = NN* accMax(weight);
        }
        else { // ran out of fuel
            acc=0;
        }

        v_acc -= vacc;
        if(hs>0) {hs -= h_acc*dt;}
        dist -= hs*dt;
        vs -= v_acc*dt;
        alt -= dt*vs;


        if(time%10==0 || alt<100) {
            System.out.printf("| %.2f ", time);
            System.out.printf("| %.2f ", vs);
            System.out.printf("| %.2f ", hs);
            System.out.printf("| %.2f ", dist);
            System.out.printf("| %.2f ", alt);
            System.out.printf("| %.2f ", ang);
            System.out.printf("| %.2f ", weight);
            System.out.printf("| %.2f ", NN);
            System.out.printf("| %.2f ", fuel);
            System.out.printf("| %.2f |\n", acc);
        }
    }
}
