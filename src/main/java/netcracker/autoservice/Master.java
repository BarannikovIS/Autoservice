/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netcracker.autoservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Иван
 */
public class Master extends Thread {

    private static final Logger Log = Logger.getLogger(Master.class);
    private Car faceliftedCar;
    private Parking parking;

    public Master(Parking parking) {
        this.parking = parking;
    }

    public void writeDataAboutCar() {
        if (faceliftedCar.getType() == OutputType.Console) {
            writeTheConsole();
        }
        if (faceliftedCar.getType() == OutputType.XML) {
            writeInXML();
        }
        if (faceliftedCar.getType() == OutputType.DB) {
            writeInDB();
        }
    }

    private void writeTheConsole() {
        System.out.println("id: " + faceliftedCar.getId() + " " + "name: " + faceliftedCar.getName() + " "
                + "owner: " + faceliftedCar.getOwner() + " " + "handling_time: " + faceliftedCar.getHandlingTime());
    }

    private void writeInXML() {
        Element root = new Element(faceliftedCar.getId().name());
        Document doc = new Document(root);
        root.addContent(new Element("Name").addContent(faceliftedCar.getName()));
        root.addContent(new Element("Owner").addContent(faceliftedCar.getOwner()));
        root.addContent(new Element("Handling_time").addContent(Integer.toString(faceliftedCar.getHandlingTime())));
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        try {
            outputter.output(doc, new FileOutputStream("forXml/" + faceliftedCar.getName() + ".xml"));
        } catch (IOException io) {
            Log.error(io.getMessage(), io);
        }
    }

    private void writeInDB() {
        Properties property = new Properties();
        try (FileInputStream in = new FileInputStream("db.properties");) {
            property.load(in);
        } catch (IOException ex) {
            Log.error(ex.getMessage(), ex);
        }
        String drivers = property.getProperty("mysql.drivers");
        try {
            Class.forName(drivers);
        } catch (ClassNotFoundException ex) {
            Log.error(ex.getMessage(), ex);
        }
        String url = property.getProperty("mysql.url");
        String name = property.getProperty("mysql.username");
        String password = property.getProperty("mysql.password");
        try (Connection conn = DriverManager.getConnection(url, name, password);
                Statement statement = conn.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS " + faceliftedCar.getId().name()
                    + " (id int(7) PRIMARY KEY AUTO_INCREMENT, "
                    + " name VARCHAR(40), "
                    + " owner VARCHAR(40), "
                    + " handling_time int(3))";
            statement.executeUpdate(query);
            query = "INSERT INTO " + faceliftedCar.getId().name()
                    + " (name,owner,handling_time) VALUES ('" + faceliftedCar.getName() + "', '" + 
                    faceliftedCar.getOwner() + "', " + faceliftedCar.getHandlingTime() + " )";
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Log.error(ex.getMessage() + ex.getErrorCode(), ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                faceliftedCar = parking.takeCar();
                Thread.sleep(faceliftedCar.getHandlingTime() * 1000);
                writeDataAboutCar();
            } catch (InterruptedException ex) {
                Log.error(ex.getMessage(), ex);
            }
        }
    }
}
