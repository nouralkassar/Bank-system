// package gui;

// import javax.swing.*;
// import java.awt.*;
// import banking_system.BankFacade;
// import accounts.Account;

// public class DepositFrame extends JFrame {

//     private JTextField txtAmount;
//     private JComboBox<String> cmbAccounts;

//     public DepositFrame() {
//         setTitle("Deposit");
//         setSize(350, 200);
//         setLayout(new GridLayout(3, 2, 10, 10));
//         setLocationRelativeTo(null);

//         cmbAccounts = new JComboBox<>(BankFacade.getInstance()
//                 .getAccountsMap().keySet().toArray(new String[0]));

//         txtAmount = new JTextField();

//         JButton btnDeposit = new JButton("Deposit");

//         add(new JLabel("Select Account:"));
//         add(cmbAccounts);

//         add(new JLabel("Amount:"));
//         add(txtAmount);

//         add(new JLabel(""));
//         add(btnDeposit);

//         btnDeposit.addActionListener(e -> depositMoney());

//         setVisible(true);
//     }

//     private void depositMoney() {
//         String id = (String) cmbAccounts.getSelectedItem();
//         String amtStr = txtAmount.getText().trim();

//         if (id == null) {
//             JOptionPane.showMessageDialog(this, "No account selected.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }
//         if (amtStr.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Please enter an amount.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         double amount;
//         try {
//             amount = Double.parseDouble(amtStr);
//         } catch (NumberFormatException ex) {
//             JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         Account acc = BankFacade.getInstance().getAccount(id);
//         acc.deposit(amount);

//         JOptionPane.showMessageDialog(this, "Deposit Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
//         this.dispose();
//     }
// }
package gui;

import javax.swing.*;
import java.awt.*;
import banking_system.BankFacade;
import accounts.Account;
import java.util.LinkedHashMap;
import java.util.Map;

public class DepositFrame extends JFrame {

    private JTextField txtAmount;
    private JComboBox<String> cmbAccounts;

    // نعمل خريطة تربط Owner Name → Account ID
    private Map<String, String> ownerToId = new LinkedHashMap<>();

    public DepositFrame() {
        setTitle("Deposit");
        setSize(350, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);

        // تعبئة القائمة بالأسماء فقط
        for (Account acc : BankFacade.getInstance().getAccountsMap().values()) {
            ownerToId.put(acc.getOwnerName(), acc.getId());
        }

        cmbAccounts = new JComboBox<>(ownerToId.keySet().toArray(new String[0]));

        txtAmount = new JTextField();

        JButton btnDeposit = new JButton("Deposit");

        add(new JLabel("Select Owner Name:"));
        add(cmbAccounts);

        add(new JLabel("Amount:"));
        add(txtAmount);

        add(new JLabel(""));
        add(btnDeposit);

        btnDeposit.addActionListener(e -> depositMoney());

        setVisible(true);
    }

    private void depositMoney() {

        String owner = (String) cmbAccounts.getSelectedItem();
        String amtStr = txtAmount.getText().trim();

        if (owner == null) {
            JOptionPane.showMessageDialog(this, "No owner selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (amtStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // نستخرج الـ ID من خلال الاسم
        String accountId = ownerToId.get(owner);

        Account acc = BankFacade.getInstance().getAccount(accountId);
        acc.deposit(amount);
        utils.AuditLog.log("DEPOSIT", "Account " + acc.getOwnerName() + " deposited " + amount);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       BankFacade.getInstance().saveAccountsToTxt();

        JOptionPane.showMessageDialog(this, "Deposit Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
}
