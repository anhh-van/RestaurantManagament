package view;

import manager.FoodManager;
import manager.OrderManager;
import model.Food;
import model.OrderDetail;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderForm extends JPanel {

    private JComboBox<Food> cbFood;
    private JComboBox<Integer> cbTable;
    private JTextField txtPrice, txtQty;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;

    private List<OrderDetail> cart = new ArrayList<>();

    public OrderForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createLeftPanel(), BorderLayout.WEST);
        add(createTable(), BorderLayout.CENTER);
        add(createBottom(), BorderLayout.SOUTH);

        loadFoods();
    }

    private JPanel createHeader() {
        JPanel p = new JPanel();
        p.setBackground(new Color(52, 152, 219));
        p.setPreferredSize(new Dimension(0, 55));

        JLabel lbl = new JLabel("🧾 QUẢN LÝ ĐẶT MÓN");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));

        p.add(lbl);
        return p;
    }

    private JPanel createLeftPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(300, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbFood = new JComboBox<>();
        cbTable = new JComboBox<>();
        txtPrice = new JTextField();
        txtQty = new JTextField();

        txtPrice.setEditable(false);

        for (int i = 1; i <= 20; i++) cbTable.addItem(i);

        JButton btnAdd = styledButton("➕ Thêm món", new Color(46, 204, 113));

        gbc.gridy = 0;
        card.add(new JLabel("Món ăn"), gbc);
        gbc.gridy = 1;
        card.add(cbFood, gbc);

        gbc.gridy = 2;
        card.add(new JLabel("Giá"), gbc);
        gbc.gridy = 3;
        card.add(txtPrice, gbc);

        gbc.gridy = 4;
        card.add(new JLabel("Số lượng"), gbc);
        gbc.gridy = 5;
        card.add(txtQty, gbc);

        gbc.gridy = 6;
        card.add(new JLabel("Số bàn"), gbc);
        gbc.gridy = 7;
        card.add(cbTable, gbc);

        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(btnAdd, gbc);

        cbFood.addActionListener(e -> {
            Food f = (Food) cbFood.getSelectedItem();
            if (f != null) txtPrice.setText(String.valueOf(f.getPrice()));
        });

        btnAdd.addActionListener(e -> addFoodToCart());

        return card;
    }

    private JScrollPane createTable() {
        model = new DefaultTableModel(
                new String[]{"#", "Món", "SL", "Giá", "Thành tiền"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);

        // Căn phải cột Giá và Thành tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        return new JScrollPane(table);
    }

    private JPanel createBottom() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblTotal = new JLabel("Tổng: 0 đ");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(231, 76, 60));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);

        JButton btnDelete = styledButton("🗑 Xóa", new Color(231, 76, 60));
        JButton btnSave = styledButton("💾 Lưu đơn", new Color(52, 152, 219));

        btnDelete.addActionListener(e -> deleteSelected());
        btnSave.addActionListener(e -> saveOrder());

        btns.add(btnDelete);
        btns.add(btnSave);

        p.add(lblTotal, BorderLayout.EAST);
        p.add(btns, BorderLayout.WEST);

        return p;
    }

    private void loadFoods() {
        List<Food> foods = FoodManager.getAllFoods();
        cbFood.removeAllItems();
        for (Food f : foods) cbFood.addItem(f);

        if (!foods.isEmpty())
            txtPrice.setText(String.valueOf(foods.get(0).getPrice()));
    }

    private void addFoodToCart() {
        try {
            Food f = (Food) cbFood.getSelectedItem();
            int qty = Integer.parseInt(txtQty.getText());

            if (f == null || qty <= 0) throw new Exception();

            double total = qty * f.getPrice();
            cart.add(new OrderDetail(f.getId(), qty, f.getPrice()));

            model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    f.getName(),
                    qty,
                    f.getPrice(),
                    total
            });

            updateTotal();
            txtQty.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        cart.remove(row);
        model.removeRow(row);

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }

        updateTotal();
    }

    private void updateTotal() {
        double sum = 0;
        for (OrderDetail d : cart) {
            sum += d.getQty() * d.getPrice();
        }
        lblTotal.setText("Tổng: " + sum + " đ");
    }

    private void saveOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }

        int tableNo = (int) cbTable.getSelectedItem();

        double totalAmount = 0;
        for (OrderDetail d : cart) {
            totalAmount += d.getQty() * d.getPrice();
        }

        if (OrderManager.saveOrder(cart, tableNo, totalAmount)) {
            JOptionPane.showMessageDialog(this, "Lưu đơn thành công!");
            cart.clear();
            model.setRowCount(0);
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu đơn thất bại! Kiểm tra Console để xem lỗi.");
        }
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(140, 35));
        return b;
    }
}
