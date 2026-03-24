
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import adapters.LegacyPaymentAPI;
import adapters.LegacyPaymentAdapter;
import adapters.PaymentGateway;
import notifications.NotificationSubject;
import transactions.*;

import java.time.LocalDateTime;

public class TransactionLogicTest {

    private TransactionService service;
    private TransactionHistory history;
    private TransactionScheduler scheduler;
    private ApprovalHandler chain;

    @BeforeEach
    void setUp() {
        chain = new AutoApprovalHandler(1000.0);
        NotificationSubject notifier = new NotificationSubject();
        PaymentGateway gateway = new LegacyPaymentAdapter(new LegacyPaymentAPI());

        service = new TransactionService(chain, notifier, gateway);
        history = new TransactionHistory();
        scheduler = new TransactionScheduler();
    }

    @Test
    void testTransactionHistoryMethods() {
        Transaction tx = new Transaction("ACC1", "ACC2", 500.0);
        history.add(tx);

        assertNotNull(history.findById(tx.getId()));
        assertNull(history.findById("WRONG_ID"));

        history.updateStatus(tx.getId(), "APPROVED");
        history.updateStage(tx.getId(), "COMPLETED");

        assertEquals("APPROVED", tx.getStatus());
        assertEquals("COMPLETED", tx.getStage());
        assertEquals(1, history.getAll().size());
    }

    @Test
    void testTransactionServiceProcess() {
        Transaction tx = new Transaction("ACC1", "ACC2", 200.0);

        assertDoesNotThrow(() -> service.process(tx));
    }

    @Test
    void testTransactionScheduler() {
        Transaction tx = new Transaction("A", "B", 100.0);
        ScheduledTransaction stx = new ScheduledTransaction(tx, LocalDateTime.now());

        scheduler.schedule(stx);
        assertEquals(1, scheduler.all().size());
    }

    @Test
    void testTransactionTemplateValidation() {

        Transaction badTx = new Transaction("A", "B", -50.0);

        TransactionTemplate template = new TransactionTemplate() {
            @Override protected void authorize(Transaction tx) {}
            @Override protected void perform(Transaction tx) {}
        };

        assertThrows(IllegalArgumentException.class, () -> template.execute(badTx));

        Transaction goodTx = new Transaction("A", "B", 100.0);
        assertDoesNotThrow(() -> template.execute(goodTx));
    }
}