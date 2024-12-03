/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.login;

/**
 *
 * @author choun
 */
public class Account {
    private String id = null;
    private String passward = null;
    private String position = null;

    public Account(String id, String passward, String position) {
        this.id = id;
        this.passward = passward;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public String getPassward() {
        return passward;
    }

    public String getPosition() {
        return position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    
}
