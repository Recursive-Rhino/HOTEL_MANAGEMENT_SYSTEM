import java.sql.Connection;
import java.sql.PreparedStatement;

public class Customer_Entry_DB {
    public void addCustomer(CustomerEntry cust) {
        String query = "INSERT INTO CUSTOMER (name, phone, id_proof) VALUES(?,?,?)";
        try{
            Connection con =DBConnection.getConnection();
            PreparedStatement pst=con.prepareStatement(query);
            int rows=pst.executeUpdate();
            if(rows>0) {
                System.out.println("Data Inserted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
