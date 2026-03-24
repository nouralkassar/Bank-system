package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

import banking_system.BankFacade;
import accounts.*;
import accounts.state.*;

public class ShowAccountsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ShowAccountsFrame() {
        setTitle("All Accounts with Interest / Return");
        setSize(1000, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setStyle();
        initTable();
        initButtons();

        setVisible(true);
    }

    private void initTable() {

        String[] columns = {
                "ID", "Owner", "Balance", "Type",
                "State", "Interest / Return", "Recommendation"
        };

        model = new DefaultTableModel(columns, 0);

        refreshTable();

        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getColumn("Recommendation")
                .setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {

                        Component c = super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);

                        double balance =
                                Double.parseDouble(table.getValueAt(row, 2).toString());

                        if (balance < 100) c.setForeground(Color.RED);
                        else if (balance > 1000) c.setForeground(Color.BLUE);
                        else c.setForeground(Color.BLACK);

                        return c;
                    }
                });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    private void initButtons() {

        JButton btnRemove = new JButton("Remove");
        JButton btnActive = new JButton("Set Active");
        JButton btnFreeze = new JButton("Freeze");
        JButton btnSuspend = new JButton("Suspend");
        JButton btnClose = new JButton("Close");
        JButton btnRefresh = new JButton("Refresh");

        styleButton(btnRemove, new Color(178, 34, 34));
        styleButton(btnActive, new Color(46, 139, 87));
        styleButton(btnFreeze, new Color(70, 130, 180));
        styleButton(btnSuspend, new Color(255, 165, 0));
        styleButton(btnClose, new Color(105, 105, 105));
        styleButton(btnRefresh, new Color(60, 60, 60));
        btnRemove.addActionListener(e -> removeAccount());
        btnActive.addActionListener(e -> changeState(new ActiveState()));
        btnFreeze.addActionListener(e -> changeState(new FrozenState()));
        btnSuspend.addActionListener(e -> changeState(new SuspendedState()));
        btnClose.addActionListener(e -> changeState(new ClosedState()));
        btnRefresh.addActionListener(e -> refreshTable());

        JPanel bottom = new JPanel();
        bottom.add(btnRemove);
        bottom.add(btnActive);
        bottom.add(btnFreeze);
        bottom.add(btnSuspend);
        bottom.add(btnClose);
        bottom.add(btnRefresh);

        add(bottom, BorderLayout.SOUTH);
    }

    private void changeState(AccountState newState) {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an account first");
            return;
        }

        String id = table.getValueAt(row, 0).toString();
        Account acc = BankFacade.getInstance().getAccount(id);

        if (acc instanceof BaseAccount baseAcc) {
            baseAcc.setState(newState);
            refreshTable();
        }
    }

    // -----------------------------------
    // إزالة حساب
    // -----------------------------------
    private void removeAccount() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an account first");
            return;
        }

        String id = table.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove account " + id + "?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            BankFacade.getInstance().removeAccount(id);
            refreshTable();
        }
    }
    private void refreshTable() {

        model.setRowCount(0);

        for (Account acc :
                BankFacade.getInstance().getAccountsMap().values()) {

            double interestOrReturn =
                    BankFacade.getInstance().getInterestOrReturn(acc.getId());

            Object[] row = {
                    acc.getId(),
                    acc.getOwnerName(),
                    acc.getBalance(),
                    acc.getClass().getSimpleName(),
                    acc.getState().getClass().getSimpleName(),
                    String.format(Locale.US, "%.2f", interestOrReturn),
                    getRecommendation(acc.getBalance(), interestOrReturn)
            };

            model.addRow(row);
        }
    }

    private String getRecommendation(double balance, double interest) {
        if (balance < 100)
            return "Deposit regularly to maintain minimum balance.";
        else if (balance > 1000 && interest > 0)
            return "Consider investing more for higher return.";
        else
            return "Balance is healthy.";
    }

    private void styleButton(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }

    private void setStyle() {
        Font f = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", f);
        UIManager.put("Button.font", f);
        UIManager.put("Table.font", f);
    }
}
