package manager;
import model.OrderDetail;
import storage.JDBCconnect;

import java.sql.*;
import java.util.List;

public class OrderManager {

    public static boolean saveOrder(List<OrderDetail> details) {

        double total = 0;
        for (OrderDetail d : details) {
            total += d.getQty() * d.getPrice();
        }

        try (Connection conn = JDBCconnect.getConnection()) {

            conn.setAutoCommit(false);

            String sqlOrder =
                    "INSERT INTO orders(employee_id, total, created_at) VALUES (?, ?, NOW())";

            PreparedStatement psOrder =
                    conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);

            psOrder.setInt(1, 1);   // employee_id tạm thời để 1
            psOrder.setDouble(2, total);
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            rs.next();
            int orderId = rs.getInt(1);

            String sqlDetail =
                    "INSERT INTO order_detail(order_id, foods_id, quantity, price) VALUES (?, ?, ?, ?)";

            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            for (OrderDetail d : details) {
                psDetail.setInt(1, orderId);
                psDetail.setInt(2, d.getFoodId());
                psDetail.setInt(3, d.getQty());
                psDetail.setDouble(4, d.getPrice());
                psDetail.addBatch();
            }

            psDetail.executeBatch();
            conn.commit();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
