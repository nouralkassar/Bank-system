package accounts.state;

import accounts.BaseAccount;

public class ClosedState implements AccountState {
    @Override
    public void deposit(BaseAccount acc, double amount) {
        throw new IllegalStateException("Account is closed");
    }

    @Override
    public void withdraw(BaseAccount acc, double amount) {
        throw new IllegalStateException("Account is closed");
    }
}
