package transactions;

import adapters.PaymentGateway;
import utils.AuditLog;
import notifications.NotificationSubject;

public class TransactionService {
    private ApprovalHandler approvalChain;
    private NotificationSubject notifier;
    private PaymentGateway paymentGateway;

    public TransactionService(ApprovalHandler chain, NotificationSubject notifier, PaymentGateway gateway) {
        this.approvalChain = chain;
        this.notifier = notifier;
        this.paymentGateway = gateway;
    }

    public void process(Transaction tx) {
        approvalChain.approve(tx);
        // here you would perform debit/credit on accounts (omitted)
        boolean paid = paymentGateway.pay(tx.amount);
        AuditLog.log("Processed tx " + tx.id + " paid=" + paid);
       // notifier.notifyAll("Transaction " + tx.id + " processed.");
    }
}
//package transactions;
//
//import adapters.PaymentGateway;
//import utils.AuditLog;
//import notifications.NotificationSubject;
//import banking_system.BankFacade;
//import accounts.Account;
//
//public class TransactionService {
//    private ApprovalHandler approvalChain;
//    private NotificationSubject notifier;
//    private PaymentGateway paymentGateway;
//
//    public TransactionService(ApprovalHandler chain, NotificationSubject notifier, PaymentGateway gateway) {
//        this.approvalChain = chain;
//        this.notifier = notifier;
//        this.paymentGateway = gateway;
//    }
//
//public void process(Transaction tx) {
//    Account from = BankFacade.getInstance().getAccount(tx.fromAccountId);
//
//    // تحقق الرصيد قبل الموافقة (مكرر للحماية)
//    if (from.getBalance() < tx.amount) {
//        tx.setStatus("REJECTED");
//        tx.setStage("AUTO");
//        AuditLog.log("Transaction rejected due to insufficient balance: " + tx.id);
//        notifier.notifyObservers("Transaction rejected: " + tx.id);
//        return;
//    }
//    approvalChain.approve(tx);
//    if (tx.getStatus().equals("APPROVED")) {
//        Account to = BankFacade.getInstance().getAccount(tx.toAccountId);
//        from.withdraw(tx.amount);
//        to.deposit(tx.amount);
//
//        boolean paid = paymentGateway.pay(tx.amount);
//        AuditLog.log("Processed tx " + tx.id + " paid=" + paid);
//        notifier.notifyObservers("Transaction approved and processed: " + tx.id);
//    } else {
//        AuditLog.log("Transaction not approved: " + tx.id);
//        notifier.notifyObservers("Transaction not approved: " + tx.id);
//    }
//}
//
//}
