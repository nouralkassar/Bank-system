// package gui;

// import javax.swing.*;
// import java.awt.*;
// import banking_system.BankFacade;

// public class TransferFrame extends JFrame {

//     private JComboBox<String> cmbFrom, cmbTo;
//     private JTextField txtAmount;

//     public TransferFrame() {

//         setTitle("Transfer Money");
//         setSize(400, 250);
//         setLayout(new GridLayout(4, 2, 10, 10));
//         setLocationRelativeTo(null);
//         setStyle();

//         var keys = BankFacade.getInstance()
//                 .getAccountsMap().keySet().toArray(new String[0]);

//         cmbFrom = new JComboBox<>(keys);
//         cmbTo = new JComboBox<>(keys);
//         txtAmount = new JTextField();

//         JButton btnTransfer = new JButton("Transfer");
//         btnTransfer.setBackground(new Color(60, 160, 90));
//         btnTransfer.setForeground(Color.WHITE);

//         add(new JLabel("From Account:"));
//         add(cmbFrom);

//         add(new JLabel("To Account:"));
//         add(cmbTo);

//         add(new JLabel("Amount:"));
//         add(txtAmount);

//         add(new JLabel(""));
//         add(btnTransfer);

//         btnTransfer.addActionListener(e -> transfer());

//         setVisible(true);
//     }

//     private void transfer() {

//         String from = (String) cmbFrom.getSelectedItem();
//         String to = (String) cmbTo.getSelectedItem();
//         String amtTxt = txtAmount.getText().trim();

//         // Validation
//         if (from.equals(to)) {
//             showError("From and To accounts cannot be the same.");
//             return;
//         }

//         if (amtTxt.isEmpty()) {
//             showError("Please enter amount.");
//             return;
//         }

//         double amount;
//         try {
//             amount = Double.parseDouble(amtTxt);
//             if (amount <= 0) {
//                 showError("Amount must be > 0.");
//                 return;
//             }
//         } catch (Exception ex) {
//             showError("Invalid number format.");
//             return;
//         }

//         try {
//             BankFacade.getInstance().transfer(from, to, amount);
//             showSuccess("Transfer Completed!");
//             dispose();
//         } catch (Exception ex) {
//             showError(ex.getMessage());
//         }
//     }

//     private void setStyle() {
//         Font f = new Font("Segoe UI", Font.PLAIN, 14);
//         UIManager.put("Label.font", f);
//         UIManager.put("Button.font", f);
//         UIManager.put("TextField.font", f);
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

public class TransferFrame extends JFrame {

    private JComboBox<String> cmbFrom, cmbTo;
    private JTextField txtAmount;

    private Map<String, String> ownerToId = new LinkedHashMap<>();

    public TransferFrame() {

        setTitle("Transfer Money");
        setSize(400, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null);

        // تعبئة Map أسماء المستخدمين
        for (Account acc : BankFacade.getInstance().getAccountsMap().values()) {
            ownerToId.put(acc.getOwnerName(), acc.getId());
        }

        cmbFrom = new JComboBox<>(ownerToId.keySet().toArray(new String[0]));
        cmbTo   = new JComboBox<>(ownerToId.keySet().toArray(new String[0]));
        txtAmount = new JTextField();

        JButton btnTransfer = new JButton("Transfer");

        add(new JLabel("From (Owner Name):"));
        add(cmbFrom);

        add(new JLabel("To (Owner Name):"));
        add(cmbTo);

        add(new JLabel("Amount:"));
        add(txtAmount);

        add(new JLabel(""));
        add(btnTransfer);

        btnTransfer.addActionListener(e -> transfer());

        setVisible(true);
    }

    private void transfer() {

        String fromOwner = (String) cmbFrom.getSelectedItem();
        String toOwner   = (String) cmbTo.getSelectedItem();

        String amtStr = txtAmount.getText().trim();

        if (fromOwner.equals(toOwner)) {
            showError("Cannot transfer to the same account.");
            return;
        }

        if (amtStr.isEmpty()) {
            showError("Please enter an amount.");
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


String fromId = ownerToId.get(fromOwner);
String toId   = ownerToId.get(toOwner);

try {
    BankFacade.getInstance().transfer(fromId, toId, amount);
    BankFacade.getInstance().saveAccountsToTxt();
    BankFacade.getInstance().saveTransactionsToTxt();

    Account fromAcc = BankFacade.getInstance().getAccount(fromId);
    Account toAcc   = BankFacade.getInstance().getAccount(toId);

    String fromOwnerName = fromAcc.getOwnerName();
    String toOwnerName   = toAcc.getOwnerName();

    utils.AuditLog.log(
        "TRANSFER",
        "From " + fromOwnerName + " to " + toOwnerName + " amount=" + amount
    );
 showSuccess("Transfer Completed!");
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
