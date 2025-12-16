package view;

import manager.SalaryManager;
import model.Salary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class SalaryForm extends JPanel {

    private JTextField txtEmpId, txtBase, txtBonus, txtMonth, txtYear;
    private JTable table;
    private DefaultTableModel model;
    private SalaryManager dao = new SalaryManager();
    private DecimalFormat df = new DecimalFormat("#,###");

    public SalaryForm() {

        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // ===== TITLE =====
        JLabel title = new JLabel("QUẢN LÝ LƯƠNG NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(5,2,8,8));

        txtEmpId = new JTextField();
        txtBase  = new JTextField();
        txtBonus = new JTextField();
        txtMonth = new JTextField();
        txtYear  = new JTextField();

        form.add(new JLabel("ID Nhân viên"));
        form.add(txtEmpId);
        form.add(new JLabel("Lương cơ bản"));
        form.add(txtBase);
        form.add(new JLabel("Thưởng"));
        form.add(txtBonus);
        form.add(new JLabel("Tháng"));
        form.add(txtMonth);
        form.add(new JLabel("Năm"));
        form.add(txtYear);

        JPanel left = new JPanel(new BorderLayout(8,8));
        left.add(form, BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel btns = new JPanel(new GridLayout(2,2,8,8));
        JButton btnAdd    = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear  = new JButton("Làm mới");

        btns.add(btnAdd);
        btns.add(btnUpdate);
        btns.add(btnDelete);
        btns.add(btnClear);

        left.add(btns, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"ID","NV ID","Lương","Thưởng","Tổng","Tháng","Năm"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== EVENTS =====
        btnAdd.addActionListener(e -> addSalary());
        btnUpdate.addActionListener(e -> updateSalary());
        btnDelete.addActionListener(e -> deleteSalary());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> fillForm());

        loadData();
    }

    // =========================
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
        Salary s = getFormData(0);
        if (dao.insert(s)) loadData();
    }

    private void updateSalary() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row,0).toString());
        Salary s = getFormData(id);
        if (dao.update(s)) loadData();
    }

    private void deleteSalary() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row,0).toString());
        if (dao.delete(id)) loadData();
    }

    private Salary getFormData(int id) {
        return new Salary(
                id,
                Integer.parseInt(txtEmpId.getText()),
                Double.parseDouble(txtBase.getText().replace(",","")),
                Double.parseDouble(txtBonus.getText().replace(",","")),
                0,
                Integer.parseInt(txtMonth.getText()),
                Integer.parseInt(txtYear.getText())
        );
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r == -1) return;

        txtEmpId.setText(model.getValueAt(r,1).toString());
        txtBase.setText(model.getValueAt(r,2).toString().replace(",",""));
        txtBonus.setText(model.getValueAt(r,3).toString().replace(",",""));
        txtMonth.setText(model.getValueAt(r,5).toString());
        txtYear.setText(model.getValueAt(r,6).toString());
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
