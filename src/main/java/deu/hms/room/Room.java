/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.room;

/**
 *
 * @author choun
 */
public class Room {
    
    private String roomNum;
    private String isBook;
    private String firstName;
    private String lastName;

    public String getRoomNum() {
        return roomNum;
    }

    public String getIsBook() {
        return isBook;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public void setIsBook(String isBook) {
        this.isBook = isBook;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
}
