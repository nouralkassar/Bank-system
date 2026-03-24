package accounts.decorator;

import accounts.Account;

public abstract class AccountDecorator implements Account {
    protected Account wrapped;

    public AccountDecorator(Account wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getId() { return wrapped.getId(); }

    @Override
    public double getBalance() { return wrapped.getBalance(); }
}
