package view;

import manager.SalaryManager;
import model.Salary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class SalaryForm extends JPanel {

    private JTextField txtEmpId, txtBase, txtBonus, txtMonth, txtYear;
    private JTable table;
    private DefaultTableModel model;
    private SalaryManager dao = new SalaryManager();
    private DecimalFormat df = new DecimalFormat("#,### đ");

    // Bảng màu Flat Design
    private final Color COLOR_PRIMARY = new Color(52, 152, 219); // Blue
    private final Color COLOR_SUCCESS = new Color(46, 204, 113); // Green
    private final Color COLOR_DANGER  = new Color(231, 76, 60);  // Red
    private final Color COLOR_BG      = new Color(240, 242, 245); // Light Gray

    public SalaryForm() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Title
        add(createHeader(), BorderLayout.NORTH);

        // Panel bên trái (Input Form)
        add(createLeftPanel(), BorderLayout.WEST);

        // Panel trung tâm (Table)
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel createHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        JLabel title = new JLabel(" QUẢN LÝ LƯƠNG NHÂN VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(44, 62, 80));
        p.add(title);
        return p;
    }

    private JPanel createLeftPanel() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(320, 0));

        JPanel form = new JPanel(new GridLayout(10, 1, 5, 5));
        form.setBackground(Color.WHITE);

        txtEmpId = styledTextField();
        txtBase  = styledTextField();
        txtBonus = styledTextField();
        txtMonth = styledTextField();
        txtYear  = styledTextField();

        form.add(new JLabel("Mã nhân viên:"));
        form.add(txtEmpId);
        form.add(new JLabel("Lương cơ bản (VNĐ):"));
        form.add(txtBase);
        form.add(new JLabel("Tiền thưởng (VNĐ):"));
        form.add(txtBonus);
        form.add(new JLabel("Tháng:"));
        form.add(txtMonth);
        form.add(new JLabel("Năm:"));
        form.add(txtYear);

        JPanel btns = new JPanel(new GridLayout(2, 2, 10, 10));
        btns.setBackground(Color.WHITE);

        JButton btnAdd    = styledButton("Thêm mới", COLOR_SUCCESS);
        JButton btnUpdate = styledButton("Cập nhật", COLOR_PRIMARY);
        JButton btnDelete = styledButton("Xóa bỏ", COLOR_DANGER);
        JButton btnClear  = styledButton("Làm mới", new Color(149, 165, 166));

        btns.add(btnAdd);
        btns.add(btnUpdate);
        btns.add(btnDelete);
        btns.add(btnClear);

        card.add(form, BorderLayout.CENTER);
        card.add(btns, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addSalary());
        btnUpdate.addActionListener(e -> updateSalary());
        btnDelete.addActionListener(e -> deleteSalary());
        btnClear.addActionListener(e -> clearForm());

        return card;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        model = new DefaultTableModel(
                new String[]{"ID", "NV ID", "Lương CB", "Thưởng", "Tổng nhận", "Tháng", "Năm"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(232, 241, 250));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(240, 240, 240));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        table.getSelectionModel().addListSelectionListener(e -> fillForm());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private JTextField styledTextField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return f;
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void loadData() {
        model.setRowCount(0);
        List<Salary> list = dao.getAll();
        for (Salary s : list) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getEmployeeId(),
                    df.format(s.getBaseSalary()),
                    df.format(s.getBonus()),
                    df.format(s.getTotal()),
                    s.getMonth(),
                    s.getYear()
            });
        }
    }

    private void addSalary() {
        try {
            Salary s = getFormData(0);
            if (dao.insert(s)) {
                loadData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        }
    }

    private void updateSalary() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lương cần sửa!");
            return;
        }
        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        Salary s = getFormData(id);
        if (dao.update(s)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        }
    }

    private void deleteSalary() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa bản ghi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            if (dao.delete(id)) loadData();
        }
    }

    private Salary getFormData(int id) {
        return new Salary(
                id,
                Integer.parseInt(txtEmpId.getText()),
                Double.parseDouble(txtBase.getText().replace(".", "").replace(",", "").replace(" đ", "")),
                Double.parseDouble(txtBonus.getText().replace(".", "").replace(",", "").replace(" đ", "")),
                0,
                Integer.parseInt(txtMonth.getText()),
                Integer.parseInt(txtYear.getText())
        );
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r == -1) return;

        txtEmpId.setText(model.getValueAt(r, 1).toString());
        txtBase.setText(model.getValueAt(r, 2).toString().replace(" đ", "").replace(",", ""));
        txtBonus.setText(model.getValueAt(r, 3).toString().replace(" đ", "").replace(",", ""));
        txtMonth.setText(model.getValueAt(r, 5).toString());
        txtYear.setText(model.getValueAt(r, 6).toString());
    }

    private void clearForm() {
        txtEmpId.setText("");
        txtBase.setText("");
        txtBonus.setText("");
        txtMonth.setText("");
        txtYear.setText("");
        table.clearSelection();
    }
}