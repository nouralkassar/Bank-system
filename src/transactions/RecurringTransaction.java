package transactions;

import java.time.LocalDateTime;

public class RecurringTransaction {

    public enum Frequency { DAILY, WEEKLY, MONTHLY }

    private final Transaction tx;
    private final Frequency frequency;
    private LocalDateTime nextRun;

    public RecurringTransaction(Transaction tx, Frequency frequency) {
        this.tx = tx;
        this.frequency = frequency;
        this.nextRun = LocalDateTime.now().plusDays(getInterval());
    }

    public boolean shouldRun() {
        return LocalDateTime.now().isAfter(nextRun);
    }

    public Transaction getTransaction() {
        return tx;
    }

    public void updateNextRun() {
        nextRun = nextRun.plusDays(getInterval());
    }

    private int getInterval() {
        return switch (frequency) {
            case DAILY -> 1;
            case WEEKLY -> 7;
            case MONTHLY -> 30;
        };
    }
}
