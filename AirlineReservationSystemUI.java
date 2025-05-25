import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Passenger {
    private String passengerId;
    private String name;
    private String email;
    private String phone;

    public Passenger(String passengerId, String name, String email, String phone) {
        this.passengerId = passengerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getPassengerId() { return passengerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "ID: " + passengerId + " | Name: " + name + " | Email: " + email + " | Phone: " + phone;
    }
}

class Flight {
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private String departureTime;
    private String arrivalTime;
    private int availableSeats;
    private List<String> bookedPassengers;

    public Flight(String flightNumber, String departureCity, String arrivalCity, 
                  String departureTime, String arrivalTime, int availableSeats) {
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.bookedPassengers = new ArrayList<>();
    }

    public String getFlightNumber() { return flightNumber; }
    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public boolean bookPassenger(String passengerId) {
        if (availableSeats > 0) {
            bookedPassengers.add(passengerId);
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean cancelBooking(String passengerId) {
        if (bookedPassengers.remove(passengerId)) {
            availableSeats++;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return flightNumber + " | " + departureCity + " (" + departureTime + ") to " + 
               arrivalCity + " (" + arrivalTime + ") | Seats: " + availableSeats;
    }
}

public class AirlineReservationSystemUI extends JFrame {
    private List<Passenger> passengers;
    private List<Flight> flights;
    
    private JTabbedPane tabbedPane;
    private JPanel passengerPanel, flightPanel, bookingPanel;
    
    // Passenger UI components
    private JTextField passengerIdField, nameField, emailField, phoneField;
    private JTextArea passengerOutputArea;
    private JList<String> passengerList;
    private DefaultListModel<String> passengerListModel;
    
    // Flight UI components
    private JTextField flightNumberField, departureCityField, arrivalCityField;
    private JTextField departureTimeField, arrivalTimeField, seatsField;
    private JTextArea flightOutputArea;
    private JList<String> flightList;
    private DefaultListModel<String> flightListModel;
    
    // Booking UI components
    private JComboBox<String> passengerCombo, flightCombo;
    private JTextArea bookingOutputArea;

    public AirlineReservationSystemUI() {
        super("Airline Reservation System");
        passengers = new ArrayList<>();
        flights = new ArrayList<>();
        
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Create panels
        createPassengerPanel();
        createFlightPanel();
        createBookingPanel();
        
        tabbedPane.addTab("Passengers", passengerPanel);
        tabbedPane.addTab("Flights", flightPanel);
        tabbedPane.addTab("Bookings", bookingPanel);
        
        add(tabbedPane);
    }

    private void createPassengerPanel() {
        passengerPanel = new JPanel(new BorderLayout(10, 10));
        passengerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input fields panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Passenger Details"));
        
        passengerIdField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        
        inputPanel.add(new JLabel("Passenger ID:"));
        inputPanel.add(passengerIdField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Passenger");
        JButton updateButton = new JButton("Update Passenger");
        JButton deleteButton = new JButton("Delete Passenger");
        
        addButton.addActionListener(e -> addPassenger());
        updateButton.addActionListener(e -> updatePassenger());
        deleteButton.addActionListener(e -> deletePassenger());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        
        // Passenger list
        passengerListModel = new DefaultListModel<>();
        passengerList = new JList<>(passengerListModel);
        passengerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        passengerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedPassenger();
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(passengerList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Passenger List"));
        
        // Output area
        passengerOutputArea = new JTextArea(5, 20);
        passengerOutputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(passengerOutputArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Details"));
        
        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.CENTER);
        leftPanel.add(outputScrollPane, BorderLayout.SOUTH);
        
        passengerPanel.add(leftPanel, BorderLayout.WEST);
        passengerPanel.add(listScrollPane, BorderLayout.CENTER);
    }

    private void createFlightPanel() {
        flightPanel = new JPanel(new BorderLayout(10, 10));
        flightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input fields panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Flight Details"));
        
        flightNumberField = new JTextField();
        departureCityField = new JTextField();
        arrivalCityField = new JTextField();
        departureTimeField = new JTextField();
        arrivalTimeField = new JTextField();
        seatsField = new JTextField();
        
        inputPanel.add(new JLabel("Flight Number:"));
        inputPanel.add(flightNumberField);
        inputPanel.add(new JLabel("Departure City:"));
        inputPanel.add(departureCityField);
        inputPanel.add(new JLabel("Arrival City:"));
        inputPanel.add(arrivalCityField);
        inputPanel.add(new JLabel("Departure Time:"));
        inputPanel.add(departureTimeField);
        inputPanel.add(new JLabel("Arrival Time:"));
        inputPanel.add(arrivalTimeField);
        inputPanel.add(new JLabel("Available Seats:"));
        inputPanel.add(seatsField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Flight");
        JButton updateButton = new JButton("Update Flight");
        JButton deleteButton = new JButton("Delete Flight");
        
        addButton.addActionListener(e -> addFlight());
        updateButton.addActionListener(e -> updateFlight());
        deleteButton.addActionListener(e -> deleteFlight());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        
        // Flight list
        flightListModel = new DefaultListModel<>();
        flightList = new JList<>(flightListModel);
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedFlight();
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(flightList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Flight List"));
        
        // Output area
        flightOutputArea = new JTextArea(5, 20);
        flightOutputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(flightOutputArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Details"));
        
        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.CENTER);
        leftPanel.add(outputScrollPane, BorderLayout.SOUTH);
        
        flightPanel.add(leftPanel, BorderLayout.WEST);
        flightPanel.add(listScrollPane, BorderLayout.CENTER);
    }

    private void createBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout(10, 10));
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        
        passengerCombo = new JComboBox<>();
        flightCombo = new JComboBox<>();
        
        inputPanel.add(new JLabel("Passenger:"));
        inputPanel.add(passengerCombo);
        inputPanel.add(new JLabel("Flight:"));
        inputPanel.add(flightCombo);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton bookButton = new JButton("Book Flight");
        JButton cancelButton = new JButton("Cancel Booking");
        
        bookButton.addActionListener(e -> bookFlight());
        cancelButton.addActionListener(e -> cancelBooking());
        
        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        
        // Output area
        bookingOutputArea = new JTextArea(10, 30);
        bookingOutputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(bookingOutputArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Booking Information"));
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        bookingPanel.add(topPanel, BorderLayout.NORTH);
        bookingPanel.add(outputScrollPane, BorderLayout.CENTER);
    }

    // Passenger operations
    private void addPassenger() {
        String id = passengerIdField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (findPassengerById(id) != null) {
            JOptionPane.showMessageDialog(this, "Passenger ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Passenger passenger = new Passenger(id, name, email, phone);
        passengers.add(passenger);
        passengerListModel.addElement(passenger.toString());
        
        passengerIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        
        updatePassengerCombo();
        JOptionPane.showMessageDialog(this, "Passenger added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updatePassenger() {
        int selectedIndex = passengerList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a passenger to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Passenger passenger = passengers.get(selectedIndex);
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (!name.isEmpty()) passenger.setName(name);
        if (!email.isEmpty()) passenger.setEmail(email);
        if (!phone.isEmpty()) passenger.setPhone(phone);
        
        passengerListModel.set(selectedIndex, passenger.toString());
        updatePassengerCombo();
        showSelectedPassenger();
        JOptionPane.showMessageDialog(this, "Passenger updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deletePassenger() {
        int selectedIndex = passengerList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a passenger to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Passenger passenger = passengers.get(selectedIndex);
        
        // Cancel all bookings for this passenger
        for (Flight flight : flights) {
            flight.cancelBooking(passenger.getPassengerId());
        }
        
        passengers.remove(selectedIndex);
        passengerListModel.remove(selectedIndex);
        
        passengerIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passengerOutputArea.setText("");
        
        updatePassengerCombo();
        JOptionPane.showMessageDialog(this, "Passenger deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSelectedPassenger() {
        int selectedIndex = passengerList.getSelectedIndex();
        if (selectedIndex != -1) {
            Passenger passenger = passengers.get(selectedIndex);
            passengerIdField.setText(passenger.getPassengerId());
            nameField.setText(passenger.getName());
            emailField.setText(passenger.getEmail());
            phoneField.setText(passenger.getPhone());
            
            passengerOutputArea.setText("Passenger ID: " + passenger.getPassengerId() + "\n" +
                                     "Name: " + passenger.getName() + "\n" +
                                     "Email: " + passenger.getEmail() + "\n" +
                                     "Phone: " + passenger.getPhone());
        }
    }

    // Flight operations
    private void addFlight() {
        String number = flightNumberField.getText().trim();
        String departure = departureCityField.getText().trim();
        String arrival = arrivalCityField.getText().trim();
        String depTime = departureTimeField.getText().trim();
        String arrTime = arrivalTimeField.getText().trim();
        String seatsText = seatsField.getText().trim();
        
        if (number.isEmpty() || departure.isEmpty() || arrival.isEmpty() || 
            depTime.isEmpty() || arrTime.isEmpty() || seatsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int seats = Integer.parseInt(seatsText);
            if (seats <= 0) {
                JOptionPane.showMessageDialog(this, "Seats must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (findFlightByNumber(number) != null) {
                JOptionPane.showMessageDialog(this, "Flight number already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Flight flight = new Flight(number, departure, arrival, depTime, arrTime, seats);
            flights.add(flight);
            flightListModel.addElement(flight.toString());
            
            flightNumberField.setText("");
            departureCityField.setText("");
            arrivalCityField.setText("");
            departureTimeField.setText("");
            arrivalTimeField.setText("");
            seatsField.setText("");
            
            updateFlightCombo();
            JOptionPane.showMessageDialog(this, "Flight added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number for seats", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFlight() {
        int selectedIndex = flightList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Flight flight = flights.get(selectedIndex);
        String departure = departureCityField.getText().trim();
        String arrival = arrivalCityField.getText().trim();
        String depTime = departureTimeField.getText().trim();
        String arrTime = arrivalTimeField.getText().trim();
        String seatsText = seatsField.getText().trim();
        
        if (!departure.isEmpty()) flight.setDepartureCity(departure);
        if (!arrival.isEmpty()) flight.setArrivalCity(arrival);
        if (!depTime.isEmpty()) flight.setDepartureTime(depTime);
        if (!arrTime.isEmpty()) flight.setArrivalTime(arrTime);
        if (!seatsText.isEmpty()) {
            try {
                int seats = Integer.parseInt(seatsText);
                if (seats >= 0) {
                    flight.setAvailableSeats(seats);
                } else {
                    JOptionPane.showMessageDialog(this, "Seats must be non-negative", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number for seats", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        flightListModel.set(selectedIndex, flight.toString());
        updateFlightCombo();
        showSelectedFlight();
        JOptionPane.showMessageDialog(this, "Flight updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteFlight() {
        int selectedIndex = flightList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        flights.remove(selectedIndex);
        flightListModel.remove(selectedIndex);
        
        flightNumberField.setText("");
        departureCityField.setText("");
        arrivalCityField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        seatsField.setText("");
        flightOutputArea.setText("");
        
        updateFlightCombo();
        JOptionPane.showMessageDialog(this, "Flight deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSelectedFlight() {
        int selectedIndex = flightList.getSelectedIndex();
        if (selectedIndex != -1) {
            Flight flight = flights.get(selectedIndex);
            flightNumberField.setText(flight.getFlightNumber());
            departureCityField.setText(flight.getDepartureCity());
            arrivalCityField.setText(flight.getArrivalCity());
            departureTimeField.setText(flight.getDepartureTime());
            arrivalTimeField.setText(flight.getArrivalTime());
            seatsField.setText(String.valueOf(flight.getAvailableSeats()));
            
            flightOutputArea.setText("Flight Number: " + flight.getFlightNumber() + "\n" +
                                    "From: " + flight.getDepartureCity() + " at " + flight.getDepartureTime() + "\n" +
                                    "To: " + flight.getArrivalCity() + " at " + flight.getArrivalTime() + "\n" +
                                    "Available Seats: " + flight.getAvailableSeats());
        }
    }

    // Booking operations
    private void bookFlight() {
        if (passengerCombo.getSelectedIndex() == -1 || flightCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select both passenger and flight", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String passengerId = ((String) passengerCombo.getSelectedItem()).split(" \\| ")[0];
        String flightNumber = ((String) flightCombo.getSelectedItem()).split(" \\| ")[0];
        
        Passenger passenger = findPassengerById(passengerId);
        Flight flight = findFlightByNumber(flightNumber);
        
        if (passenger == null || flight == null) {
            JOptionPane.showMessageDialog(this, "Passenger or Flight not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (flight.bookPassenger(passengerId)) {
            bookingOutputArea.append("Booking successful for:\n");
            bookingOutputArea.append("Passenger: " + passenger.getName() + "\n");
            bookingOutputArea.append("Flight: " + flight.getFlightNumber() + "\n\n");
            updateFlightCombo();
            showSelectedFlight();
        } else {
            JOptionPane.showMessageDialog(this, "No available seats on this flight", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelBooking() {
        if (passengerCombo.getSelectedIndex() == -1 || flightCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select both passenger and flight", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String passengerId = ((String) passengerCombo.getSelectedItem()).split(" \\| ")[0];
        String flightNumber = ((String) flightCombo.getSelectedItem()).split(" \\| ")[0];
        
        Passenger passenger = findPassengerById(passengerId);
        Flight flight = findFlightByNumber(flightNumber);
        
        if (passenger == null || flight == null) {
            JOptionPane.showMessageDialog(this, "Passenger or Flight not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (flight.cancelBooking(passengerId)) {
            bookingOutputArea.append("Booking cancelled for:\n");
            bookingOutputArea.append("Passenger: " + passenger.getName() + "\n");
            bookingOutputArea.append("Flight: " + flight.getFlightNumber() + "\n\n");
            updateFlightCombo();
            showSelectedFlight();
        } else {
            JOptionPane.showMessageDialog(this, "This passenger doesn't have a booking on this flight", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper methods
    private Passenger findPassengerById(String id) {
        for (Passenger passenger : passengers) {
            if (passenger.getPassengerId().equals(id)) {
                return passenger;
            }
        }
        return null;
    }

    private Flight findFlightByNumber(String number) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(number)) {
                return flight;
            }
        }
        return null;
    }

    private void updatePassengerCombo() {
        passengerCombo.removeAllItems();
        for (Passenger passenger : passengers) {
            passengerCombo.addItem(passenger.getPassengerId() + " | " + passenger.getName());
        }
    }

    private void updateFlightCombo() {
        flightCombo.removeAllItems();
        for (Flight flight : flights) {
            flightCombo.addItem(flight.getFlightNumber() + " | " + flight.getDepartureCity() + " to " + flight.getArrivalCity());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirlineReservationSystemUI system = new AirlineReservationSystemUI();
            system.setVisible(true);
        });
    }

    private static void runTerminalMode(Scanner scanner) {
        System.out.println("=== Terminal Booking System ===");

        System.out.print("Enter Passenger ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();

        System.out.println("\nBooking Successful!");
        System.out.println("Passenger Details:");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
    }

}