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
    
    private String part;
    private String menu;
    private String price;

    public ServiceLoadInfo(String part, String menu, String price) {
        this.part = part;
        this.menu = menu;
        this.price = price;
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
