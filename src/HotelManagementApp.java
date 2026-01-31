import java.time.LocalDateTime;
import java.util.Scanner;

public class HotelManagementApp {
    
    static Scanner input = new Scanner(System.in);
    static Customer_Entry_DB customerDB = new Customer_Entry_DB();
    static Room roomDB = new Room();
    static Booking_db bookingDB = new Booking_db();
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   WELCOME TO HOTEL MANAGEMENT SYSTEM   ");
        System.out.println("========================================");
        
        while (true) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    roomMenu();
                    break;
                case 3:
                    bookingMenu();
                    break;
                case 0:
                    System.out.println("\nThank you for using Hotel Management System!");
                    System.out.println("Goodbye!");
                    input.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    static void showMainMenu() {
        System.out.println("\n======== MAIN MENU ========");
        System.out.println("1. Customer Management");
        System.out.println("2. Room Management");
        System.out.println("3. Booking Management");
        System.out.println("0. Exit");
        System.out.println("===========================");
    }
    
    // ============ CUSTOMER MENU ============
    static void customerMenu() {
        while (true) {
            System.out.println("\n===== CUSTOMER MANAGEMENT =====");
            System.out.println("1. Add New Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Search Customer by Phone");
            System.out.println("4. Delete Customer");
            System.out.println("0. Back to Main Menu");
            System.out.println("===============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    customerDB.getAllCustomers();
                    break;
                case 3:
                    System.out.print("Enter phone number to search: ");
                    String phone = input.nextLine();
                    customerDB.searchCustomerByPhone(phone);
                    break;
                case 4:
                    int custId = getIntInput("Enter Customer ID to delete: ");
                    customerDB.deleteCustomer(custId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    static void addCustomer() {
        System.out.println("\n--- Add New Customer ---");
        System.out.print("Enter customer name: ");
        String name = input.nextLine();
        System.out.print("Enter phone number: ");
        String phone = input.nextLine();
        System.out.print("Enter ID proof (Aadhar/PAN/Passport): ");
        String idProof = input.nextLine();
        
        CustomerEntry customer = new CustomerEntry(name, phone, idProof);
        customerDB.addCustomer(customer);
    }
    
    // ============ ROOM MENU ============
    static void roomMenu() {
        while (true) {
            System.out.println("\n===== ROOM MANAGEMENT =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. View All Rooms");
            System.out.println("3. Add New Room");
            System.out.println("4. Update Room Status");
            System.out.println("5. Delete Room");
            System.out.println("0. Back to Main Menu");
            System.out.println("===========================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    roomDB.viewAvailableRooms();
                    break;
                case 2:
                    roomDB.viewAllRooms();
                    break;
                case 3:
                    addRoom();
                    break;
                case 4:
                    updateRoomStatus();
                    break;
                case 5:
                    int roomId = getIntInput("Enter Room ID to delete: ");
                    roomDB.deleteRoom(roomId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    static void addRoom() {
        System.out.println("\n--- Add New Room ---");
        int roomId = getIntInput("Enter Room ID: ");
        System.out.print("Enter Room Type (Single/Double/Suite): ");
        String type = input.nextLine();
        double price = getDoubleInput("Enter Price per Day: ");
        
        roomDB.addRoom(roomId, type, price);
    }
    
    static void updateRoomStatus() {
        int roomId = getIntInput("Enter Room ID: ");
        System.out.print("Enter new status (available/booked/maintenance): ");
        String status = input.nextLine();
        roomDB.updateRoomStatus(roomId, status);
    }
    
    // ============ BOOKING MENU ============
    static void bookingMenu() {
        while (true) {
            System.out.println("\n===== BOOKING MANAGEMENT =====");
            System.out.println("1. Book a Room");
            System.out.println("2. View Active Bookings");
            System.out.println("3. View Booking History");
            System.out.println("4. Checkout (Generate Bill)");
            System.out.println("5. Cancel Booking");
            System.out.println("0. Back to Main Menu");
            System.out.println("==============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bookRoom();
                    break;
                case 2:
                    bookingDB.getAllBookings();
                    break;
                case 3:
                    bookingDB.getBookingHistory();
                    break;
                case 4:
                    int checkoutId = getIntInput("Enter Booking ID to checkout: ");
                    bookingDB.checkOut(checkoutId);
                    break;
                case 5:
                    int cancelId = getIntInput("Enter Booking ID to cancel: ");
                    bookingDB.cancelBooking(cancelId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    static void bookRoom() {
        System.out.println("\n--- Book a Room ---");
        
        // Show available rooms first
        roomDB.viewAvailableRooms();
        
        int customerId = getIntInput("Enter Customer ID: ");
        int roomId = getIntInput("Enter Room ID to book: ");
        
        Booking booking = new Booking(roomId, customerId, LocalDateTime.now());
        bookingDB.bookRoom(booking);
    }
    
    // ============ UTILITY METHODS ============
    static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(input.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(input.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}
