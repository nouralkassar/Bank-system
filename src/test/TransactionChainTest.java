
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import transactions.*;

import java.time.LocalDateTime;

public class TransactionChainTest {

    @Test
    void testChainOfApproval() {
        AutoApprovalHandler auto = new AutoApprovalHandler(1000.0);
        TellerApprovalHandler teller = new TellerApprovalHandler(5000.0);
        ManagerApprovalHandler manager = new ManagerApprovalHandler();

        auto.setNext(teller);
        teller.setNext(manager);

        Transaction t1 = new Transaction("ACC1", "ACC2", 500.0);
        auto.approve(t1);
        assertEquals("APPROVED", t1.getStatus());
        assertEquals("AUTO", t1.getStage());

        Transaction t2 = new Transaction("ACC1", "ACC2", 3000.0);
        auto.approve(t2);
        assertEquals("APPROVED", t2.getStatus());
        assertEquals("TELLER", t2.getStage());

        Transaction t3 = new Transaction("ACC1", "ACC2", 10000.0);
        auto.approve(t3);
        assertEquals("APPROVED", t3.getStatus());
        assertEquals("MANAGER", t3.getStage());
    }

    @Test
    void testScheduledAndRecurring() {
        Transaction tx = new Transaction("A", "B", 100.0);

        ScheduledTransaction scheduled = new ScheduledTransaction(tx, LocalDateTime.now().plusDays(1));
        assertNotNull(scheduled.getScheduledAt());

        RecurringTransaction recurring = new RecurringTransaction(tx, RecurringTransaction.Frequency.DAILY);
        assertNotNull(recurring.getTransaction());
        recurring.updateNextRun();
    }
}