/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netcracker.autoservice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author Иван
 */
public class Parking {
    private static final Logger Log= Logger.getLogger(Parking.class);
    private BlockingQueue<Car> cars;

    public Parking(int maxCars) {
        cars = new ArrayBlockingQueue(maxCars);
    }

    public void addCar(Car car) {
        try {
            cars.put(car);
        } catch (InterruptedException ex) {
            Log.error(ex.getMessage(), ex);
        }
    }

    public Car takeCar() {
        try {
            return cars.take();
        } catch (InterruptedException ex) {
            Log.error(ex.getMessage(), ex);
            return null;
        }
    }
}
