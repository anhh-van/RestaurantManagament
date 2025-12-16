package view;

import manager.ReportManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportForm extends JPanel {

    private JTextField txtDate;
    private JLabel lblResult;
    private JTable table;
    private DefaultTableModel model;
    private ReportManager manager = new ReportManager();

    public ReportForm() {
        setLayout(new BorderLayout(10,10));
        setBackground(new Color(240,242,245));
        setBorder(new EmptyBorder(10,10,10,10));

        add(createHeader(), BorderLayout.NORTH);
        add(createFilterPanel(), BorderLayout.WEST);
        add(createTable(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        loadOrders();
    }

    // ================= HEADER =================
    private JPanel createHeader() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(0,55));
        p.setBackground(new Color(52,152,219));

        JLabel lbl = new JLabel("📊 BÁO CÁO DOANH THU");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));

        p.add(lbl);
        return p;
    }

    // ================= FILTER =================
    private JPanel createFilterPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(320,0));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDate = new JTextField();
        txtDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnCheck  = styledButton("🔍 Xem doanh thu", new Color(52,152,219));
        JButton btnDelete = styledButton("🗑 Xóa theo ngày", new Color(231,76,60));
        JButton btnReload = styledButton("🔄 Tải lại", new Color(46,204,113));

        gbc.gridy = 0;
        card.add(new JLabel("Ngày (YYYY-MM-DD)"), gbc);

        gbc.gridy = 1;
        card.add(txtDate, gbc);

        gbc.gridy = 2;
        card.add(btnCheck, gbc);

        gbc.gridy = 3;
        card.add(btnDelete, gbc);

        gbc.gridy = 4;
        card.add(btnReload, gbc);

        btnCheck.addActionListener(e -> onCheck());
        btnDelete.addActionListener(e -> onDeleteByDate());
        btnReload.addActionListener(e -> loadOrders());

        return card;
    }

    // ================= TABLE =================
    private JScrollPane createTable() {
        model = new DefaultTableModel(
                new String[]{"Order ID", "Employee ID", "Tổng tiền", "Ngày tạo"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41,128,185));
        table.getTableHeader().setForeground(Color.WHITE);

        return new JScrollPane(table);
    }

    // ================= FOOTER =================
    private JPanel createFooter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10,20,10,20));

        lblResult = new JLabel("Tổng: 0 đ");
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResult.setForeground(new Color(231,76,60));

        p.add(lblResult, BorderLayout.WEST);
        return p;
    }

    // ================= LOGIC =================
    private void onCheck() {
        String date = txtDate.getText().trim();

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                    "Ngày không hợp lệ! (YYYY-MM-DD)",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = manager.getRevenueByDate(date);
        lblResult.setText("Doanh thu ngày " + date + ": " + total + " đ");
    }

    private void onDeleteByDate() {
        String date = txtDate.getText().trim();

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                    "Ngày không hợp lệ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa tất cả đơn ngày " + date + " ?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        int deleted = manager.deleteOrdersByDate(date);
        JOptionPane.showMessageDialog(this, "Đã xóa " + deleted + " đơn.");
        loadOrders();
    }

    private void loadOrders() {
        model.setRowCount(0);
        List<String[]> list = manager.getAllOrders();
        for (String[] r : list) model.addRow(r);
        lblResult.setText("Tổng bản ghi: " + list.size());
    }

    // ================= STYLE =================
    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(180,35));
        return b;
    }
}
