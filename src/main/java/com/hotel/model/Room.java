package com.hotel.model;

public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private double price;
    private boolean available;

    public Room(int id, String roomNumber, String type, double price, boolean available) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public Room(String roomNumber, String type, double price, boolean available) {
        this(-1, roomNumber, type, price, available);
    }

    public int getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }
}