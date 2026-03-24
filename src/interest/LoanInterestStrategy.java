package interest;

public class LoanInterestStrategy implements InterestStrategy {
    private final double annualRate;

    public LoanInterestStrategy(double annualRate) {
        this.annualRate = annualRate;
    }

    @Override
    public double calculate(double balance) {
        // simplified loan interest calculation
        return balance * annualRate;
    }
}
