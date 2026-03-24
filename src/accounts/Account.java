
package accounts;

import accounts.state.AccountState;

public interface Account {
    String getId();
    double getBalance();
    void deposit(double amount);
    void withdraw(double amount);
    String getOwnerName();
    void setState(AccountState state);
    AccountState getState();
}
