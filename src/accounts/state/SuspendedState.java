package accounts.state;

import accounts.BaseAccount;

public class SuspendedState implements AccountState {

    @Override
    public void deposit(BaseAccount acc, double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        acc.addBalance(amount);
    }

    @Override
    public void withdraw(BaseAccount acc, double amount) {
        throw new IllegalStateException(
                "Account suspended: withdrawals are blocked"
        );
    }
}
