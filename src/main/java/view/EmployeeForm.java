package view;

import manager.EmployeeManager;
import model.Employee;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class EmployeeForm extends JPanel {

    private JTextField txtId, txtName, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JTable table;
    private DefaultTableModel model;

    private final Color BG = new Color(236, 240, 241);
    private final Color CARD = Color.WHITE;
    private final Color PRIMARY = new Color(33, 150, 243);
    private final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private EmployeeManager manager = new EmployeeManager();

    public EmployeeForm() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(BG);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);

        loadTable();
    }

    /*  HEADER  */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("👥 QUẢN LÝ NHÂN VIÊN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        header.add(title, BorderLayout.WEST);
        return header;
    }

    /*  CONTENT  */
    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(BG);

        content.add(createFormCard(), BorderLayout.WEST);
        content.add(createTableCard(), BorderLayout.CENTER);

        return content;
    }

    /*  FORM CARD  */
    private JPanel createFormCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(320, 0));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lbl = new JLabel("Thông tin nhân viên");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));

        card.add(lbl, BorderLayout.NORTH);
        card.add(createForm(), BorderLayout.CENTER);
        card.add(createButtons(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridLayout(5, 1, 8, 8));
        form.setBackground(CARD);

        txtId = createField(false);
        txtName = createField(true);
        txtUsername = createField(true);
        txtPassword = new JPasswordField();
        txtPassword.setFont(FONT);
        cbRole = new JComboBox<>(new String[]{"Quản Lý", "Bồi Bàn", "Đầu Bếp"});
        cbRole.setFont(FONT);

        form.add(createRow("ID", txtId));
        form.add(createRow("Tên", txtName));
        form.add(createRow("Username", txtUsername));
        form.add(createRow("Password", txtPassword));
        form.add(createRow("Chức vụ", cbRole));

        return form;
    }

    private JPanel createButtons() {
        JPanel btns = new JPanel(new GridLayout(2, 2, 8, 8));
        btns.setBackground(CARD);
        btns.setBorder(new EmptyBorder(15, 0, 0, 0));

        btns.add(createButton("Thêm", e -> addEmp()));
        btns.add(createButton("Sửa", e -> updateEmp()));
        btns.add(createButton("Xóa", e -> deleteEmp()));
        btns.add(createButton("Làm mới", e -> clearForm()));

        return btns;
    }

    // TABLE CARD
    private JPanel createTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        model = new DefaultTableModel(
                new String[]{"ID", "Tên", "Chức vụ", "Username"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(FONT);
        table.setSelectionBackground(new Color(187, 222, 251));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 245, 245));

        table.getSelectionModel().addListSelectionListener(e -> fillForm());

        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void loadTable() {
        model.setRowCount(0);
        List<Employee> list = manager.getAll();
        for (Employee e : list) {
            model.addRow(new Object[]{
                    e.getId(),
                    e.getName(),
                    e.getRole(),
                    e.getUsername()
            });
        }
    }

    private void addEmp() {
        if (txtName.getText().isEmpty() || txtUsername.getText().isEmpty()) return;

        Employee e = new Employee();
        e.setName(txtName.getText());
        e.setUsername(txtUsername.getText());
        e.setPassword(new String(txtPassword.getPassword()));
        e.setRole(cbRole.getSelectedItem().toString());

        if (manager.insert(e)) {
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        }
    }

    private void updateEmp() {
        if (txtId.getText().isEmpty()) return;

        Employee e = new Employee();
        e.setId(Integer.parseInt(txtId.getText()));
        e.setName(txtName.getText());
        e.setUsername(txtUsername.getText());
        e.setPassword(new String(txtPassword.getPassword()));
        e.setRole(cbRole.getSelectedItem().toString());

        if (manager.update(e)) {
            loadTable();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        }
    }

    private void deleteEmp() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        if (manager.delete(id)) {
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Đã xóa!");
        }
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r == -1) return;

        txtId.setText(model.getValueAt(r, 0).toString());
        txtName.setText(model.getValueAt(r, 1).toString());
        cbRole.setSelectedItem(model.getValueAt(r, 2).toString());
        txtUsername.setText(model.getValueAt(r, 3).toString());
        txtPassword.setText("");
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    // UI UTILS
    private JTextField createField(boolean editable) {
        JTextField f = new JTextField();
        f.setFont(FONT);
        f.setEditable(editable);
        return f;
    }

    private JPanel createRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setBackground(CARD);
        JLabel l = new JLabel(label);
        l.setFont(FONT);
        row.add(l, BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JButton createButton(String text, java.awt.event.ActionListener e) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(e);
        return btn;
    }
}
