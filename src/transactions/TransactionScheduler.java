package transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionScheduler {
    private final List<ScheduledTransaction> scheduled = new ArrayList<>();

    public void schedule(ScheduledTransaction stx) {
        scheduled.add(stx);
    }

    public List<ScheduledTransaction> all() {
        return new ArrayList<>(scheduled);
    }
}
