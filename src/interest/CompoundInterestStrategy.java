package interest;

public class CompoundInterestStrategy implements InterestStrategy {
    private final double rate;
    private final int periodsPerYear;

    public CompoundInterestStrategy(double rate, int periodsPerYear) {
        this.rate = rate;
        this.periodsPerYear = periodsPerYear;
    }

    @Override
    public double calculate(double balance) {
        // return interest for 1 year compounded once (example)
        return balance * (Math.pow(1 + rate / periodsPerYear, periodsPerYear) - 1);
    }
}
