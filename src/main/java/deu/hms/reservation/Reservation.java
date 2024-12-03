/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

/**
 *
 * @author choun
 */
public class Reservation {

    private String number = null;
    private String name = null;
    private String address = null;
    private String tel = null;
    private String people = null;
    private String roomNum = null;
    private String payType = null;
    private String price = null;
    private String checkInDate = null;
    private String checkOutDate = null;
    private String state = null;

    public Reservation(String number, String name, String address, String tel, String people, String roomNum, String payType, String price, String checkInDate, String checkOutDate, String state) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.people = people;
        this.roomNum = roomNum;
        this.payType = payType;
        this.price = price;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public String getPeople() {
        return people;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public String getPayType() {
        return payType;
    }

    public String getPrice() {
        return price;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getState() {
        return state;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setPrice(String payType) {
        this.price = payType;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setState(String state) {
        this.state = state;
    }

}
