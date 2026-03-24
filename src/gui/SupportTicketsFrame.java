package gui;

import customer.SupportTicket;
import customer.TicketManager;
import utils.AuditLog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SupportTicketsFrame extends JFrame {

    private final TicketManager manager;

    private JTable table;
    private TicketTableModel tableModel;

    private JTextField txtName;
    private JTextField txtMessage;
    private JButton btnCreate;

    private static final String FILE_NAME = "tickets.txt";

    public SupportTicketsFrame(TicketManager manager) {
        this.manager = manager;

        setTitle("Support Tickets Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initForm();
        initTable();
        loadTicketsFromFile();
        refreshTable();

        setVisible(true);
    }

    // -----------------------------
    // FORM (Top Panel)
    // -----------------------------
    private void initForm() {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtName = new JTextField();
        txtMessage = new JTextField();
        btnCreate = new JButton("Create Ticket");

        form.add(new JLabel("Customer Name:"));
        form.add(txtName);

        form.add(new JLabel("Message:"));
        form.add(txtMessage);

        form.add(new JLabel(""));
        form.add(btnCreate);

        btnCreate.addActionListener(e -> createTicket());

        add(form, BorderLayout.NORTH);
    }

    // -----------------------------
    // TABLE (Center Panel)
    // -----------------------------
    private void initTable() {

        tableModel = new TicketTableModel(manager.getTickets());
        table = new JTable(tableModel);

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setDefaultRenderer(Object.class, new TicketColorRenderer());

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // When user clicks status cell → show dropdown
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.getSelectedColumn();
                int row = table.getSelectedRow();
                if (col == 3) { // status column
                    showStatusEditor(row);
                }
            }
        });
    }

    // -----------------------------
    // CREATE NEW TICKET
    // -----------------------------
    private void createTicket() {

        String name = txtName.getText().trim();
        String msg = txtMessage.getText().trim();

        if (name.isEmpty() || msg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        manager.createTicket(name, msg);

        AuditLog.log("TICKET", "Ticket created by " + name);

        saveTicketsToFile();
        refreshTable();

        txtName.setText("");
        txtMessage.setText("");
    }

    // -----------------------------
    // CHANGE STATUS
    // -----------------------------
    private void showStatusEditor(int row) {

        SupportTicket ticket = manager.getTickets().get(row);
        String current = ticket.getStatus();

        String[] options;

        switch (current) {
            case "Open" -> options = new String[]{"Open", "In Progress", "Closed"};
            case "In Progress" -> options = new String[]{"In Progress", "Closed"};
            default -> {
                // Closed → no editing
                return;
            }
        }

        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select Status:",
                "Update Ticket",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                current
        );

        if (newStatus != null && !newStatus.equals(current)) {
            ticket.setStatus(newStatus);

            AuditLog.log("TICKET", "Ticket #" + ticket.getId() + " changed to " + newStatus);

            saveTicketsToFile();
            refreshTable();
        }
    }

    // -----------------------------
    // LOAD FROM FILE
    // -----------------------------
    private void loadTicketsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            manager.getTickets().clear();

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";", 5);
                if (p.length < 5) continue;

                int id = Integer.parseInt(p[0]);
                String name = p[1];
                String msg = p[2];
                String status = p[3];
                LocalDateTime time = LocalDateTime.parse(p[4]);

                SupportTicket t = new SupportTicket(name, msg);

                // override internal auto-id and timestamp safely
                setPrivateField(t, "id", id);
                setPrivateField(t, "status", status);
                setPrivateField(t, "createdAt", time);

                manager.getTickets().add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper to set private fields using reflection
    private void setPrivateField(Object obj, String field, Object value) {
        try {
            var f = obj.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (Exception ignored) {}
    }

    // -----------------------------
    // SAVE TO FILE
    // -----------------------------
    private void saveTicketsToFile() {
        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            for (SupportTicket t : manager.getTickets()) {
                fw.write(
                        t.getId() + ";" +
                        t.getCustomerName() + ";" +
                        t.getMessage() + ";" +
                        t.getStatus() + ";" +
                        t.getCreatedAt() + "\n"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // REFRESH TABLE
    // -----------------------------
    private void refreshTable() {
        tableModel.setData(manager.getTickets());
    }

    // -----------------------------
    // TABLE MODEL
    // -----------------------------
    class TicketTableModel extends AbstractTableModel {

        private final String[] cols = {"ID", "Name", "Message", "Status", "Created At"};
        private List<SupportTicket> data;

        public TicketTableModel(List<SupportTicket> data) {
            this.data = data;
        }

        public void setData(List<SupportTicket> newData) {
            this.data = newData;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() { return data.size(); }

        @Override
        public int getColumnCount() { return cols.length; }

        @Override
        public String getColumnName(int col) { return cols[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            SupportTicket t = data.get(row);

            return switch (col) {
                case 0 -> t.getId();
                case 1 -> t.getCustomerName();
                case 2 -> t.getMessage();
                case 3 -> t.getStatus();
                case 4 -> t.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                default -> "";
            };
        }
    }

    // -----------------------------
    // ROW COLORS
    // -----------------------------
    class TicketColorRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                      boolean focused, int row, int col) {

            Component c = super.getTableCellRendererComponent(table, value, selected, focused, row, col);

            String status = (String) table.getValueAt(row, 3);

            if (!selected) {
                switch (status) {
                    case "Open" -> c.setBackground(new Color(255, 250, 200));
                    case "In Progress" -> c.setBackground(new Color(200, 220, 255));
                    case "Closed" -> c.setBackground(new Color(200, 255, 200));
                }
            } else {
                c.setBackground(c.getBackground().darker());
            }

            return c;
        }
    }
}
