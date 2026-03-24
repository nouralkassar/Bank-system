package gui;

import utils.AuditLog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class LogsDashboardFrame extends JFrame {

    private JPanel summaryPanel;
    private JPanel filterPanel;
    private JPanel tablePanel;

    private JComboBox<String> cmbTypes;
    private JTextField txtSearch;
    private JButton btnRefresh;
    private JButton btnClear;

    private JTable logsTable;
    private LogsTableModel tableModel;

    public LogsDashboardFrame() {

        setTitle("System Logs Dashboard");
        setSize(900, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // ===== 1) Summary Panel =====
        summaryPanel = new JPanel();
        summaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        add(summaryPanel, BorderLayout.NORTH);

        // ===== 2) Filter Panel =====
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        cmbTypes = new JComboBox<>(new String[]{
                "All", "INFO", "ERROR", "TRANSFER", "DEPOSIT", "WITHDRAW", "ACCOUNT", "TICKET"
        });

        txtSearch = new JTextField(20);
        btnRefresh = new JButton("Refresh");
        btnClear = new JButton("Clear Logs");

        filterPanel.add(new JLabel("Filter by Type:"));
        filterPanel.add(cmbTypes);

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(txtSearch);

        filterPanel.add(btnRefresh);
        filterPanel.add(btnClear);

        add(filterPanel, BorderLayout.SOUTH);

        // ===== 3) Table Panel =====
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        add(tablePanel, BorderLayout.CENTER);

        // تحميل البيانات لأول مرة
        loadSummary();
        initTable();
        loadTable();

        // Actions
        btnRefresh.addActionListener(e -> {
            loadSummary();
            loadTable();
        });

        btnClear.addActionListener(e -> {
            AuditLog.clear();
            loadSummary();
            loadTable();
        });

        cmbTypes.addActionListener(e -> loadTable());

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { loadTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { loadTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { loadTable(); }
        });

        setVisible(true);
    }

    // --------------------------
    //  SUMMARY SECTION
    // --------------------------
    private void loadSummary() {
        summaryPanel.removeAll();

        Map<String, Integer> counts = AuditLog.countByType();

        addBadge("INFO", counts.getOrDefault("INFO", 0), new Color(70, 120, 200));
        addBadge("ERROR", counts.getOrDefault("ERROR", 0), new Color(200, 70, 70));
        addBadge("TRANSFER", counts.getOrDefault("TRANSFER", 0), new Color(80, 170, 80));
        addBadge("DEPOSIT", counts.getOrDefault("DEPOSIT", 0), new Color(180, 140, 50));
        addBadge("WITHDRAW", counts.getOrDefault("WITHDRAW", 0), new Color(140, 90, 200));
        addBadge("ACCOUNT", counts.getOrDefault("ACCOUNT", 0), new Color(100, 160, 180));
        addBadge("TICKET", counts.getOrDefault("TICKET", 0), new Color(220, 120, 40));

        summaryPanel.revalidate();
        summaryPanel.repaint();
    }

    private void addBadge(String title, int count, Color c) {
        JPanel badge = new JPanel();
        badge.setBackground(c);
        badge.setPreferredSize(new Dimension(120, 50));
        badge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lbl = new JLabel(title + ": " + count);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        badge.add(lbl);
        summaryPanel.add(badge);
    }

    // --------------------------
    //  TABLE SECTION
    // --------------------------
    private void initTable() {
        tableModel = new LogsTableModel(AuditLog.getLogs());
        logsTable = new JTable(tableModel);

        logsTable.setRowHeight(25);
        logsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        logsTable.setDefaultRenderer(Object.class, new LogColorRenderer());

        JScrollPane scroll = new JScrollPane(logsTable);
        tablePanel.add(scroll, BorderLayout.CENTER);
    }

    private void loadTable() {

        String selectedType = (String) cmbTypes.getSelectedItem();
        String searchText = txtSearch.getText().trim().toLowerCase();

        List<AuditLog.LogEntry> logs = AuditLog.getLogs();

        java.util.List<AuditLog.LogEntry> filtered = new java.util.ArrayList<>();

        for (AuditLog.LogEntry e : logs) {

            boolean typeMatch = selectedType.equals("All") || e.getType().equals(selectedType);
            boolean textMatch = searchText.isEmpty()
                    || e.getMessage().toLowerCase().contains(searchText)
                    || e.getType().toLowerCase().contains(searchText);

            if (typeMatch && textMatch) {
                filtered.add(e);
            }
        }

        tableModel.setData(filtered);
    }

    // =============================
    //        TABLE MODEL
    // =============================
    class LogsTableModel extends javax.swing.table.AbstractTableModel {

        private java.util.List<AuditLog.LogEntry> data;
        private final String[] columns = {"Time", "Type", "Message"};

        public LogsTableModel(java.util.List<AuditLog.LogEntry> data) {
            this.data = data;
        }

        public void setData(java.util.List<AuditLog.LogEntry> newData) {
            this.data = newData;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            AuditLog.LogEntry e = data.get(row);

            return switch (col) {
                case 0 -> e.getFormattedTime();
                case 1 -> e.getType();
                case 2 -> e.getMessage();
                default -> "";
            };
        }
    }

    // =============================
    //     COLOR RENDERER
    // =============================
    class LogColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String type = (String) table.getValueAt(row, 1);

            Color bg = Color.WHITE;

            switch (type) {
                case "INFO" -> bg = new Color(220, 230, 250);
                case "ERROR" -> bg = new Color(255, 200, 200);
                case "TRANSFER" -> bg = new Color(210, 255, 210);
                case "DEPOSIT" -> bg = new Color(255, 240, 200);
                case "WITHDRAW" -> bg = new Color(235, 220, 255);
                case "ACCOUNT" -> bg = new Color(220, 245, 250);
                case "TICKET" -> bg = new Color(255, 230, 200);
            }

            if (isSelected) {
                c.setBackground(bg.darker());
            } else {
                c.setBackground(bg);
            }

            return c;
        }
    }
}
