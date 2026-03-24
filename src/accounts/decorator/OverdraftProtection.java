package accounts.decorator;
import accounts.Account;
import accounts.state.AccountState;
public class OverdraftProtection extends AccountDecorator {

    public OverdraftProtection(Account wrapped) {
        super(wrapped);
    }

    @Override
    public void deposit(double amount) {
        wrapped.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        if (wrapped.getBalance() + 500 >= amount) {
            wrapped.withdraw(amount);
        } else {
            throw new RuntimeException("Overdraft limit exceeded");
        }
    }


    @Override
    public String getId() {
        return wrapped.getId();
    }

    @Override
    public String getOwnerName() {
        return wrapped.getOwnerName();
    }

    @Override
    public double getBalance() {
        return wrapped.getBalance();
    }

    @Override
    public void setState(AccountState state) {
        wrapped.setState(state);
    }

    @Override
    public AccountState getState() {
        return wrapped.getState();
    }
}
