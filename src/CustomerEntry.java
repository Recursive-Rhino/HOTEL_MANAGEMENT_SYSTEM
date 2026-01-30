public class CustomerEntry {
    private String name;
    private String phone;
    private String id_proof;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getId_proof() {
        return id_proof;
    }

    public CustomerEntry(String name, String phone, String id_proof) {
        this.name = name;
        this.phone = phone;
        this.id_proof = id_proof;
    }
}
