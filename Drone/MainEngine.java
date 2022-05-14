package Drone;

import java.util.HashMap;

/**
 * Main Engine class:
 * This is a representation of the main engine which
 * the `Bereshit` space craft carried to its moon landing mission.
 * @see Bereshit_101
 * @see SpaceShip
 * */
public class MainEngine {

    private final SpaceShip ship;
    private final HashMap<String, Engine> engines;

    public MainEngine(SpaceShip spaceShip) {
        this.ship = spaceShip;

        engines = new HashMap<>();
        engines.put("R1", new Engine(this, 1));
        engines.put("R2", new Engine(this, 0.75));
        engines.put("L1", new Engine(this, 1));
        engines.put("L2", new Engine(this, 0.75));
        engines.put("DR1", new Engine(this, -1));
        engines.put("DR2", new Engine(this, -0.75));
        engines.put("DL1", new Engine(this, -1));
        engines.put("DL2", new Engine(this, -0.75));
    }

    public SpaceShip getShip() {
        return ship;
    }
}
