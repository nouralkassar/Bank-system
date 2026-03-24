package admin;

import transactions.TransactionService;
import utils.AuditLog;

import java.util.List;

public class FullReportsService extends ReportsService {

    public void generateTransactionReport(List<String> txIds, TransactionService txService) {
        System.out.println("=== Transaction Report ===");
        txIds.forEach(id -> {
            System.out.println("Transaction ID: " + id);
            // لو حابب تضيف amount, from, to
        });
    }

    public void generateAuditReport(List<String> auditLogs) {
        System.out.println("=== Audit Log Report ===");
        auditLogs.forEach(System.out::println);
    }
}
