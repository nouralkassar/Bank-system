package transactions;

import notifications.NotificationSubject;
import adapters.PaymentGateway;
import utils.AuditLog;

public class EnhancedTransactionService {

    private TransactionTemplate processor;
    private TransactionHistory history;
    private NotificationSubject notifier;
    private PaymentGateway gateway;

    public EnhancedTransactionService(
            TransactionTemplate processor,
            TransactionHistory history,
            NotificationSubject notifier,
            PaymentGateway gateway) {

        this.processor = processor;
        this.history = history;
        this.notifier = notifier;
        this.gateway = gateway;
    }

//    public void process(Transaction tx) {
//
//        processor.execute(tx); // ← TEMPLATE METHOD
//
//        gateway.pay(tx.amount);
//
//        history.add(tx);       // ← NEW
//
//        notifier.notifyAll("Transaction done: " + tx.id);
//
//        AuditLog.log("Enhanced service completed tx " + tx.id);
//    }
}
