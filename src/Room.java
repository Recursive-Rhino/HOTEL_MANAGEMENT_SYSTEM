import java.sql.*;

public class Room {
    
    // View only available rooms
    public void viewAvailableRooms() {
        String query = "SELECT * FROM ROOMS WHERE status='available'";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            System.out.println("\n====== AVAILABLE ROOMS ======");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Type: " + rs.getString("room_type"));
                System.out.println("Price: Rs." + rs.getDouble("price_per_day") + "/day");
                System.out.println("----------------------");
            }
            if (!found) {
                System.out.println("No available rooms at the moment.");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // View all rooms
    public void viewAllRooms() {
        String query = "SELECT * FROM ROOMS";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            System.out.println("\n====== ALL ROOMS ======");
            while (rs.next()) {
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Type: " + rs.getString("room_type"));
                System.out.println("Price: Rs." + rs.getDouble("price_per_day") + "/day");
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("----------------------");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add new room
    public void addRoom(int roomId, String roomType, double price) {
        String query = "INSERT INTO ROOMS (room_id, room_type, price_per_day, status) VALUES (?, ?, ?, 'available')";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, roomId);
            pst.setString(2, roomType);
            pst.setDouble(3, price);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Room added successfully!");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update room status
    public void updateRoomStatus(int roomId, String status) {
        String query = "UPDATE ROOMS SET status = ? WHERE room_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, status);
            pst.setInt(2, roomId);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Room status updated to: " + status);
            } else {
                System.out.println("Room not found!");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete room
    public void deleteRoom(int roomId) {
        String query = "DELETE FROM ROOMS WHERE room_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, roomId);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Room deleted successfully!");
            } else {
                System.out.println("Room not found!");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get room by ID
    public double getRoomPrice(int roomId) {
        String query = "SELECT price_per_day FROM ROOMS WHERE room_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, roomId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                double price = rs.getDouble("price_per_day");
                con.close();
                return price;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Check if room is available
    public boolean isRoomAvailable(int roomId) {
        String query = "SELECT status FROM ROOMS WHERE room_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, roomId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                boolean available = rs.getString("status").equals("available");
                con.close();
                return available;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
