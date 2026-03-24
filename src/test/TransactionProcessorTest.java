
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import accounts.*;
import banking_system.AccountRepository;
import transactions.*;

public class TransactionProcessorTest {

    private BankTransactionProcessor processor;
    private AccountRepository repo;
    private ApprovalHandler chain;

    @BeforeEach
    void setUp() {
        repo = new AccountRepository();
        repo.add(new CheckingAccount("ACC1", "Noor"));
        repo.add(new CheckingAccount("ACC2", "Batoul"));

        repo.get("ACC1").deposit(2000.0);

        chain = new AutoApprovalHandler(1000.0);
        chain.setNext(new TellerApprovalHandler(5000.0));

        processor = new BankTransactionProcessor(chain, repo);
    }

    @Test
    void testSuccessfulExecution() {

        assertDoesNotThrow(() -> {
            processor.executeTransfer("ACC1", "ACC2", 500.0);
        });

        assertEquals(1500.0, repo.get("ACC1").getBalance());
        assertEquals(500.0, repo.get("ACC2").getBalance());
    }

    @Test
    void testValidationError_InsufficientFunds() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            processor.executeTransfer("ACC1", "ACC2", 5000.0);
        });

        assertTrue(exception.getMessage().contains("Insufficient balance"));
    }

    @Test
    void testValidationError_NonExistentAccount() {
        assertThrows(RuntimeException.class, () -> {
            processor.executeTransfer("ACC1", "NON_EXISTENT", 100.0);
        });
    }

    @Test
    void testPrintHistory() {
        processor.executeTransfer("ACC1", "ACC2", 100.0);
        assertDoesNotThrow(() -> processor.printHistory());
    }
}