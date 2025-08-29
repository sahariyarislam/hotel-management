package com.hotel;

import com.hotel.ui.HotelManagementUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelManagementUI().setVisible(true));
    }
}