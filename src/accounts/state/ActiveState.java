package accounts.state;

import accounts.BaseAccount;

public class ActiveState implements AccountState {
    @Override
    public void deposit(BaseAccount acc, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        acc.addBalance(amount);
    }

    @Override
    public void withdraw(BaseAccount acc, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        if (acc.getBalance() < amount)
            throw new RuntimeException("Insufficient funds");
        acc.subtractBalance(amount);
    }

}
