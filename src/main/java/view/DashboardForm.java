package view;

import javax.swing.*;
import java.awt.*;

public class DashboardForm extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public DashboardForm() {
        setTitle("Restaurant Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //  SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 700));
        sidebar.setBackground(new Color(34, 40, 49));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblLogo = new JLabel(" RESTAURANT");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        sidebar.add(lblLogo);

        JButton btnHome = createMenuButton(" Trang chủ");
        JButton btnEmployee = createMenuButton(" Nhân viên");
        JButton btnFood = createMenuButton(" Món ăn");
        JButton btnOrder = createMenuButton(" Đơn hàng");
        JButton btnReport = createMenuButton(" Báo cáo");
        JButton btnSalary = createMenuButton(" Lương");
        JButton btnLogout = createMenuButton(" Đăng xuất");

        sidebar.add(btnHome);
        sidebar.add(btnEmployee);
        sidebar.add(btnFood);
        sidebar.add(btnOrder);
        sidebar.add(btnReport);
        sidebar.add(btnSalary);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        //  MAIN CONTENT
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createHomePanel(), "home");
        mainPanel.add(new EmployeeForm(), "employee");
        mainPanel.add(new FoodForm(), "food");
        mainPanel.add(new OrderForm(), "order");
        mainPanel.add(new ReportForm(), "report");
        mainPanel.add(new SalaryForm(), "salary");

        add(mainPanel, BorderLayout.CENTER);

        //  EVENTS
        btnHome.addActionListener(e -> showPage("home"));
        btnEmployee.addActionListener(e -> showPage("employee"));
        btnFood.addActionListener(e -> showPage("food"));
        btnOrder.addActionListener(e -> showPage("order"));
        btnReport.addActionListener(e -> showPage("report"));
        btnSalary.addActionListener(e -> showPage("salary"));

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setVisible(true);
    }

    //  METHODS

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(210, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(57, 62, 70));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 173, 181));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(57, 62, 70));
            }
        });

        return btn;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(" CHÀO MỪNG ĐẾN HỆ THỐNG QUẢN LÝ NHÀ HÀNG ", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbl.setForeground(new Color(60, 60, 60));

        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    private void showPage(String name) {
        cardLayout.show(mainPanel, name);
    }
}
