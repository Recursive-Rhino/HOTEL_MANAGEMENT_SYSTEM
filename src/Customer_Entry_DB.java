import java.sql.*;

public class Customer_Entry_DB {
    
    // Add new customer
    public int addCustomer(CustomerEntry cust) {
        String query = "INSERT INTO CUSTOMER (name, p_number, id_proof) VALUES (?, ?, ?)";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, cust.getName());
            pst.setString(2, cust.getPhone());
            pst.setString(3, cust.getId_proof());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pst.getGeneratedKeys();
                if (keys.next()) {
                    int customerId = keys.getInt(1);
                    System.out.println("Customer added successfully! ID: " + customerId);
                    con.close();
                    return customerId;
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // View all customers
    public void getAllCustomers() {
        String query = "SELECT * FROM CUSTOMER";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            System.out.println("\n====== ALL CUSTOMERS ======");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Phone: " + rs.getString("p_number"));
                System.out.println("ID Proof: " + rs.getString("id_proof"));
                System.out.println("----------------------");
            }
            if (!found) {
                System.out.println("No customers found.");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get customer by ID
    public CustomerEntry getCustomerById(int customerId) {
        String query = "SELECT * FROM CUSTOMER WHERE customer_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                CustomerEntry cust = new CustomerEntry(
                    rs.getString("name"),
                    rs.getString("p_number"),
                    rs.getString("id_proof")
                );
                cust.setCustomerId(rs.getInt("customer_id"));
                con.close();
                return cust;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Search customer by phone
    public void searchCustomerByPhone(String phone) {
        String query = "SELECT * FROM CUSTOMER WHERE p_number LIKE ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "%" + phone + "%");
            ResultSet rs = pst.executeQuery();
            
            System.out.println("\n====== SEARCH RESULTS ======");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Phone: " + rs.getString("p_number"));
                System.out.println("ID Proof: " + rs.getString("id_proof"));
                System.out.println("----------------------");
            }
            if (!found) {
                System.out.println("No customers found with phone: " + phone);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete customer
    public void deleteCustomer(int customerId) {
        String query = "DELETE FROM CUSTOMER WHERE customer_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, customerId);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Customer not found!");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if customer exists
    public boolean customerExists(int customerId) {
        String query = "SELECT customer_id FROM CUSTOMER WHERE customer_id = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            boolean exists = rs.next();
            con.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
