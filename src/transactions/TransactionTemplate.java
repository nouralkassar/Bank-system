package transactions;

import utils.AuditLog;

public abstract class TransactionTemplate {

    // Template Method final to prevent overriding
    public final void execute(Transaction tx) {
        validate(tx);       // Step 1
        authorize(tx);      // Step 2
        perform(tx);        // Step 3
        log(tx);            // Step 4
    }

    // --- STEPS ---

    protected void validate(Transaction tx) {
        // default validation — يمكن تخصيصه في subclasses
        if (tx.amount <= 0) {
            throw new IllegalArgumentException("Invalid amount for tx " + tx.id);
        }
    }

    protected abstract void authorize(Transaction tx);

    protected abstract void perform(Transaction tx);

    protected void log(Transaction tx) {
        AuditLog.log("Transaction executed: " + tx.id + " amount=" + tx.amount);
    }
}
