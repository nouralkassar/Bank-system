package admin;

import transactions.TransactionService;
import customer.TicketManager;
import accounts.Account;

import java.util.List;

public class Dashboard {
    private TransactionService txService;
    private TicketManager ticketManager;
    private List<Account> accounts;

    public Dashboard(TransactionService txService, TicketManager ticketManager, List<Account> accounts) {
        this.txService = txService;
        this.ticketManager = ticketManager;
        this.accounts = accounts;
    }

    public void showSummary() {
        System.out.println("=== Dashboard Summary ===");
        System.out.println("Accounts:");
        accounts.forEach(acc -> System.out.println(acc.getId() + " | Owner: " + acc.getOwnerName() + " | Balance: " + acc.getBalance()));
        System.out.println("\nOpen Tickets:");
        ticketManager.listTickets();
        System.out.println("========================");
    }
}
