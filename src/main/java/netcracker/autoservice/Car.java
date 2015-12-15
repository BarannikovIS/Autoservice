/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netcracker.autoservice;

/**
 *
 * @author Иван
 */
public class Car {
    private CarBrand id;
    private String name;
    private String owner;
    private int handling_time;
    private OutputType type;
    
    public Car(CarBrand id, String name, String owner, int handling_time, OutputType type){
        this.id=id;
        this.name=name;
        this.owner=owner;
        this.handling_time=handling_time;
        this.type=type;
    }
    public CarBrand getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getOwner(){
        return owner;
    }
    public int getHandlingTime(){
        return handling_time;
    }
    public OutputType getType(){
        return type;
    }
}
