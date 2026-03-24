package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import banking_system.BankFacade;
import transactions.Transaction;

public class ApprovalFrame extends JFrame {

    public ApprovalFrame(List<Transaction> pendingTransactions) {
        setTitle("Pending Approvals");
        setSize(400, 300);
        setLayout(new GridLayout(pendingTransactions.size(), 3, 10, 10));
        setLocationRelativeTo(null);

        for (Transaction tx : pendingTransactions) {
            add(new JLabel("Tx: " + tx.id + " Amount: " + tx.amount));

            JButton btnApprove = new JButton("Approve");
            btnApprove.addActionListener(e -> {
                tx.setStatus("APPROVED");
                BankFacade.getInstance().processPendingTransaction(tx);
                JOptionPane.showMessageDialog(this, "Transaction Approved!");
                dispose();
            });
            add(btnApprove);

            JButton btnReject = new JButton("Reject");
            btnReject.addActionListener(e -> {
                tx.setStatus("REJECTED");
                BankFacade.getInstance().processPendingTransaction(tx);
                JOptionPane.showMessageDialog(this, "Transaction Rejected!");
                dispose();
            });
            add(btnReject);
        }

        setVisible(true);
    }
}
