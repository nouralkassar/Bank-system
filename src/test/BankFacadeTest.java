
import static org.junit.jupiter.api.Assertions.*;

import banking_system.BankFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import accounts.*;
import transactions.Transaction;
import interest.SimpleInterestStrategy;

public class BankFacadeTest {

    private BankFacade facade;

    @BeforeEach
    void setUp() {
        facade = BankFacade.getInstance();
        facade.getAccountsMap().clear();
    }

    @Test
    void testAccountAdditionWithDecorators() {
        Account checking = new CheckingAccount("ACC1", "Noor");
        facade.addAccount(checking, false, false);
        assertNotNull(facade.getAccount("ACC1"));

        Account savings = new SavingsAccount("S1", "Batoul", new SimpleInterestStrategy(0.05));
        facade.addAccount(savings, true, true);

        assertNotNull(facade.getAccount("S1"));
        assertEquals(0.0, facade.getAccount("S1").getBalance());
    }

    @Test
    void testTransferAndAudit() {
        Account a1 = new CheckingAccount("A1", "User1");
        Account a2 = new CheckingAccount("A2", "User2");
        a1.deposit(1000.0);

        facade.addAccount(a1, false, false);
        facade.addAccount(a2, false, false);

        facade.transfer("A1", "A2", 400.0);

        assertEquals(600.0, facade.getAccount("A1").getBalance());
        assertEquals(400.0, facade.getAccount("A2").getBalance());

        assertFalse(facade.getTransactions().isEmpty());
    }

    @Test
    void testFileOperations() {
        Account acc = new CheckingAccount("FILE1", "Tester");
        facade.addAccount(acc, false, false);

        assertDoesNotThrow(() -> {
            facade.saveAccountsToTxt();
            facade.saveTransactionsToTxt();
            facade.loadAccountsFromTxt();
        });
    }

    @Test
    void testInterestCalculation() {
        Account savings = new SavingsAccount("SAVE1", "Noor", new SimpleInterestStrategy(0.1));
        savings.deposit(1000.0);
        facade.addAccount(savings, false, false);

        double interest = facade.getInterestOrReturn("SAVE1");
        assertTrue(interest > 0);
    }

    @Test
    void testRemoveAccount() {
        facade.addAccount(new CheckingAccount("DEL1", "User"), false, false);
        facade.removeAccount("DEL1");
        assertNull(facade.getAccount("DEL1"));
    }
}