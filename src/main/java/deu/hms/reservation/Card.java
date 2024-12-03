/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

/**
 *
 * @author choun
 */
public class Card {
    private String name = null;
    private String cardNum = null;

    public Card(String name, String cardNum) {
        this.name = name;
        this.cardNum =cardNum;
    }
    
    public String getName() {
        return name;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    
    
    
}
