package interest;

public class SimpleInterestStrategy implements InterestStrategy {
    private final double rate;

    public SimpleInterestStrategy(double rate) {
        this.rate = rate;
    }

    @Override
    public double calculate(double balance) {
        return balance * rate;
    }
}
