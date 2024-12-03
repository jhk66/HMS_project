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
    private String name;

    public Room(String roomNum, String isBook, String name) {
        this.roomNum = roomNum;
        this.isBook = isBook;
        this.name = name;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public String getIsBook() {
        return isBook;
    }

    public String getName() {
        return name;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public void setIsBook(String isBook) {
        this.isBook = isBook;
    }

    public void setName(String name) {
        this.name = name;
    }

}
