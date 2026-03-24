import interest.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InterestStrategyTest {

    @Test
    void testCompoundInterestCalculation() {
        double balance = 1000.0;
        double rate = 0.10;
        int periods = 2;
        CompoundInterestStrategy strategy = new CompoundInterestStrategy(rate, periods);

        double interest = strategy.calculate(balance);


        assertEquals(102.5, interest, 0.001, "حساب الفائدة المركبة غير صحيح!");
    }

    @Test
    void testCompoundInterestIsHigherThanSimple() {
        double balance = 1000.0;
        double rate = 0.10;

        CompoundInterestStrategy compound = new CompoundInterestStrategy(rate, 4);
        double compoundRes = compound.calculate(balance);

        double simpleRes = balance * rate;

        assertTrue(compoundRes > simpleRes, "الفائدة المركبة يجب أن تكون أكبر من البسيطة");
    }
}