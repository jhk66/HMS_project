/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.restaurant;

/**
 *
 * @author choun
 */
public class ServiceLoadInfo {
    
   
    private String date = null;
    private String time = null;
    private String roomNum = null;
    private String part = null;
    private String menu = null;
    private String price = null;
    

    public ServiceLoadInfo(String part, String menu, String price) {
        this.part = part;
        this.menu = menu;
        this.price = price;
    }
    
    public ServiceLoadInfo(String date, String time, String roomNum, String part, String menu, String price){
        this.date = date;
        this.time = time;
        this.roomNum = roomNum;
        this.part = part;
        this.menu = menu;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    
    public String getRoomNum(){
        return roomNum;
    }
    
    public String getPart() {
        return part;
    }

    public String getMenu() {
        return menu;
    }

    public String getPrice() {
        return price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
}
