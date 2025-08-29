package com.hotel.dao;

import com.hotel.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void add(Customer customer) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                "INSERT INTO customers(name,contact,address) VALUES(?,?,?)")) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getContact());
            ps.setString(3, customer.getAddress());
            ps.executeUpdate();
        }
    }

    public List<Customer> getAll() throws SQLException {
        List<Customer> list = new ArrayList<>();
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM customers")) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("address"),
                        rs.getString("check_in"),
                        rs.getString("check_out"),
                        rs.getInt("room_id")
                ));
            }
        }
        return list;
    }


    public void assignRoom(int customerId, int roomId, String checkIn) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                "UPDATE customers SET room_id=?, check_in=? WHERE id=?")) {
            ps.setInt(1, roomId);
            ps.setString(2, checkIn);
            ps.setInt(3, customerId);
            ps.executeUpdate();
        }
    }

    public void checkout(int customerId, String checkOut) throws SQLException {
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(
                "UPDATE customers SET check_out=? WHERE id=?")) {
            ps.setString(1, checkOut);
            ps.setInt(2, customerId);
            ps.executeUpdate();
        }
    }
}