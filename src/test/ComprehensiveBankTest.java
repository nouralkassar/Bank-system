import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import accounts.*;
import interest.*;

public class ComprehensiveBankTest {

    @Test
    void testAllAccountTypes() {
        InterestStrategy simpleInterest = new SimpleInterestStrategy(0.05);
        SavingsAccount savings = new SavingsAccount("S1", "Noor", simpleInterest);
        savings.deposit(1000);
        assertEquals(50, savings.calculateInterest());

        CompoundInterestStrategy compoundInterest = new CompoundInterestStrategy(0.05, 12);
        InvestmentAccount investment = new InvestmentAccount("I1", "Noor", compoundInterest);
        investment.deposit(1000);
        assertTrue(investment.calculateReturn() > 0);

        LoanInterestStrategy loanStrategy = new LoanInterestStrategy(0.1);
        LoanAccount loan = new LoanAccount("L1", "Noor", loanStrategy);
        loan.deposit(5000);
        assertEquals(500, loan.calculateInterest());
    }

    @Test
    void testNegativeScenarios() {
        BaseAccount acc = new CheckingAccount("C1", "Noor");

        assertThrows(IllegalArgumentException.class, () -> acc.deposit(-100));
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(-50));
    }
}