public class CustomerEntry {
    private int customerId;
    private String name;
    private String phone;
    private String id_proof;

    public CustomerEntry(String name, String phone, String id_proof) {
        this.name = name;
        this.phone = phone;
        this.id_proof = id_proof;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getId_proof() {
        return id_proof;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name + ", Phone: " + phone + ", ID Proof: " + id_proof;
    }
}
