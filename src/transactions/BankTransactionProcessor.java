package transactions;

import accounts.Account;
import banking_system.AccountRepository;
import utils.AuditLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankTransactionProcessor {

    private final ApprovalHandler approvalChain;
    private final AccountRepository repository;
    private final List<Transaction> history = new ArrayList<>();

    public BankTransactionProcessor(ApprovalHandler chain, AccountRepository repo) {
        this.approvalChain = chain;
        this.repository = repo;
    }

    public final void executeTransfer(String fromId, String toId, double amount) {

        System.out.println("========== PROCESSING TRANSFER ==========");
        System.out.println("FROM: " + fromId + "  TO: " + toId + "  AMOUNT: " + amount);

        validate(fromId, toId, amount);

        Transaction tx = new Transaction(fromId, toId, amount);

        authorize(tx);

        performTransfer(tx);

        log(tx);

        history.add(tx);

        System.out.println("========== TRANSFER COMPLETED ==========\n");
    }

    // ------------------------------
    // STEPS (can be overridden later)
    // ------------------------------

    protected void validate(String fromId, String toId, double amount) {
        System.out.println("[VALIDATION] Checking accounts...");

        if (!repository.exists(fromId) || !repository.exists(toId)) {
            throw new RuntimeException("[VALIDATION ERROR] One or both accounts do NOT exist!");
        }

        Account from = repository.get(fromId);

        if (from.getBalance() < amount) {
            throw new RuntimeException("[VALIDATION ERROR] Insufficient balance!");
        }

        System.out.println("[VALIDATION] OK");
    }

    protected void authorize(Transaction tx) {
        System.out.println("[AUTHORIZATION] Running approval chain...");
        approvalChain.approve(tx);
    }

    protected void performTransfer(Transaction tx) {
        System.out.println("[TRANSFER] Performing debit and credit...");

        Account from = repository.get(tx.fromAccountId);
        Account to = repository.get(tx.toAccountId);

        from.withdraw(tx.amount);
        to.deposit(tx.amount);

        System.out.println("[TRANSFER] Completed.");
    }

    protected void log(Transaction tx) {
        AuditLog.log("Transfer Tx " + tx.id + " processed at " + LocalDateTime.now());
    }

    public void printHistory() {
        System.out.println("\n===== TRANSACTION HISTORY =====");
        for (Transaction tx : history) {
            System.out.println("TX " + tx.id +
                    " | FROM: " + tx.fromAccountId +
                    " | TO: " + tx.toAccountId +
                    " | AMOUNT: " + tx.amount +
                    " | DATE: " + tx.createdAt);
        }
        System.out.println("================================\n");
    }
}
