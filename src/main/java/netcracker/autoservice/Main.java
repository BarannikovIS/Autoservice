/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netcracker.autoservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Иван
 */
public class Main {
    private static final Logger Log= Logger.getLogger(Main.class);
    
    public static void main(String[] args) {
        Properties property = new Properties();
        try (FileInputStream in = new FileInputStream("Property.properties");) {
            property.load(in);
        } catch (IOException ex) {
            Log.error(ex.getMessage(), ex);
        }
        int maxCars = Integer.parseInt(property.getProperty("N"));
        int countMasters = Integer.parseInt(property.getProperty("X"));

        Parking parking = new Parking(maxCars);
        Evacuator evacuator = new Evacuator(parking);
        evacuator.start();
        for (int i = 0; i < countMasters; i++) {
            (new Thread(new Master(parking))).start();
        }
    }

}
