import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Booking_db {
    
    // Book a room
    public int bookRoom(Booking b) {
        // First check if room is available
        Room roomOps = new Room();
        if (!roomOps.isRoomAvailable(b.getRoomId())) {
            System.out.println("Room is not available for booking!");
            return -1;
        }
        
        // Check if customer exists
        Customer_Entry_DB custOps = new Customer_Entry_DB();
        if (!custOps.customerExists(b.getCustomerId())) {
            System.out.println("Customer not found! Please register first.");
            return -1;
        }
        
        String query = "INSERT INTO BOOKING (customer_id, room_id, check_in, status) VALUES (?, ?, ?, 'active')";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, b.getCustomerId());
            pst.setInt(2, b.getRoomId());
            pst.setTimestamp(3, Timestamp.valueOf(b.getCheckIn()));
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                // Update room status to booked
                roomOps.updateRoomStatus(b.getRoomId(), "booked");
                
                ResultSet keys = pst.getGeneratedKeys();
                if (keys.next()) {
                    int bookingId = keys.getInt(1);
                    System.out.println("Room booked successfully! Booking ID: " + bookingId);
                    con.close();
                    return bookingId;
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // View all active bookings
    public void getAllBookings() {
        String query = "SELECT b.*, c.name, r.room_type, r.price_per_day FROM BOOKING b " +
                       "JOIN CUSTOMER c ON b.customer_id = c.customer_id " +
                       "JOIN ROOMS r ON b.room_id = r.room_id " +
                       "WHERE b.status = 'active'";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            System.out.println("\n====== ACTIVE BOOKINGS ======");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Booking ID: " + rs.getInt("booking_id"));
                System.out.println("Customer: " + rs.getString("name") + " (ID: " + rs.getInt("customer_id") + ")");
                System.out.println("Room: " + rs.getInt("room_id") + " (" + rs.getString("room_type") + ")");
                System.out.println("Price: Rs." + rs.getDouble("price_per_day") + "/day");
                System.out.println("Check-In: " + rs.getTimestamp("check_in"));
                System.out.println("----------------------");
            }
            if (!found) {
                System.out.println("No active bookings found.");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get booking by ID
    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM BOOKING WHERE booking_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, bookingId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Booking b = new Booking(
                    rs.getInt("room_id"),
                    rs.getInt("customer_id"),
                    rs.getTimestamp("check_in").toLocalDateTime()
                );
                b.setBookingId(rs.getInt("booking_id"));
                if (rs.getTimestamp("check_out") != null) {
                    b.setCheckOut(rs.getTimestamp("check_out").toLocalDateTime());
                }
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setStatus(rs.getString("status"));
                con.close();
                return b;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cancel booking
    public void cancelBooking(int bookingId) {
        Booking b = getBookingById(bookingId);
        if (b == null) {
            System.out.println("Booking not found!");
            return;
        }
        if (!b.getStatus().equals("active")) {
            System.out.println("Booking is already " + b.getStatus());
            return;
        }
        
        String query = "UPDATE BOOKING SET status = 'cancelled' WHERE booking_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, bookingId);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                // Make room available again
                Room roomOps = new Room();
                roomOps.updateRoomStatus(b.getRoomId(), "available");
                System.out.println("Booking cancelled successfully!");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Checkout - calculate bill and update status
    public void checkOut(int bookingId) {
        Booking b = getBookingById(bookingId);
        if (b == null) {
            System.out.println("Booking not found!");
            return;
        }
        if (!b.getStatus().equals("active")) {
            System.out.println("Booking is already " + b.getStatus());
            return;
        }
        
        // Calculate total amount
        LocalDateTime checkOut = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(b.getCheckIn(), checkOut);
        if (days < 1) days = 1; // Minimum 1 day charge
        
        Room roomOps = new Room();
        double pricePerDay = roomOps.getRoomPrice(b.getRoomId());
        double totalAmount = days * pricePerDay;
        
        String query = "UPDATE BOOKING SET check_out = ?, total_amount = ?, status = 'completed' WHERE booking_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setTimestamp(1, Timestamp.valueOf(checkOut));
            pst.setDouble(2, totalAmount);
            pst.setInt(3, bookingId);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                // Make room available again
                roomOps.updateRoomStatus(b.getRoomId(), "available");
                
                System.out.println("\n====== CHECKOUT BILL ======");
                System.out.println("Booking ID: " + bookingId);
                System.out.println("Room: " + b.getRoomId());
                System.out.println("Check-In: " + b.getCheckIn());
                System.out.println("Check-Out: " + checkOut);
                System.out.println("Days Stayed: " + days);
                System.out.println("Price per Day: Rs." + pricePerDay);
                System.out.println("---------------------------");
                System.out.println("TOTAL AMOUNT: Rs." + totalAmount);
                System.out.println("===========================");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // View booking history (all bookings)
    public void getBookingHistory() {
        String query = "SELECT b.*, c.name, r.room_type FROM BOOKING b " +
                       "JOIN CUSTOMER c ON b.customer_id = c.customer_id " +
                       "JOIN ROOMS r ON b.room_id = r.room_id " +
                       "ORDER BY b.booking_id DESC";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            System.out.println("\n====== BOOKING HISTORY ======");
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("booking_id"));
                System.out.println("Customer: " + rs.getString("name"));
                System.out.println("Room: " + rs.getInt("room_id") + " (" + rs.getString("room_type") + ")");
                System.out.println("Check-In: " + rs.getTimestamp("check_in"));
                Timestamp checkout = rs.getTimestamp("check_out");
                System.out.println("Check-Out: " + (checkout != null ? checkout : "N/A"));
                System.out.println("Amount: Rs." + rs.getDouble("total_amount"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("----------------------");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
