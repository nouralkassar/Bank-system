package transactions;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    public  String id;
    public final String fromAccountId;
    public final String toAccountId;
    public final double amount;
    public final LocalDateTime createdAt;
 // Added for UI / Approval UI
    // ---------------------------
    private String status; // PENDING, APPROVED, REJECTED
    private String stage;  // AUTO, TELLER, MANAGER, COMPLETED
    public Transaction(String from, String to, double amount) {
        this.id = UUID.randomUUID().toString();
        this.fromAccountId = from;
        this.toAccountId = to;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();

          // New default values (do not affect old logic)
        this.status = "PENDING";
        this.stage = "AUTO";
    }
    public String getId() {
        return id;
    }
    
    // -------- NEW GETTERS/SETTERS ----------
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
}
