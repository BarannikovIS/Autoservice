/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netcracker.autoservice;

import java.util.Random;
import org.apache.log4j.Logger;

/**
 *
 * @author Иван
 */
public class Evacuator extends Thread {

    private static final Logger Log = Logger.getLogger(Evacuator.class);
    private Parking parking;

    public Evacuator(Parking parking) {
        this.parking = parking;
    }

    private Car carGeneration() {
        Random rnd = new Random();
        CarBrand id = CarBrand.values()[rnd.nextInt(3)];
        String name = "Name" + rnd.nextInt(1000);
        String owner = "Owner" + rnd.nextInt(1000);
        int handling_time = 6 - rnd.nextInt(5);
        OutputType type = OutputType.values()[rnd.nextInt(3)];

        return new Car(id, name, owner, handling_time, type);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                parking.addCar(carGeneration());
            } catch (InterruptedException ex) {
                Log.error(ex.getMessage(), ex);
            }
        }
    }
}
