package view;

import manager.FoodManager;
import manager.OrderManager;
import model.Food;
import model.OrderDetail;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderForm extends JPanel {

    private JComboBox<Food> cbFood;
    private JTextField txtPrice, txtQty;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;

    private List<OrderDetail> cart = new ArrayList<>();

    public OrderForm() {
        setLayout(new BorderLayout(10,10));
        setBackground(new Color(240,242,245));
        setBorder(new EmptyBorder(10,10,10,10));

        add(createHeader(), BorderLayout.NORTH);
        add(createLeftPanel(), BorderLayout.WEST);
        add(createTable(), BorderLayout.CENTER);
        add(createBottom(), BorderLayout.SOUTH);

        loadFoods();
    }

    // ================= HEADER =================
    private JPanel createHeader() {
        JPanel p = new JPanel();
        p.setBackground(new Color(52,152,219));
        p.setPreferredSize(new Dimension(0,55));

        JLabel lbl = new JLabel("🧾 QUẢN LÝ ĐẶT MÓN");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));

        p.add(lbl);
        return p;
    }

    // ================= LEFT =================
    private JPanel createLeftPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(300,0));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbFood = new JComboBox<>();
        txtPrice = new JTextField();
        txtQty = new JTextField();
        txtPrice.setEditable(false);

        JButton btnAdd = styledButton("➕ Thêm món", new Color(46,204,113));

        addField(card, gbc, "Món ăn", cbFood, 0);
        addField(card, gbc, "Giá", txtPrice, 1);
        addField(card, gbc, "Số lượng", txtQty, 2);

        gbc.gridy = 3;
        card.add(btnAdd, gbc);

        cbFood.addActionListener(e -> {
            Food f = (Food) cbFood.getSelectedItem();
            if (f != null) txtPrice.setText(String.valueOf(f.getPrice()));
        });

        btnAdd.addActionListener(e -> addFoodToCart());

        return card;
    }

    // ================= TABLE =================
    private JScrollPane createTable() {
        model = new DefaultTableModel(
                new String[]{"Món", "SL", "Giá", "Thành tiền"}, 0
        );
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41,128,185));
        table.getTableHeader().setForeground(Color.WHITE);

        return new JScrollPane(table);
    }

    // ================= BOTTOM =================
    private JPanel createBottom() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10,20,10,20));

        lblTotal = new JLabel("Tổng: 0 đ");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(231,76,60));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        btns.setOpaque(false);

        JButton btnDelete = styledButton("🗑 Xóa", new Color(231,76,60));
        JButton btnSave = styledButton("💾 Lưu đơn", new Color(52,152,219));

        btnDelete.addActionListener(e -> deleteSelected());
        btnSave.addActionListener(e -> saveOrder());

        btns.add(btnDelete);
        btns.add(btnSave);

        p.add(lblTotal, BorderLayout.WEST);
        p.add(btns, BorderLayout.EAST);

        return p;
    }

    // ================= LOGIC =================
    private void loadFoods() {
        List<Food> foods = FoodManager.getAllFoods();
        for (Food f : foods) cbFood.addItem(f);
        if (!foods.isEmpty())
            txtPrice.setText(String.valueOf(foods.get(0).getPrice()));
    }

    private void addFoodToCart() {
        try {
            Food f = (Food) cbFood.getSelectedItem();
            int qty = Integer.parseInt(txtQty.getText());

            if (qty <= 0) throw new Exception();

            double total = qty * f.getPrice();
            cart.add(new OrderDetail(f.getId(), qty, f.getPrice()));

            model.addRow(new Object[]{
                    f.getName(), qty, f.getPrice(), total
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
        updateTotal();
    }

    private void updateTotal() {
        double sum = 0;
        for (OrderDetail d : cart)
            sum += d.getQty() * d.getPrice();

        lblTotal.setText("Tổng: " + sum + " đ");
    }

    private void saveOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }

        if (OrderManager.saveOrder(cart)) {
            JOptionPane.showMessageDialog(this, "Lưu đơn thành công!");
            cart.clear();
            model.setRowCount(0);
            updateTotal();
        }
    }

    // ================= UI HELPER =================
    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent c, int y) {
        gbc.gridy = y;
        p.add(new JLabel(label), gbc);
        gbc.gridy = y + 1;
        p.add(c, gbc);
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(120,35));
        return b;
    }
}
