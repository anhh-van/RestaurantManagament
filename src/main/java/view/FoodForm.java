package view;

import manager.FoodManager;
import model.Food;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FoodForm extends JPanel {

    private JTextField txtId, txtName, txtPrice, txtCategory;
    private JTable table;
    private DefaultTableModel model;
    private FoodManager manager = new FoodManager();

    public FoodForm() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(10,10,10,10));

        // ===== HEADER =====
        add(createHeader(), BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new GridLayout(1,2,15,0));
        main.setOpaque(false);

        main.add(createFormCard());
        main.add(createTableCard());

        add(main, BorderLayout.CENTER);

        loadTable();
    }

    // ================= HEADER =================
    private JPanel createHeader() {
        JPanel header = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(52, 152, 219),
                        getWidth(), 0, new Color(155, 89, 182)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 60));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("🍽 QUẢN LÝ MÓN ĂN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(0,20,0,0));

        header.add(title, BorderLayout.WEST);
        return header;
    }

    // ================= FORM CARD =================
    private JPanel createFormCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(220,220,220),1,true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(12);
        txtId.setEnabled(false);
        txtName = new JTextField(12);
        txtPrice = new JTextField(12);
        txtCategory = new JTextField(12);

        styleField(txtId);
        styleField(txtName);
        styleField(txtPrice);
        styleField(txtCategory);

        addField(card, gbc, "ID", txtId, 0);
        addField(card, gbc, "Tên món", txtName, 1);
        addField(card, gbc, "Giá", txtPrice, 2);
        addField(card, gbc, "Danh mục", txtCategory, 3);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btnPanel.setOpaque(false);

        JButton btnAdd = modernButton("Thêm", new Color(46, 204, 113));
        JButton btnUpdate = modernButton("Sửa", new Color(241, 196, 15));
        JButton btnDelete = modernButton("Xóa", new Color(231, 76, 60));
        JButton btnClear = modernButton("Mới", new Color(52, 152, 219));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        card.add(btnPanel, gbc);

        btnAdd.addActionListener(e -> addFood());
        btnUpdate.addActionListener(e -> updateFood());
        btnDelete.addActionListener(e -> deleteFood());
        btnClear.addActionListener(e -> clearForm());

        return card;
    }

    // ================= TABLE CARD =================
    private JPanel createTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(220,220,220),1,true));

        model = new DefaultTableModel(
                new String[]{"ID","Tên món","Giá","Danh mục"},0
        ){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getSelectionModel().addListSelectionListener(e -> fillForm());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10,10,10,10));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    // ================= HELPER =================
    private void addField(JPanel panel, GridBagConstraints gbc,
                          String label, JComponent field, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200,200,200),1,true),
                new EmptyBorder(5,8,5,8)
        ));
    }

    private JButton modernButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(90,32));
        return btn;
    }

    //  LOGIC
    private void loadTable() {
        model.setRowCount(0);
        List<Food> list = manager.getAllFoods();
        for (Food f : list) {
            model.addRow(new Object[]{
                    f.getId(), f.getName(), f.getPrice(), f.getCategory()
            });
        }
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r == -1) return;

        txtId.setText(model.getValueAt(r,0).toString());
        txtName.setText(model.getValueAt(r,1).toString());
        txtPrice.setText(model.getValueAt(r,2).toString());
        txtCategory.setText(model.getValueAt(r,3).toString());
    }

    private void addFood() {
        try {
            manager.addFood(new Food(
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    txtCategory.getText()
            ));
            loadTable();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Dữ liệu không hợp lệ!");
        }
    }

    private void updateFood() {
        if (txtId.getText().isEmpty()) return;

        try {
            manager.updateFood(new Food(
                    Integer.parseInt(txtId.getText()),
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    txtCategory.getText()
            ));
            loadTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Lỗi cập nhật!");
        }
    }

    private void deleteFood() {
        if (txtId.getText().isEmpty()) return;

        int id = Integer.parseInt(txtId.getText());
        manager.deleteFood(id);
        loadTable();
        clearForm();
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtCategory.setText("");
        table.clearSelection();
    }
}
