import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import accounts.*;
import accounts.decorator.*;
import accounts.state.*;

public class DecoratorPatternDetailedTest {

    @Test
    void testFullDecoratorChainAndMethods() {
        // 1. إنشاء حساب أساسي
        BaseAccount base = new CheckingAccount("D100", "Noor");
        base.deposit(200.0);

        // 2. تغليفه بـ OverdraftProtection
        OverdraftProtection overdraft = new OverdraftProtection(base);

        // --- اختبار كافة الدوال لرفع الـ Coverage ---
        assertEquals("D100", overdraft.getId());
        assertEquals("Noor", overdraft.getOwnerName());
        assertEquals(200.0, overdraft.getBalance());
        assertTrue(overdraft.getState() instanceof ActiveState);

        // اختبار السحب ضمن حدود الـ Overdraft (200 رصيد + 500 حماية = 700 متاح)
        overdraft.withdraw(600.0);
        assertEquals(-400.0, overdraft.getBalance());

        // اختبار تغيير الحالة من خلال الـ Decorator
        overdraft.setState(new FrozenState());
        assertTrue(overdraft.getState() instanceof FrozenState);
    }

    @Test
    void testPremiumFeatureCoverage() {
        BaseAccount base = new CheckingAccount("P200", "Batoul");
        base.deposit(1000.0);

        // تغليف الحساب بميزة الـ Premium
        PremiumAccountFeature premium = new PremiumAccountFeature(base);

        // اختبار وظيفة الكاش باك
        premium.applyCashback(5.0); // 5% من 1000 هي 50
        assertEquals(1050.0, premium.getBalance());

        // اختبار الإيداع والسحب من خلال الـ Premium Decorator
        premium.deposit(100.0);
        assertEquals(1150.0, premium.getBalance());

        premium.withdraw(150.0);
        assertEquals(1000.0, premium.getBalance());
    }

    @Test
    void testOverdraftExceptionCoverage() {
        Account base = new CheckingAccount("E300", "Admin");
        OverdraftProtection overdraft = new OverdraftProtection(base);

        // اختبار حالة الخطأ (سحب أكثر من 500 والرصيد 0) لرفع نسبة تغطية الـ catch/throw
        assertThrows(RuntimeException.class, () -> {
            overdraft.withdraw(600.0);
        });
    }
}