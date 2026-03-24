package accounts;

import interest.InterestStrategy;

public class InvestmentAccount extends BaseAccount {
    private InterestStrategy strategy;

public InvestmentAccount(String id, String ownerName, InterestStrategy strategy) {
    super(id, ownerName);
    this.strategy = strategy;
}
    public double calculateReturn() {
        return strategy.calculate(getBalance());
    }

}
