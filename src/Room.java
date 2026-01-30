import java.sql.*;

public class Room {
    public void viewRoom() {
        String query="SELECT * FROM ROOMS where status='available'";
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement pst= con.prepareStatement(query);
            ResultSet rs=pst.executeQuery();
            while (rs.next()) {
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Type: " + rs.getString("room_type"));
                System.out.println("Price: " + rs.getDouble("price_per_day"));
                System.out.println("----------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
