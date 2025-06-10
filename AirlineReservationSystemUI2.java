import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    public List<String> getBookedPassengers() { return bookedPassengers; }

    public boolean bookPassenger(String passengerId) {
        if (availableSeats > 0 && !bookedPassengers.contains(passengerId)) {
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

public class AirlineReservationSystemUI2 extends JFrame {
    private List<Passenger> passengers;
    private List<Flight> flights;
    
    private JTabbedPane tabbedPane;
    private JPanel passengerPanel, flightPanel, bookingPanel;
    
    // Passenger UI components
    private JTextField passengerIdField, nameField, emailField, phoneField, searchPassengerField;
    private JTextArea passengerOutputArea;
    private JList<String> passengerList;
    private DefaultListModel<String> passengerListModel;
    
    // Flight UI components
    private JTextField flightNumberField, departureCityField, arrivalCityField;
    private JTextField departureTimeField, arrivalTimeField, seatsField, searchFlightField;
    private JTextArea flightOutputArea;
    private JList<String> flightList;
    private DefaultListModel<String> flightListModel;
    
    // Booking UI components
    private JComboBox<String> passengerCombo, flightCombo;
    private JTextArea bookingOutputArea, historyArea;

    // Constants for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,4}[-\\s.]?[0-9]{1,6}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");

    public AirlineReservationSystemUI2() {
        super("Airline Reservation System");
        passengers = new ArrayList<>();
        flights = new ArrayList<>();
        
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
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
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPassengerField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(searchPassengerField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchButton.addActionListener(e -> searchPassengers());
        
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
        JButton clearButton = new JButton("Clear");
        
        addButton.addActionListener(e -> addPassenger());
        updateButton.addActionListener(e -> updatePassenger());
        deleteButton.addActionListener(e -> deletePassenger());
        clearButton.addActionListener(e -> clearPassengerFields());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // Passenger list
        passengerListModel = new DefaultListModel<>();
        passengerList = new JList<>(passengerListModel);
        passengerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        passengerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedPassenger();
            }
        });
        
        // Add double-click listener
        passengerList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showSelectedPassenger();
                }
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
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(inputPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(listScrollPane, BorderLayout.CENTER);
        rightPanel.add(outputScrollPane, BorderLayout.SOUTH);
        
        passengerPanel.add(leftPanel, BorderLayout.WEST);
        passengerPanel.add(rightPanel, BorderLayout.CENTER);
    }

    private void createFlightPanel() {
        flightPanel = new JPanel(new BorderLayout(10, 10));
        flightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchFlightField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(searchFlightField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchButton.addActionListener(e -> searchFlights());
        
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
        inputPanel.add(new JLabel("Departure Time (HH:MM):"));
        inputPanel.add(departureTimeField);
        inputPanel.add(new JLabel("Arrival Time (HH:MM):"));
        inputPanel.add(arrivalTimeField);
        inputPanel.add(new JLabel("Available Seats:"));
        inputPanel.add(seatsField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Flight");
        JButton updateButton = new JButton("Update Flight");
        JButton deleteButton = new JButton("Delete Flight");
        JButton clearButton = new JButton("Clear");
        
        addButton.addActionListener(e -> addFlight());
        updateButton.addActionListener(e -> updateFlight());
        deleteButton.addActionListener(e -> deleteFlight());
        clearButton.addActionListener(e -> clearFlightFields());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // Flight list
        flightListModel = new DefaultListModel<>();
        flightList = new JList<>(flightListModel);
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedFlight();
            }
        });
        
        // Add double-click listener
        flightList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showSelectedFlight();
                }
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
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(inputPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(listScrollPane, BorderLayout.CENTER);
        rightPanel.add(outputScrollPane, BorderLayout.SOUTH);
        
        flightPanel.add(leftPanel, BorderLayout.WEST);
        flightPanel.add(rightPanel, BorderLayout.CENTER);
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
        JButton refreshButton = new JButton("Refresh Lists");
        
        bookButton.addActionListener(e -> bookFlight());
        cancelButton.addActionListener(e -> cancelBooking());
        refreshButton.addActionListener(e -> refreshCombos());
        
        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        
        // Output area
        bookingOutputArea = new JTextArea(10, 30);
        bookingOutputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(bookingOutputArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Booking Information"));
        
        // History area
        historyArea = new JTextArea(10, 30);
        historyArea.setEditable(false);
        JScrollPane historyScrollPane = new JScrollPane(historyArea);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Booking History"));
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(outputScrollPane, BorderLayout.CENTER);
        centerPanel.add(historyScrollPane, BorderLayout.SOUTH);
        
        bookingPanel.add(topPanel, BorderLayout.NORTH);
        bookingPanel.add(centerPanel, BorderLayout.CENTER);
    }

    // Passenger operations
    private void addPassenger() {
        try {
            String id = passengerIdField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                showError("Please fill all fields");
                return;
            }
            
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                showError("Invalid email format");
                return;
            }
            
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                showError("Invalid phone number format");
                return;
            }
            
            if (findPassengerById(id) != null) {
                showError("Passenger ID already exists");
                return;
            }
            
            Passenger passenger = new Passenger(id, name, email, phone);
            passengers.add(passenger);
            passengerListModel.addElement(passenger.toString());
            
            clearPassengerFields();
            updatePassengerCombo();
            showMessage("Passenger added successfully");
        } catch (Exception e) {
            showError("Error adding passenger: " + e.getMessage());
        }
    }

    private void updatePassenger() {
        try {
            int selectedIndex = passengerList.getSelectedIndex();
            if (selectedIndex == -1) {
                showError("Please select a passenger to update");
                return;
            }
            
            Passenger passenger = passengers.get(selectedIndex);
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (!name.isEmpty()) {
                if (name.length() < 2) {
                    showError("Name must be at least 2 characters");
                    return;
                }
                passenger.setName(name);
            }
            
            if (!email.isEmpty()) {
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    showError("Invalid email format");
                    return;
                }
                passenger.setEmail(email);
            }
            
            if (!phone.isEmpty()) {
                if (!PHONE_PATTERN.matcher(phone).matches()) {
                    showError("Invalid phone number format");
                    return;
                }
                passenger.setPhone(phone);
            }
            
            passengerListModel.set(selectedIndex, passenger.toString());
            updatePassengerCombo();
            showSelectedPassenger();
            showMessage("Passenger updated successfully");
        } catch (Exception e) {
            showError("Error updating passenger: " + e.getMessage());
        }
    }

    private void deletePassenger() {
        try {
            int selectedIndex = passengerList.getSelectedIndex();
            if (selectedIndex == -1) {
                showError("Please select a passenger to delete");
                return;
            }
            
            Passenger passenger = passengers.get(selectedIndex);
            
            // Cancel all bookings for this passenger
            for (Flight flight : flights) {
                flight.cancelBooking(passenger.getPassengerId());
            }
            
            passengers.remove(selectedIndex);
            passengerListModel.remove(selectedIndex);
            
            clearPassengerFields();
            updatePassengerCombo();
            showMessage("Passenger deleted successfully");
        } catch (Exception e) {
            showError("Error deleting passenger: " + e.getMessage());
        }
    }

    private void showSelectedPassenger() {
        try {
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
        } catch (Exception e) {
            showError("Error displaying passenger: " + e.getMessage());
        }
    }

    // Flight operations
    private void addFlight() {
        try {
            String number = flightNumberField.getText().trim();
            String departure = departureCityField.getText().trim();
            String arrival = arrivalCityField.getText().trim();
            String depTime = departureTimeField.getText().trim();
            String arrTime = arrivalTimeField.getText().trim();
            String seatsText = seatsField.getText().trim();
            
            if (number.isEmpty() || departure.isEmpty() || arrival.isEmpty() || 
                depTime.isEmpty() || arrTime.isEmpty() || seatsText.isEmpty()) {
                showError("Please fill all fields");
                return;
            }
            
            if (!TIME_PATTERN.matcher(depTime).matches() || !TIME_PATTERN.matcher(arrTime).matches()) {
                showError("Invalid time format (use HH:MM)");
                return;
            }
            
            int seats;
            try {
                seats = Integer.parseInt(seatsText);
                if (seats <= 0) {
                    showError("Seats must be positive");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid number for seats");
                return;
            }
            
            if (findFlightByNumber(number) != null) {
                showError("Flight number already exists");
                return;
            }
            
            Flight flight = new Flight(number, departure, arrival, depTime, arrTime, seats);
            flights.add(flight);
            flightListModel.addElement(flight.toString());
            
            clearFlightFields();
            updateFlightCombo();
            showMessage("Flight added successfully");
        } catch (Exception e) {
            showError("Error adding flight: " + e.getMessage());
        }
    }

    private void updateFlight() {
        try {
            int selectedIndex = flightList.getSelectedIndex();
            if (selectedIndex == -1) {
                showError("Please select a flight to update");
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
            
            if (!depTime.isEmpty()) {
                if (!TIME_PATTERN.matcher(depTime).matches()) {
                    showError("Invalid departure time format (use HH:MM)");
                    return;
                }
                flight.setDepartureTime(depTime);
            }
            
            if (!arrTime.isEmpty()) {
                if (!TIME_PATTERN.matcher(arrTime).matches()) {
                    showError("Invalid arrival time format (use HH:MM)");
                    return;
                }
                flight.setArrivalTime(arrTime);
            }
            
            if (!seatsText.isEmpty()) {
                try {
                    int seats = Integer.parseInt(seatsText);
                    if (seats >= 0) {
                        flight.setAvailableSeats(seats);
                    } else {
                        showError("Seats must be non-negative");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showError("Invalid number for seats");
                    return;
                }
            }
            
            flightListModel.set(selectedIndex, flight.toString());
            updateFlightCombo();
            showSelectedFlight();
            showMessage("Flight updated successfully");
        } catch (Exception e) {
            showError("Error updating flight: " + e.getMessage());
        }
    }

    private void deleteFlight() {
        try {
            int selectedIndex = flightList.getSelectedIndex();
            if (selectedIndex == -1) {
                showError("Please select a flight to delete");
                return;
            }
            
            flights.remove(selectedIndex);
            flightListModel.remove(selectedIndex);
            
            clearFlightFields();
            updateFlightCombo();
            showMessage("Flight deleted successfully");
        } catch (Exception e) {
            showError("Error deleting flight: " + e.getMessage());
        }
    }

    private void showSelectedFlight() {
        try {
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
                                        "Available Seats: " + flight.getAvailableSeats() + "\n" +
                                        "Booked Passengers: " + flight.getBookedPassengers().size());
            }
        } catch (Exception e) {
            showError("Error displaying flight: " + e.getMessage());
        }
    }

    // Booking operations
    private void bookFlight() {
        try {
            if (passengerCombo.getSelectedIndex() == -1 || flightCombo.getSelectedIndex() == -1) {
                showError("Please select both passenger and flight");
                return;
            }
            
            String passengerId = ((String) passengerCombo.getSelectedItem()).split(" \\| ")[0];
            String flightNumber = ((String) flightCombo.getSelectedItem()).split(" \\| ")[0];
            
            Passenger passenger = findPassengerById(passengerId);
            Flight flight = findFlightByNumber(flightNumber);
            
            if (passenger == null || flight == null) {
                showError("Passenger or Flight not found");
                return;
            }
            
            if (flight.bookPassenger(passengerId)) {
                String historyEntry = "BOOKED: " + passenger.getName() + " on " + 
                                    flight.getFlightNumber() + " (" + 
                                    flight.getDepartureCity() + " to " + 
                                    flight.getArrivalCity() + ") at " + 
                                    new java.util.Date() + "\n";
                bookingOutputArea.append(historyEntry);
                historyArea.append(historyEntry);
                updateFlightCombo();
                showSelectedFlight();
                showMessage("Booking successful!");
            } else {
                showError("No available seats on this flight or passenger already booked");
            }
        } catch (Exception e) {
            showError("Error booking flight: " + e.getMessage());
        }
    }

    private void cancelBooking() {
        try {
            if (passengerCombo.getSelectedIndex() == -1 || flightCombo.getSelectedIndex() == -1) {
                showError("Please select both passenger and flight");
                return;
            }
            
            String passengerId = ((String) passengerCombo.getSelectedItem()).split(" \\| ")[0];
            String flightNumber = ((String) flightCombo.getSelectedItem()).split(" \\| ")[0];
            
            Passenger passenger = findPassengerById(passengerId);
            Flight flight = findFlightByNumber(flightNumber);
            
            if (passenger == null || flight == null) {
                showError("Passenger or Flight not found");
                return;
            }
            
            if (flight.cancelBooking(passengerId)) {
                String historyEntry = "CANCELLED: " + passenger.getName() + " from " + 
                                    flight.getFlightNumber() + " at " + 
                                    new java.util.Date() + "\n";
                bookingOutputArea.append(historyEntry);
                historyArea.append(historyEntry);
                updateFlightCombo();
                showSelectedFlight();
                showMessage("Booking cancelled successfully");
            } else {
                showError("This passenger doesn't have a booking on this flight");
            }
        } catch (Exception e) {
            showError("Error cancelling booking: " + e.getMessage());
        }
    }

    // Search functionality
    private void searchPassengers() {
        try {
            String query = searchPassengerField.getText().toLowerCase();
            passengerListModel.clear();
            
            if (query.isEmpty()) {
                // Show all passengers if search is empty
                for (Passenger passenger : passengers) {
                    passengerListModel.addElement(passenger.toString());
                }
                return;
            }
            
            for (Passenger passenger : passengers) {
                if (passenger.getPassengerId().toLowerCase().contains(query) ||
                    passenger.getName().toLowerCase().contains(query) ||
                    passenger.getEmail().toLowerCase().contains(query) ||
                    passenger.getPhone().toLowerCase().contains(query)) {
                    passengerListModel.addElement(passenger.toString());
                }
            }
            
            if (passengerListModel.isEmpty()) {
                showMessage("No passengers found matching: " + query);
            }
        } catch (Exception e) {
            showError("Error searching passengers: " + e.getMessage());
        }
    }

    private void searchFlights() {
        try {
            String query = searchFlightField.getText().toLowerCase();
            flightListModel.clear();
            
            if (query.isEmpty()) {
                // Show all flights if search is empty
                for (Flight flight : flights) {
                    flightListModel.addElement(flight.toString());
                }
                return;
            }
            
            for (Flight flight : flights) {
                if (flight.getFlightNumber().toLowerCase().contains(query) ||
                    flight.getDepartureCity().toLowerCase().contains(query) ||
                    flight.getArrivalCity().toLowerCase().contains(query) ||
                    flight.getDepartureTime().toLowerCase().contains(query) ||
                    flight.getArrivalTime().toLowerCase().contains(query)) {
                    flightListModel.addElement(flight.toString());
                }
            }
            
            if (flightListModel.isEmpty()) {
                showMessage("No flights found matching: " + query);
            }
        } catch (Exception e) {
            showError("Error searching flights: " + e.getMessage());
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

    private void refreshCombos() {
        updatePassengerCombo();
        updateFlightCombo();
        showMessage("Passenger and flight lists refreshed");
    }

    private void clearPassengerFields() {
        passengerIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passengerOutputArea.setText("");
    }

    private void clearFlightFields() {
        flightNumberField.setText("");
        departureCityField.setText("");
        arrivalCityField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        seatsField.setText("");
        flightOutputArea.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                AirlineReservationSystemUI2 system = new AirlineReservationSystemUI2();
                system.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error starting application: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
