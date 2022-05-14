package Drone;

public class Engine {

    double power_to_rotation; // Rotation 90
    double power = 1; // power per sec
    double main_engine_rotation;
    MainEngine mEngine;

    public Engine(MainEngine engine, double powerto) {
        this.power_to_rotation = powerto;
        mEngine = engine;
    }


    public void Fire() {
        if(mEngine.getShip().ang > 0) {
            mEngine.getShip().ang += power*power_to_rotation;
        }
    }
}
