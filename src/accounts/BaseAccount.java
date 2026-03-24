package accounts;

import accounts.state.*;
import utils.AuditLog;

public abstract class BaseAccount implements Account {

    protected String id;
    protected double balance;
    protected String ownerName;
    protected AccountState state;

    public BaseAccount(String id, String ownerName) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = 0.0;

        // الحالة الافتراضية
        this.state = new ActiveState();
    }

    // -------------------------
    // State handling
    // -------------------------
    public void setState(AccountState state) {
        this.state = state;
        AuditLog.log("STATE CHANGE", id + " -> " + state.getClass().getSimpleName());
    }

    public AccountState getState() {
        return state;
    }

    // -------------------------
    // معلومات الحساب
    // -------------------------
    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    // -------------------------
    // تغيير الحالة (هاي صارت مستخدمة فعليًا)
    // -------------------------
    public void close() {
        setState(new ClosedState());
    }

    public void freeze() {
        setState(new FrozenState());
    }

    public void suspend() {
        setState(new SuspendedState());
    }

    // -------------------------
    // العمليات المالية (المهم!)
    // -------------------------
    @Override
    public void deposit(double amount) {
        state.deposit(this, amount);
        AuditLog.log("Deposit", amount + " -> " + id + " | balance=" + balance);
    }

    @Override
    public void withdraw(double amount) {
        state.withdraw(this, amount);
        AuditLog.log("Withdraw", amount + " <- " + id + " | balance=" + balance);
    }

    // -------------------------
    // يستخدمها الـ State فقط
    // -------------------------
    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void subtractBalance(double amount) {
        this.balance -= amount;
    }
}
