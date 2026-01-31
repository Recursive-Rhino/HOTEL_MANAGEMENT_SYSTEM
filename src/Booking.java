import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private int roomId;
    private int customerId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private double totalAmount;
    private String status;

    public Booking(int roomId, int customerId, LocalDateTime checkIn) {
        this.roomId = roomId;
        this.customerId = customerId;
        this.checkIn = checkIn;
        this.status = "active";
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + ", Room: " + roomId + ", Customer: " + customerId +
               ", Check-In: " + checkIn + ", Check-Out: " + (checkOut != null ? checkOut : "N/A") +
               ", Amount: Rs." + totalAmount + ", Status: " + status;
    }
}
