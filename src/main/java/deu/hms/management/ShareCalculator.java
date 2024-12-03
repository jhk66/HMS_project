/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.management;

import deu.hms.reservation.Reservation;
import deu.hms.reservation.ReservationLoad;
import deu.hms.restaurant.ServiceLoadInfo;
import deu.hms.restaurant.ServiceReservationLoad;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author choun
 */
public class ShareCalculator {

    ArrayList<Reservation> reservationList = new ArrayList<>();
    ArrayList<ServiceLoadInfo> serviceReservationList = new ArrayList<>();

    private Date startDate = null;
    private Date endDate = null;
    private String reservationCount = null;
    private String reservationPeople = null;
    private String totalRoomPrice = null;
    private String totalServicePrice = null;
    private String estimatedShare = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private JTable table;

    public ShareCalculator(String startDate, String endDate, JTable table) throws ParseException {
        ReservationLoad RL = new ReservationLoad();
        ServiceReservationLoad SRL = new ServiceReservationLoad();

        this.startDate = dateFormat.parse(startDate);
        this.endDate = dateFormat.parse(endDate);
        reservationList = RL.getList();
        serviceReservationList = SRL.getList();

        Calculation();
        InsertTable(table);
    }

    private void Calculation() throws ParseException {
        double count = 0;
        int people = 0;
        int roomPrice = 0;
        int servicePrice = 0;
        System.out.println(reservationList.size());
        System.out.println(serviceReservationList.size());
        for (int i = 0; i < reservationList.size(); i++) {
            Date checkInDate = dateFormat.parse(reservationList.get(i).getCheckInDate());
            Date checkOutDate = dateFormat.parse(reservationList.get(i).getCheckOutDate());

            if (checkOutDate.before(endDate) && checkInDate.after(startDate)) {
                count++;
                people += Integer.valueOf(reservationList.get(i).getPeople());
                roomPrice += Integer.valueOf(reservationList.get(i).getPrice());
            }
            System.out.println("Count = " + count);
            System.out.println("People = " + people);
            System.out.println("roomPrice = " + roomPrice);
        }

        for (int i = 0; i < serviceReservationList.size(); i++) {
            Date checkInDate = dateFormat.parse(serviceReservationList.get(i).getDate());
            Date checkOutDate = dateFormat.parse(serviceReservationList.get(i).getDate());
            
            if (checkOutDate.before(endDate) && checkInDate.after(startDate)){
                servicePrice += Integer.valueOf(serviceReservationList.get(i).getPrice());
            }
            System.out.println("servicePrice = " + servicePrice);
        }
        
        reservationCount = String.valueOf((int)count);
        reservationPeople = String.valueOf(people);
        totalRoomPrice = String.valueOf(roomPrice);
        totalServicePrice = String.valueOf(servicePrice);
        estimatedShare = String.valueOf((count/100) * 100);
    }
    
    private void InsertTable(JTable table){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{
            reservationCount, reservationPeople,
            totalRoomPrice, totalServicePrice
        });
    }

    public String getReservationCount() {
        return reservationCount;
    }

    public String getReservationPeople() {
        return reservationPeople;
    }

    public String getTotalRoomPrice() {
        return totalRoomPrice;
    }

    public String getTotalServicePrice() {
        return totalServicePrice;
    }

    public String getEstimatedShare() {
        return estimatedShare;
    }

    public void setReservationCount(String reservationCount) {
        this.reservationCount = reservationCount;
    }

    public void setReservationPeople(String reservationPeople) {
        this.reservationPeople = reservationPeople;
    }

    public void setTotalRoomPrice(String totalRoomPrice) {
        this.totalRoomPrice = totalRoomPrice;
    }

    public void setTotalServicePrice(String totalServicePrice) {
        this.totalServicePrice = totalServicePrice;
    }

}
