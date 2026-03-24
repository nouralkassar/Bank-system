package customer;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class SupportTicket {
    private static int counter = 1;
    private final int id;
    private final String customerName;
    private String message;
    private String status; // Open, In Progress, Closed
    private final LocalDateTime createdAt;

    public SupportTicket(String customerName, String message) {
        this.id = counter++;
        this.customerName = customerName;
        this.message = message;
        this.status = "Open";
        this.createdAt = LocalDateTime.now();
    }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getCustomerName() { return customerName; }
    public int getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Ticket #" + id + " (" + status + ") from " + customerName + ": " + message;
    }


}


