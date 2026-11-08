/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRACY
 */
public class HallMg {
    private String hallName;
    private String hallType;
    
    public HallMg(String hallName, String hallType){
        this.hallName = hallName;
        this.hallType = hallType;
    }
    
    public String getHallName(){
        return hallName;
    }
    
    public String getHallType(){
        return hallType;
    }
    
    public void setHallName(String hallName){
        this.hallName = hallName;
    }
    
    public void setHallType(String halltype){
        this.hallType = hallType;
    }
    
    @Override
    // Polymorphism
    public String toString(){
        return hallName + " (" + hallType + ")";
    }
}
