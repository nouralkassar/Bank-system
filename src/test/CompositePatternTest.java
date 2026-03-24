import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import accounts.*;

public class CompositePatternTest {

    @Test
    void testTotalBalanceOfGroup() {
        Account acc1 = new CheckingAccount("C1", "Noor");
        Account acc2 = new CheckingAccount("C2", "Batoul");
        acc1.deposit(1000);
        acc2.deposit(2000);

        AccountGroup group = new AccountGroup("G1");
        group.add(acc1);
        group.add(acc2);

        assertEquals(3000.0, group.getBalance(), "يجب أن يعيد الـ Composite مجموع أرصدة الحسابات الفرعية");
    }

    @Test
    void testDepositDistributionInGroup() {
        Account acc1 = new CheckingAccount("C1", "Noor");
        Account acc2 = new CheckingAccount("C2", "Batoul");
        AccountGroup group = new AccountGroup("G1");
        group.add(acc1);
        group.add(acc2);

        group.deposit(1000);

        assertEquals(500.0, acc1.getBalance());
        assertEquals(500.0, acc2.getBalance());
    }
}