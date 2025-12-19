package view;

import manager.ReportManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportForm extends JPanel {

    private JSpinner dateSpinner;
    private JLabel lblRevenue, lblOrders, lblDate;
    private JTable table;
    private DefaultTableModel model;

    private ReportManager manager = new ReportManager();
    private DecimalFormat df = new DecimalFormat("#,### đ");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ReportForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createFilterPanel(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        loadAllOrders();
    }

    private JPanel createHeader() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(0, 55));
        p.setBackground(new Color(52, 152, 219));
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel lbl = new JLabel("📊 BÁO CÁO DOANH THU");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        p.add(lbl);
        return p;
    }

    private JPanel createFilterPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(280, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("BỘ LỌC THỜI GIAN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));

        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnCheck = styledButton("🔍 Xem theo ngày", new Color(52, 152, 219));
        JButton btnReload = styledButton("🔄 Xem tất cả", new Color(46, 204, 113));
        JButton btnDelete = styledButton("🗑 Xóa ngày chọn", new Color(231, 76, 60));

        gbc.gridy = 0; card.add(title, gbc);
        gbc.gridy = 1; card.add(new JLabel("Chọn ngày báo cáo:"), gbc);
        gbc.gridy = 2; card.add(dateSpinner, gbc);
        gbc.gridy = 3; card.add(new JSeparator(), gbc);
        gbc.gridy = 4; card.add(btnCheck, gbc);
        gbc.gridy = 5; card.add(btnReload, gbc);
        gbc.gridy = 6; card.add(btnDelete, gbc);

        // Events
        btnCheck.addActionListener(e -> onFilterByDate());
        btnReload.addActionListener(e -> loadAllOrders());
        btnDelete.addActionListener(e -> onDeleteByDate());

        return card;
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setOpaque(false);
        p.add(createCards(), BorderLayout.NORTH);
        p.add(createTable(), BorderLayout.CENTER);
        return p;
    }

    private JPanel createCards() {
        JPanel p = new JPanel(new GridLayout(1, 3, 15, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 100));

        lblRevenue = new JLabel("0 đ");
        lblOrders = new JLabel("0");
        lblDate = new JLabel("Tất cả");

        p.add(infoCard("💰 TỔNG DOANH THU", lblRevenue, new Color(46, 204, 113)));
        p.add(infoCard("🧾 TỔNG ĐƠN HÀNG", lblOrders, new Color(52, 152, 219)));
        p.add(infoCard("📅 THỜI GIAN LỌC", lblDate, new Color(155, 89, 182)));

        return p;
    }

    private JPanel infoCard(String title, JLabel value, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(color);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel t = new JLabel(title);
        t.setForeground(new Color(255, 255, 255, 200));
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));

        value.setForeground(Color.WHITE);
        value.setFont(new Font("Segoe UI", Font.BOLD, 24));

        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private JScrollPane createTable() {
        model = new DefaultTableModel(
                new String[]{"Mã đơn hàng", "Mã nhân viên", "Tổng tiền", "Ngày tạo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Number) {
                    setText(df.format(((Number) value).doubleValue()));
                } else {
                    setText(value == null ? "" : value.toString());
                }
            }
        };
        moneyRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(moneyRenderer);

        return new JScrollPane(table);
    }

    private JPanel createFooter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(8, 20, 8, 20));
        JLabel lbl = new JLabel("Hệ thống quản lý nhà hàng v1.0");
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lbl.setForeground(Color.GRAY);
        p.add(lbl, BorderLayout.EAST);
        return p;
    }


    private void loadAllOrders() {
        updateTableData(manager.getAllOrders());
        lblDate.setText("Tất cả");
    }

    private void onFilterByDate() {
        String dateStr = sdf.format((Date) dateSpinner.getValue());
        // Giả sử ReportManager có hàm lấy đơn hàng theo ngày
        // Nếu chưa có, bạn hãy viết thêm hoặc dùng manager.getRevenueByDate(dateStr)
        List<Object[]> list = manager.getAllOrders(); // Tạm lấy tất cả

        // Lọc dữ liệu ngay trên Java nếu Manager chưa hỗ trợ SQL filter
        List<Object[]> filteredList = list.stream()
                .filter(row -> row[3].toString().contains(dateStr))
                .toList();

        updateTableData(filteredList);
        lblDate.setText(dateStr);
    }

    private void updateTableData(List<Object[]> list) {
        model.setRowCount(0);
        double totalRevenue = 0;

        for (Object[] row : list) {
            try {
                // Cách ép kiểu an toàn nhất để tránh lỗi "ko hiện tiền"
                double price = Double.parseDouble(row[2].toString());
                totalRevenue += price;

                model.addRow(new Object[]{
                        row[0],
                        row[1],
                        price, // Đưa kiểu Double vào để Renderer format
                        row[3]
                });
            } catch (Exception e) {
                System.err.println("Lỗi dòng dữ liệu: " + e.getMessage());
            }
        }

        lblOrders.setText(String.valueOf(list.size()));
        lblRevenue.setText(df.format(totalRevenue));
    }

    private void onDeleteByDate() {
        String date = sdf.format((Date) dateSpinner.getValue());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa tất cả đơn hàng ngày " + date + " ?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int deleted = manager.deleteOrdersByDate(date);
            JOptionPane.showMessageDialog(this, "Đã xóa thành công " + deleted + " đơn hàng.");
            loadAllOrders();
        }
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(180, 40));
        return b;
    }
}