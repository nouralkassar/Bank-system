// package gui;

// import javax.swing.*;
// import java.awt.*;
// import banking_system.BankFacade;
// import accounts.Account;

// public class WithdrawFrame extends JFrame {

//     private JTextField txtAmount;
//     private JComboBox<String> cmbAccounts;

//     public WithdrawFrame() {
//         setTitle("Withdraw");
//         setSize(350, 200);
//         setLayout(new GridLayout(3, 2, 10, 10));
//         setLocationRelativeTo(null);

//         setStyle();

//         cmbAccounts = new JComboBox<>(
//                 BankFacade.getInstance().getAccountsMap().keySet().toArray(new String[0])
//         );

//         txtAmount = new JTextField();

//         JButton btnWithdraw = new JButton("Withdraw");
//         btnWithdraw.setBackground(new Color(220, 80, 80));
//         btnWithdraw.setForeground(Color.WHITE);

//         add(new JLabel("Select Account:"));
//         add(cmbAccounts);

//         add(new JLabel("Amount:"));
//         add(txtAmount);

//         add(new JLabel(""));
//         add(btnWithdraw);

//         btnWithdraw.addActionListener(e -> withdrawMoney());

//         setVisible(true);
//     }

//     private void withdrawMoney() {
//         String id = (String) cmbAccounts.getSelectedItem();
//         String amtStr = txtAmount.getText().trim();

//         // Validation
//         if (id == null || id.isEmpty()) {
//             showError("Please select account.");
//             return;
//         }
//         if (amtStr.isEmpty()) {
//             showError("Please enter an amount.");
//             return;
//         }

//         double amount;

//         try {
//             amount = Double.parseDouble(amtStr);
//             if (amount <= 0) {
//                 showError("Amount must be greater than 0.");
//                 return;
//             }
//         } catch (Exception ex) {
//             showError("Invalid number format.");
//             return;
//         }

//         Account acc = BankFacade.getInstance().getAccount(id);

//         try {
//             acc.withdraw(amount);
//             showSuccess("Withdraw successful!");
//             dispose();
//         } catch (Exception ex) {
//             showError(ex.getMessage());
//         }
//     }

//     private void setStyle() {
//         Font font = new Font("Segoe UI", Font.PLAIN, 14);
//         UIManager.put("Label.font", font);
//         UIManager.put("Button.font", font);
//         UIManager.put("TextField.font", font);
//     }

//     private void showError(String msg) {
//         JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
//     }

//     private void showSuccess(String msg) {
//         JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
//     }
// }
package gui;

import javax.swing.*;
import java.awt.*;
import banking_system.BankFacade;
import accounts.Account;
import java.util.LinkedHashMap;
import java.util.Map;

public class WithdrawFrame extends JFrame {

    private JTextField txtAmount;
    private JComboBox<String> cmbOwners;
    private Map<String, String> ownerToId = new LinkedHashMap<>();

    public WithdrawFrame() {
        setTitle("Withdraw");
        setSize(350, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);

        // تعبئة Map اسم → رقم
        for (Account acc : BankFacade.getInstance().getAccountsMap().values()) {
            ownerToId.put(acc.getOwnerName(), acc.getId());
        }

        cmbOwners = new JComboBox<>(ownerToId.keySet().toArray(new String[0]));
        txtAmount = new JTextField();

        JButton btnWithdraw = new JButton("Withdraw");

        add(new JLabel("Select Owner Name:"));
        add(cmbOwners);

        add(new JLabel("Amount:"));
        add(txtAmount);

        add(new JLabel(""));
        add(btnWithdraw);

        btnWithdraw.addActionListener(e -> withdrawMoney());

        setVisible(true);
    }

    private void withdrawMoney() {

        String owner = (String) cmbOwners.getSelectedItem();
        String amtStr = txtAmount.getText().trim();

        if (owner == null) {
            showError("Please select account.");
            return;
        }

        if (amtStr.isEmpty()) {
            showError("Enter an amount.");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amtStr);
            if (amount <= 0) {
                showError("Amount must be > 0.");
                return;
            }
        } catch (Exception ex) {
            showError("Invalid amount format.");
            return;
        }

        // استرجاع ID الحساب من خلال اسم صاحبه
        String accountId = ownerToId.get(owner);

        try {
            Account acc = BankFacade.getInstance().getAccount(accountId);
            acc.withdraw(amount);
           // ===============================================
           BankFacade.getInstance().saveAccountsToTxt();
        utils.AuditLog.log("WITHDRAW", "Account " + acc.getOwnerName() + " withdrew " + amount);
  
            showSuccess("Withdraw Successful!");
            dispose();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
