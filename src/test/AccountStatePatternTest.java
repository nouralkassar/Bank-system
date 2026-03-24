import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import accounts.*;
import accounts.state.*;
public class AccountStatePatternTest {

    @Test
    void testActiveStateOperations() {
        BaseAccount account = new CheckingAccount("ACC001", "Noor");
        assertTrue(account.getState() instanceof ActiveState);

        account.deposit(1000.0);
        assertEquals(1000.0, account.getBalance());

        account.withdraw(400.0);
        assertEquals(600.0, account.getBalance());

        assertThrows(RuntimeException.class, () -> account.withdraw(1000.0));
    }

    @Test
    void testFrozenStateOperations() {
        BaseAccount account = new CheckingAccount("ACC002", "Noor");
        account.deposit(500.0);

        account.freeze();
        assertTrue(account.getState() instanceof FrozenState);

        assertThrows(IllegalStateException.class, () -> account.deposit(100.0));
        assertThrows(IllegalStateException.class, () -> account.withdraw(100.0));
    }

    @Test
    void testSuspendedStateOperations() {
        BaseAccount account = new CheckingAccount("ACC003", "Noor");
        account.deposit(500.0);

        account.suspend();

        account.deposit(200.0);
        assertEquals(700.0, account.getBalance());

        assertThrows(IllegalStateException.class, () -> account.withdraw(100.0));
    }

    @Test
    void testClosedStateOperations() {
        BaseAccount account = new CheckingAccount("ACC004", "Noor");

        account.close();

        assertThrows(IllegalStateException.class, () -> account.deposit(100.0));
        assertThrows(IllegalStateException.class, () -> account.withdraw(100.0));
    }
}