package accounts;

import interest.LoanInterestStrategy;

public class LoanAccount extends BaseAccount {
    private LoanInterestStrategy loanInterestStrategy;
    public LoanAccount(String id, String ownerName, interest.LoanInterestStrategy strategy) {
        super(id, ownerName);
        this.loanInterestStrategy = strategy;
    }

    public double calculateInterest() {
        return loanInterestStrategy.calculate(getBalance());
    }
    }



