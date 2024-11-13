/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.room;

/**
 *
 * @author choun
 */
public class RoomInfo {
    
    private String floor;
    private String type;
    private String price;

    public RoomInfo(String floor, String type, String price) {
        this.floor = floor;
        this.type = type;
        this.price = price;
    }

    public String getFloor() {
        return floor;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
}
