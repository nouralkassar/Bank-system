package accounts.decorator;
import accounts.Account;
import accounts.state.AccountState;
public class PremiumAccountFeature extends AccountDecorator {

    public PremiumAccountFeature(Account wrapped) {
        super(wrapped);
    }

    public void applyCashback(double percentage) {
        double cb = wrapped.getBalance() * (percentage / 100.0);
        wrapped.deposit(cb);
    }

    @Override
    public void deposit(double amount) {
        wrapped.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        wrapped.withdraw(amount);
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
