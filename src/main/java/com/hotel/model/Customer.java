package com.hotel.model;

public class Customer {
    private int id;
    private String name;
    private String contact;
    private String address;
    private String checkIn;
    private String checkOut;
    private int roomId;

    public Customer(int id, String name, String contact, String address,
                    String checkIn, String checkOut, int roomId) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomId = roomId;
    }

    public Customer(String name, String contact, String address) {
        this(-1, name, contact, address, null, null, -1);
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
    public int getRoomId() { return roomId; }

    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
}