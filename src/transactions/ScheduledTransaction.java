package transactions;

import java.time.LocalDateTime;

public class ScheduledTransaction {
    private final Transaction tx;
    private final LocalDateTime scheduledAt;

    public ScheduledTransaction(Transaction tx, LocalDateTime when) {
        this.tx = tx;
        this.scheduledAt = when;
    }

    public Transaction getTransaction() { return tx; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
}
