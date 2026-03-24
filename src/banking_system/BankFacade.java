//package banking_system;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import accounts.Account;
//import adapters.PaymentGateway;
//import adapters.LegacyPaymentAdapter;
//import adapters.LegacyPaymentAPI;
//import notifications.NotificationSubject;
//import notifications.NotificationObserver;
//import transactions.AutoApprovalHandler;
//import transactions.ManagerApprovalHandler;
//import transactions.TellerApprovalHandler;
//import transactions.Transaction;
//import transactions.TransactionService;
//import transactions.ApprovalHandler;
//import utils.AuditLog;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class BankFacade {
//
//    private final TransactionService txService;
//    private final NotificationSubject notifier;
//    private final ApprovalHandler approvalChain;
//    public final Map<String, Account> accounts = new HashMap<>();
//    private static BankFacade instance;
//
//    public static BankFacade getInstance() {
//        if (instance == null) {
//            instance = new BankFacade();
//        }
//        return instance;
//    }
//
//    public Map<String, Account> getAccountsMap() {
//        return accounts;
//    }
//
//    public BankFacade() {
//        notifier = new NotificationSubject();
//        LegacyPaymentAPI legacyAPI = new LegacyPaymentAPI();
//        PaymentGateway gateway = new LegacyPaymentAdapter(legacyAPI);
//        AutoApprovalHandler auto = new AutoApprovalHandler(1000.0);
//        TellerApprovalHandler teller = new TellerApprovalHandler(10000.0);
//        ManagerApprovalHandler manager = new ManagerApprovalHandler();
//        auto.setNext(teller);
//        teller.setNext(manager);
//        approvalChain = auto;
//        txService = new TransactionService(approvalChain, notifier, gateway);
//    }
//
//    public void registerNotifier(NotificationObserver obs) {
//        notifier.addObserver(obs);
//    }
//    public void addAccount(Account account) {
//        accounts.put(account.getId(), account);
//    }
//
//public void transfer(String fromId, String toId, double amount) {
//    Account from = accounts.get(fromId);
//    Account to = accounts.get(toId);
//
//    if (from == null || to == null)
//        throw new RuntimeException("Invalid account: fromId or toId does not exist.");
//
//    Transaction tx = new Transaction(fromId, toId, amount);
//    addTransaction(tx);
//    AuditLog.log("Facade initiating transfer " + tx.getId());
//    txService.process(tx);
//    if (tx.getStatus().equals("APPROVED")) {
//        from.withdraw(amount);
//        to.deposit(amount);
//        AuditLog.log("Transfer completed for tx " + tx.id);
//    } else {
//        AuditLog.log("Transfer not approved for tx " + tx.id);
//    }
//}
//
//    public void approveTransaction(transactions.Transaction tx) {
//    try {
//        approvalChain.approve(tx);
//        AuditLog.log("APPROVAL", "Transaction approved by facade: " + tx.id);
//    } catch (Exception e) {
//        AuditLog.log("ERROR", "Approval failed for tx " + tx.id + ": " + e.getMessage());
//        throw e;
//    }
//}
//
//    private final List<Transaction> transactions = new ArrayList<>();
//
//    public void addTransaction(Transaction tx) {
//    transactions.add(tx);
//}
//
//    public List<Transaction> getTransactions() {
//    return transactions;
//}
//
//    public Transaction findTransaction(String id) {
//    for (Transaction t : transactions) {
//        if (t.id.equals(id)) return t;
//    }
//    return null;
//}
//
//     public void saveTransactionsToTxt() {
//    try (FileWriter fw = new FileWriter("transactions.txt")) {
//        for (Transaction t : transactions) {
//            fw.write(
//                    t.id + ";" +
//                    t.fromAccountId + ";" +
//                    t.toAccountId + ";" +
//                    t.amount + ";" +
//                    t.getStage() + ";" +
//                    t.getStatus() + ";" +
//                    t.createdAt + "\n"
//            );
//        }
//        fw.flush();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
//     public void loadTransactionsFromTxt() {
//    File f = new File("transactions.txt");
//    if (!f.exists()) return;
//
//    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] p = line.split(";");
//            if (p.length < 7) continue;
//
//            Transaction t = new Transaction(p[1], p[2], Double.parseDouble(p[3]));
//            t.id = p[0];
//            t.setStage(p[4]);
//            t.setStatus(p[5]);
//            transactions.add(t);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
//     public Account getAccount(String accountId) {
//        return accounts.get(accountId);
//    }
//     public void saveAccountsToTxt() {
//      try (FileWriter fw = new FileWriter("accounts.txt")) {
//        for (Account acc : accounts.values()) {
//            String type = acc.getClass().getSimpleName();
//            double rate = 0.0;
//            try {
//                if (acc instanceof accounts.SavingsAccount) {
//                    // assuming method getInterestStrategy exists and returning SimpleInterestStrategy with getRate()
//                    // if your classes differ, adapt accordingly (or store 0)
//                    // Here we attempt reflection-free safe cast if you provided getter
//                    // If you don't have getter, leave rate=0
//                }
//            } catch (Exception ignored) {}
//
//            // Compose line: id;owner;balance;type;rate
//            String line = acc.getId() + ";" +
//                          acc.getOwnerName() + ";" +
//                          acc.getBalance() + ";" +
//                          type + ";" +
//                          rate + "\n";
//            fw.write(line);
//           }
//           fw.flush();
//           System.out.println("Accounts saved to accounts.txt");
//          } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//
//     public void loadAccountsFromTxt() {
//    try {
//        File file = new File("accounts.txt");
//        if (!file.exists()) return;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            accounts.clear();
//            String line;
//            while ((line = br.readLine()) != null) {
//                if (line.trim().isEmpty()) continue;
//                String[] parts = line.split(";");
//                if (parts.length < 4) continue;
//                String id = parts[0];
//                String owner = parts[1];
//                double balance = 0.0;
//                try { balance = Double.parseDouble(parts[2]); } catch (Exception ignored) {}
//                String type = parts[3];
//                double rate = 0.0;
//                if (parts.length >= 5) {
//                    try { rate = Double.parseDouble(parts[4]); } catch (Exception ignored) {}
//                }
//
//                Account acc = null;
//                switch (type) {
//                    case "SavingsAccount":
//                        acc = new accounts.SavingsAccount(id, owner, new interest.SimpleInterestStrategy(rate));
//                        break;
//                    case "CheckingAccount":
//                        acc = new accounts.CheckingAccount(id, owner);
//                        break;
//                    case "LoanAccount":
//                        acc = new accounts.LoanAccount(id, owner, new interest.LoanInterestStrategy(rate));
//                        break;
//                    case "InvestmentAccount":
//                        acc = new accounts.InvestmentAccount(id, owner, new interest.SimpleInterestStrategy(rate));
//                        break;
//                    default:
//                        continue;
//                }
//
//                acc.setState(new accounts.state.ActiveState());
//                if (balance != 0.0) acc.deposit(balance);
//
//                accounts.put(id, acc);
//            }
//        }
//
//        System.out.println("Accounts loaded from accounts.txt");
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
//    public void processPendingTransaction(Transaction tx) {
//        if (tx.getStatus().equals("APPROVED")) {
//            Account from = accounts.get(tx.fromAccountId);
//            Account to   = accounts.get(tx.toAccountId);
//            from.withdraw(tx.amount);
//            to.deposit(tx.amount);
//            AuditLog.log("Processed pending tx " + tx.id);
//        } else {
//            AuditLog.log("Rejected pending tx " + tx.id);
//        }
//
//        saveAccountsToTxt();
//        saveTransactionsToTxt();
//    }
//
//}
package banking_system;

import java.io.*;
import java.util.*;

import accounts.Account;
import accounts.CheckingAccount;
import accounts.InvestmentAccount;
import accounts.LoanAccount;
import accounts.SavingsAccount;
import accounts.decorator.OverdraftProtection;
import accounts.decorator.PremiumAccountFeature;
import adapters.PaymentGateway;
import adapters.LegacyPaymentAdapter;
import adapters.LegacyPaymentAPI;
import notifications.NotificationObserver;
import notifications.NotificationSubject;
import transactions.*;
import utils.AuditLog;
import interest.SimpleInterestStrategy;
import interest.LoanInterestStrategy;

public class BankFacade {

    private final TransactionService txService;
    private final NotificationSubject notifier;
    private final ApprovalHandler approvalChain;
    public final Map<String, Account> accounts = new HashMap<>();
    private static BankFacade instance;

    public static BankFacade getInstance() {
        if (instance == null) {
            instance = new BankFacade();
        }
        return instance;
    }

    public Map<String, Account> getAccountsMap() {
        return accounts;
    }

    public BankFacade() {
        notifier = new NotificationSubject();
        LegacyPaymentAPI legacyAPI = new LegacyPaymentAPI();
        PaymentGateway gateway = new LegacyPaymentAdapter(legacyAPI);
        AutoApprovalHandler auto = new AutoApprovalHandler(1000.0);
        TellerApprovalHandler teller = new TellerApprovalHandler(10000.0);
        ManagerApprovalHandler manager = new ManagerApprovalHandler();
        auto.setNext(teller);
        teller.setNext(manager);
        approvalChain = auto;
        txService = new TransactionService(approvalChain, notifier, gateway);
    }

    public void registerNotifier(NotificationObserver obs) {
        notifier.addObserver(obs);
    }

    // -------------------------------
    // إضافة الحساب مع إمكانية تغليفه بـ Decorator
    // -------------------------------
    public void addAccount(Account account, boolean isOverdraft, boolean isPremium) {
        if (isOverdraft) {
            account = new OverdraftProtection(account);
        }
        if (isPremium) {
            account = new PremiumAccountFeature(account);
        }
        accounts.put(account.getId(), account);
    }

    // -------------------------------
    // العمليات المالية
    // -------------------------------
    public void transfer(String fromId, String toId, double amount) {
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);

        if (from == null || to == null)
            throw new RuntimeException("Invalid account: fromId or toId does not exist.");

        Transaction tx = new Transaction(fromId, toId, amount);
        addTransaction(tx);
        AuditLog.log("Facade initiating transfer " + tx.getId());
        txService.process(tx);
        if (tx.getStatus().equals("APPROVED")) {
            from.withdraw(amount);
            to.deposit(amount);
            AuditLog.log("Transfer completed for tx " + tx.id);
        } else {
            AuditLog.log("Transfer not approved for tx " + tx.id);
        }
    }

    public void approveTransaction(Transaction tx) {
        try {
            approvalChain.approve(tx);
            AuditLog.log("APPROVAL", "Transaction approved by facade: " + tx.id);
        } catch (Exception e) {
            AuditLog.log("ERROR", "Approval failed for tx " + tx.id + ": " + e.getMessage());
            throw e;
        }
    }

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Transaction findTransaction(String id) {
        for (Transaction t : transactions) {
            if (t.id.equals(id)) return t;
        }
        return null;
    }

    // -------------------------------
    // حفظ وتحميل العمليات
    // -------------------------------
    public void saveTransactionsToTxt() {
        try (FileWriter fw = new FileWriter("transactions.txt")) {
            for (Transaction t : transactions) {
                fw.write(
                        t.id + ";" +
                                t.fromAccountId + ";" +
                                t.toAccountId + ";" +
                                t.amount + ";" +
                                t.getStage() + ";" +
                                t.getStatus() + ";" +
                                t.createdAt + "\n"
                );
            }
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTransactionsFromTxt() {
        File f = new File("transactions.txt");
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length < 7) continue;

                Transaction t = new Transaction(p[1], p[2], Double.parseDouble(p[3]));
                t.id = p[0];
                t.setStage(p[4]);
                t.setStatus(p[5]);
                transactions.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    // -------------------------------
    // حفظ وتحميل الحسابات مع Decorator
    // -------------------------------
    public void saveAccountsToTxt() {
        try (FileWriter fw = new FileWriter("accounts.txt")) {
            for (Account acc : accounts.values()) {
                String type = acc.getClass().getSimpleName();
                double rate = 0.0;

                // يمكن تعديل هنا إذا أردت تخزين المعدل من InterestStrategy
                if (acc instanceof SavingsAccount) {
                    // rate = ((SavingsAccount) acc).getInterestStrategy().getRate();
                }

                String line = acc.getId() + ";" +
                        acc.getOwnerName() + ";" +
                        acc.getBalance() + ";" +
                        type + ";" +
                        rate + "\n";
                fw.write(line);
            }
            fw.flush();
            System.out.println("Accounts saved to accounts.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAccountsFromTxt() {
        try {
            File file = new File("accounts.txt");
            if (!file.exists()) return;

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                accounts.clear();
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(";");
                    if (parts.length < 4) continue;
                    String id = parts[0];
                    String owner = parts[1];
                    double balance = 0.0;
                    try { balance = Double.parseDouble(parts[2]); } catch (Exception ignored) {}
                    String type = parts[3];
                    double rate = 0.0;
                    if (parts.length >= 5) {
                        try { rate = Double.parseDouble(parts[4]); } catch (Exception ignored) {}
                    }

                    Account acc = null;
                    switch (type) {
                        case "SavingsAccount":
                            acc = new SavingsAccount(id, owner, new SimpleInterestStrategy(rate));
                            break;
                        case "CheckingAccount":
                            acc = new CheckingAccount(id, owner);
                            break;
                        case "LoanAccount":
                            acc = new LoanAccount(id, owner, new LoanInterestStrategy(rate));
                            break;
                        case "InvestmentAccount":
                            acc = new InvestmentAccount(id, owner, new SimpleInterestStrategy(rate));
                            break;
                        default:
                            continue;
                    }

                    acc.setState(new accounts.state.ActiveState());
                    if (balance != 0.0) acc.deposit(balance);

                    // -------------------------------
                    // مثال: تطبيق Decorator بعد التحميل
                    // -------------------------------
                    // يمكن هنا إضافة شروطك لتغليف الحساب
                    // acc = new OverdraftProtection(acc);
                    // acc = new PremiumAccountFeature(acc);

                    accounts.put(id, acc);
                }
            }

            System.out.println("Accounts loaded from accounts.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------
    // معالجة العمليات المعلقة
    // -------------------------------
    public void processPendingTransaction(Transaction tx) {
        if (tx.getStatus().equals("APPROVED")) {
            Account from = accounts.get(tx.fromAccountId);
            Account to = accounts.get(tx.toAccountId);
            from.withdraw(tx.amount);
            to.deposit(tx.amount);
            AuditLog.log("Processed pending tx " + tx.id);
        } else {
            AuditLog.log("Rejected pending tx " + tx.id);
        }

        saveAccountsToTxt();
        saveTransactionsToTxt();
    }
    public void removeAccount(String accountId) {
        Account acc = accounts.get(accountId);
        if (acc != null) {
            // إذا الحساب جزء من مجموعة، ممكن تتصرفي حسب المنطق (هنا بس نحذفه من الماب)
            accounts.remove(accountId);
            AuditLog.log("ACCOUNT", "Removed account " + accountId);
            saveAccountsToTxt();
        } else {
            throw new RuntimeException("Account not found: " + accountId);
        }
    }

    public double getInterestOrReturn(String accountId) {
        Account acc = accounts.get(accountId);
        if (acc == null) return 0;

        if (acc instanceof SavingsAccount sa) {
            return sa.calculateInterest();
        } else if (acc instanceof LoanAccount la) {
            return la.calculateInterest();
        } else if (acc instanceof InvestmentAccount ia) {
            return ia.calculateReturn();
        }

        return 0;
    }

}
