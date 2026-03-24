package gui;

import javax.swing.*;
import java.awt.*;
import banking_system.BankFacade;
import accounts.*;
import accounts.state.ActiveState;
import interest.SimpleInterestStrategy;
import interest.LoanInterestStrategy;

public class CreateAccountFrame extends JFrame {

    private JTextField txtId, txtOwner, txtRate;
    private JComboBox<String> cmbType;

    public CreateAccountFrame() {

        setTitle("Create Account");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2, 10, 10));
        setLocationRelativeTo(null);
        setStyle();

        cmbType = new JComboBox<>(new String[]{
                "Savings",
                "Checking",
                "Loan",
                "Investment"
        });

        txtId = new JTextField();
        txtOwner = new JTextField();
        txtRate = new JTextField();
        JButton btnRemove = new JButton("Remove Account");
        JButton btnCreate = new JButton("Create");
        btnCreate.setBackground(new Color(60, 130, 220));
        btnCreate.setForeground(Color.WHITE);

        add(new JLabel("Account Type:"));
        add(cmbType);

        add(new JLabel("Account ID:"));
        add(txtId);

        add(new JLabel("Owner Name:"));
        add(txtOwner);

        add(new JLabel("Interest Rate:"));
        add(txtRate);

        add(new JLabel(""));
        add(btnCreate);


        cmbType.addActionListener(e -> toggleRate());

        btnCreate.addActionListener(e -> createAccount());

        toggleRate(); // إخفاء خانة الفائدة حسب النوع

        setVisible(true);
    }

    private void toggleRate() {
        String type = (String) cmbType.getSelectedItem();
        boolean show = type.equals("Savings") || type.equals("Investment") || type.equals("Loan");
        txtRate.setEnabled(show);
    }

    private void createAccount() {

        String type = (String) cmbType.getSelectedItem();
        String id = txtId.getText().trim();
        String owner = txtOwner.getText().trim();
        String rateTxt = txtRate.getText().trim();

        // Validation
        if (id.isEmpty() || owner.isEmpty()) {
            showError("Please fill all required fields.");
            return;
        }

        double rate = 0;

        if (txtRate.isEnabled()) {
            try {
                rate = Double.parseDouble(rateTxt);
                if (rate <= 0) {
                    showError("Rate must be > 0.");
                    return;
                }
            } catch (Exception ex) {
                showError("Invalid rate format.");
                return;
            }
        }

        Account acc = null;

        switch (type) {
            case "Savings" ->
                    acc = new SavingsAccount(id, owner, new SimpleInterestStrategy(rate));
            case "Checking" ->
                    acc = new CheckingAccount(id, owner);
            case "Loan" ->
                    acc = new LoanAccount(id, owner, new LoanInterestStrategy(rate));
            case "Investment" ->
                    acc = new InvestmentAccount(id, owner, new SimpleInterestStrategy(rate));
        }

        acc.setState(new ActiveState());
        BankFacade.getInstance().addAccount(acc, false, false);
        BankFacade.getInstance().saveAccountsToTxt();
        utils.AuditLog.log("ACCOUNT", "Created account " + id + " for " + owner);

        showSuccess("Account Created Successfully!");
        dispose();
    }

    private void setStyle() {
        Font f = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", f);
        UIManager.put("Button.font", f);
        UIManager.put("TextField.font", f);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
