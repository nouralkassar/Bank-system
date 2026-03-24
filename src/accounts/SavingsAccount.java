package accounts;

import interest.InterestStrategy;

public class SavingsAccount extends BaseAccount {
    private InterestStrategy interestStrategy;


public SavingsAccount(String id, String ownerName, InterestStrategy strategy) {
    super(id, ownerName);
    this.interestStrategy = strategy;
}

    public double calculateInterest() {
        return interestStrategy.calculate(getBalance());
    }
}
