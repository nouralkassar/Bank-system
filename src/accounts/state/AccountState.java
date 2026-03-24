package accounts.state;

import accounts.BaseAccount;

public interface AccountState {
    void deposit(BaseAccount acc, double amount);
    void withdraw(BaseAccount acc, double amount);
}
