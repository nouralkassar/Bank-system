package accounts.state;

import accounts.BaseAccount;

public class FrozenState implements AccountState {
    @Override
    public void deposit(BaseAccount acc, double amount) {
        throw new IllegalStateException("Account is frozen: deposits not allowed");
    }

    @Override
    public void withdraw(BaseAccount acc, double amount) {
        throw new IllegalStateException("Account is frozen: withdrawals not allowed");
    }
}
