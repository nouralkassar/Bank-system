// package gui;

// import javax.swing.*;
// import java.awt.*;

// public class MainFrame extends JFrame {

//     public MainFrame() {

//         setTitle("Banking System");
//         setSize(400, 400);
//         setLocationRelativeTo(null);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         setLayout(new GridLayout(5, 1, 10, 10));
//         setStyle();

//         JButton btnCreate = new JButton("Create Account");
//         JButton btnDeposit = new JButton("Deposit Money");
//         JButton btnWithdraw = new JButton("Withdraw Money");
//         JButton btnTransfer = new JButton("Transfer Money");
//         JButton btnShow = new JButton("Show All Accounts");

//         btnCreate.addActionListener(e -> new CreateAccountFrame());
//         btnDeposit.addActionListener(e -> new DepositFrame());
//         btnWithdraw.addActionListener(e -> new WithdrawFrame());
//         btnTransfer.addActionListener(e -> new TransferFrame());
//         btnShow.addActionListener(e -> new ShowAccountsFrame());

//         add(btnCreate);
//         add(btnDeposit);
//         add(btnWithdraw);
//         add(btnTransfer);
//         add(btnShow);

//         setVisible(true);
//     }

//     private void setStyle() {
//         Font f = new Font("Segoe UI", Font.PLAIN, 16);
//         UIManager.put("Button.font", f);
//     }
// }
package gui;

import javax.swing.*;

import banking_system.BankFacade;

import java.awt.*;
import customer.TicketManager;
import transactions.TransactionHistory;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("Banking System");
        setSize(400, 550); // كبرنا شوي
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // عدد الصفوف صار 7 الآن
        setLayout(new GridLayout(6, 1, 10, 10));
        setStyle();

        TicketManager ticketManager = new TicketManager();

        // أزرار العمليات البنكية
        JButton btnCreate = new JButton("Create Account");
        JButton btnDeposit = new JButton("Deposit Money");
        JButton btnWithdraw = new JButton("Withdraw Money");
        JButton btnTransfer = new JButton("Transfer Money");
        JButton btnShow = new JButton("Show All Accounts");
        JButton btnTickets = new JButton("Support Tickets");

        // --- زر Dashboard للّوجز ---
        JButton btnLogs = new JButton("System Logs Dashboard");
        JButton btnApprovals = new JButton("Transaction Approvals");

        // الأحداث لكل زر
        btnCreate.addActionListener(e -> new CreateAccountFrame());
        btnDeposit.addActionListener(e -> new DepositFrame());
        btnWithdraw.addActionListener(e -> new WithdrawFrame());
        btnTransfer.addActionListener(e -> new TransferFrame());
        btnShow.addActionListener(e -> new ShowAccountsFrame());
        btnTickets.addActionListener(e -> new SupportTicketsFrame(ticketManager));

        // الحدث لزر Dashboard
        btnLogs.addActionListener(e -> new LogsDashboardFrame());


        // إضافة الأزرار للواجهة
        add(btnCreate);
        add(btnDeposit);
        add(btnWithdraw);
        add(btnTransfer);
        add(btnShow);
        add(btnTickets);
        add(btnLogs); // ← الزر الجديد

        setVisible(true);
    }

    private void setStyle() {
        Font f = new Font("Segoe UI", Font.PLAIN, 16);
        UIManager.put("Button.font", f);
    }
}
