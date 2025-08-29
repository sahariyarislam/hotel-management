package com.hotel.ui;

import com.hotel.dao.CustomerDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Customer;
import com.hotel.model.Room;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HotelManagementUI extends JFrame {

    private final RoomDAO roomDAO = new RoomDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public HotelManagementUI() {
        super("Hotel Management System");
        initLookAndFeel();
        initComponents();
    }

    private void initLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        BackgroundPanel root = new BackgroundPanel("hotel_background.jpg");
        root.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Box content = Box.createVerticalBox();
        content.setOpaque(false);

        JLabel titleLabel = new JLabel("WELCOME TO CROWN HOTEL");
        titleLabel.setFont(new Font("Algerian", Font.BOLD, 45));
        titleLabel.setForeground(new Color(0x000080));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(25));

        JPanel controls = new JPanel(new GridLayout(5, 5, 80, 95));
        controls.setOpaque(false);

        JButton addRoomBtn = createStyledButton("Add Room", new Color(0x2E8B57));
        addRoomBtn.addActionListener(e -> onAddRoom());

        JButton viewRoomsBtn = createStyledButton("View Rooms", new Color(0x4682B4));
        viewRoomsBtn.addActionListener(e -> onViewRooms());

        JButton addCustomerBtn = createStyledButton("Add Customer", new Color(0x8B4513));
        addCustomerBtn.addActionListener(e -> onAddCustomer());

        JButton viewCustomersBtn = createStyledButton("View Customers", new Color(0x6A5ACD));
        viewCustomersBtn.addActionListener(e -> onViewCustomers());

        JButton bookRoomBtn = createStyledButton("Book Room", new Color(0xDAA520));
        bookRoomBtn.addActionListener(e -> onBookRoom());

        JButton checkoutBtn = createStyledButton("Checkout", new Color(0xB22222));
        checkoutBtn.addActionListener(e -> onCheckout());

        controls.add(addRoomBtn);
        controls.add(viewRoomsBtn);
        controls.add(addCustomerBtn);
        controls.add(viewCustomersBtn);
        controls.add(bookRoomBtn);
        controls.add(checkoutBtn);

        content.add(controls);
        root.add(content, BorderLayout.CENTER);

        add(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 520);
        setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String label, Color bg) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(30, 20));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Algerian", Font.PLAIN, 16));
        return button;
    }

    private void onAddRoom() {
        JTextField roomNumber = new JTextField();
        JTextField type = new JTextField();
        JTextField price = new JTextField();

        int res = JOptionPane.showConfirmDialog(this,
                new Object[]{"Room Number:", roomNumber, "Type (Single/Double/Suite):", type, "Price:", price},
                "Add Room", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                roomDAO.add(new Room(roomNumber.getText(), type.getText(), Double.parseDouble(price.getText()), true));
                JOptionPane.showMessageDialog(this, "Room added.");
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    private void onViewRooms() {
        try {
            List<Room> rooms = roomDAO.getAll();
            String msg = rooms.stream()
                    .map(r -> String.format("%s | %s | $%.2f | %s",
                            r.getRoomNumber(), r.getType(), r.getPrice(),
                            r.isAvailable() ? "Available" : "Occupied"))
                    .collect(Collectors.joining("\n"));
            JOptionPane.showMessageDialog(this, msg.isEmpty() ? "No rooms." : msg);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onAddCustomer() {
        JTextField name = new JTextField();
        JTextField contact = new JTextField();
        JTextField address = new JTextField();

        int res = JOptionPane.showConfirmDialog(this,
                new Object[]{"Name:", name, "Contact:", contact, "Address:", address},
                "Add Customer", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                customerDAO.add(new Customer(name.getText(), contact.getText(), address.getText()));
                JOptionPane.showMessageDialog(this, "Customer added.");
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    private void onViewCustomers() {
        try {
            List<Customer> customers = customerDAO.getAll();
            String msg = customers.stream()
                    .map(c -> String.format("%d | %s | %s | %s | Room: %d",
                            c.getId(), c.getName(), c.getContact(), c.getAddress(), c.getRoomId()))
                    .collect(Collectors.joining("\n"));
            JOptionPane.showMessageDialog(this, msg.isEmpty() ? "No customers." : msg);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onBookRoom() {
        try {
            String customerIdStr = JOptionPane.showInputDialog(this, "Customer ID:");
            if (customerIdStr == null) return;
            int customerId = Integer.parseInt(customerIdStr);

            String roomType = JOptionPane.showInputDialog(this, "Desired Room Type (Single/Double/Suite):");
            if (roomType == null) return;

            Room room = roomDAO.findAvailable(roomType);
            if (room == null) {
                JOptionPane.showMessageDialog(this, "No available room of that type.");
                return;
            }

            roomDAO.updateAvailability(room.getId(), false);
            customerDAO.assignRoom(customerId, room.getId(), LocalDate.now().toString());
            JOptionPane.showMessageDialog(this, "Room " + room.getRoomNumber() + " booked for customer " + customerId);

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onCheckout() {
        try {
            String customerIdStr = JOptionPane.showInputDialog(this, "Customer ID to checkout:");
            if (customerIdStr == null) return;
            int customerId = Integer.parseInt(customerIdStr);

            List<Customer> customers = customerDAO.getAll();
            Customer target = customers.stream().filter(c -> c.getId() == customerId).findFirst().orElse(null);

            if (target == null || target.getRoomId() == -1) {
                JOptionPane.showMessageDialog(this, "Customer not found or no room assigned.");
                return;
            }

            roomDAO.updateAvailability(target.getRoomId(), true);
            customerDAO.checkout(customerId, LocalDate.now().toString());
            JOptionPane.showMessageDialog(this, "Checkout complete for customer " + customerId);

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
            } else {
                System.err.println("Image not found: " + imagePath);
            }
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}