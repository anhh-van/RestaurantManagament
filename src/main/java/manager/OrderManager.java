package manager;

import model.OrderDetail;
import storage.JDBCconnect;
import java.sql.*;
import java.util.List;

public class OrderManager {

    public static boolean saveOrder(List<OrderDetail> cart, int tableNo, double totalAmount) {
        String sqlOrder = "INSERT INTO orders (employee_id, total, created_at, table_no) VALUES (?, ?, NOW(), ?)";
        // Đảm bảo tên bảng là 'order_details' và cột số lượng là 'qty'
        String sqlDetail = "INSERT INTO order_details (order_id, food_id, qty, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = JDBCconnect.getConnection()) {
            conn.setAutoCommit(false);
            int orderId = -1;
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, 1);
                psOrder.setDouble(2, totalAmount); // Lưu tổng tiền để Report không bị 0đ
                psOrder.setInt(3, tableNo);
                psOrder.executeUpdate();
                ResultSet rs = psOrder.getGeneratedKeys();
                if (rs.next()) orderId = rs.getInt(1);
            }

            if (orderId != -1) {
                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                    for (OrderDetail d : cart) {
                        psDetail.setInt(1, orderId);
                        psDetail.setInt(2, d.getFoodId());
                        psDetail.setInt(3, d.getQty());
                        psDetail.setDouble(4, d.getPrice());
                        psDetail.addBatch();
                    }
                    psDetail.executeBatch();
                }
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}