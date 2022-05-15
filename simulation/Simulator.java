package simulation;

import Drone.SpaceShip;

public class Simulator /*extends JFrame*/{

    public static void main(String[] args) {

        SpaceShip beresheet = new SpaceShip();

        // Start landing simulation
        beresheet.missionStart();
    }
}
