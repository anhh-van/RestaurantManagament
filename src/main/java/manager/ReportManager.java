package manager;

import storage.JDBCconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    // Lấy doanh thu 1 ngày (DATE(created_at))
    public double getRevenueByDate(String date) {
        double total = 0;
        String sql = "SELECT COALESCE(SUM(total),0) FROM orders WHERE DATE(created_at) = ?";
        try (Connection conn = JDBCconnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) total = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    // Lấy danh sách orders (dùng để show table)
    public List<String[]> getAllOrders() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT id, employee_id, total, created_at FROM orders ORDER BY created_at DESC";
        try (Connection conn = JDBCconnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("employee_id")),
                        String.valueOf(rs.getDouble("total")),
                        String.valueOf(rs.getTimestamp("created_at"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Xóa an toàn theo ngày: xóa detail trước, sau đó xóa orders trong 1 transaction
    // Trả về số orders đã xóa (>=0)
    public int deleteOrdersByDate(String date) {
        String sqlSelect = "SELECT id FROM orders WHERE DATE(created_at) = ?";
        String sqlDeleteDetail = "DELETE FROM order_detail WHERE order_id = ?";
        String sqlDeleteOrder = "DELETE FROM orders WHERE DATE(created_at) = ?";

        try (Connection conn = JDBCconnect.getConnection()) {
            conn.setAutoCommit(false);

            // get order ids
            List<Integer> ids = new ArrayList<>();
            try (PreparedStatement psSel = conn.prepareStatement(sqlSelect)) {
                psSel.setString(1, date);
                try (ResultSet rs = psSel.executeQuery()) {
                    while (rs.next()) ids.add(rs.getInt("id"));
                }
            }

            if (ids.isEmpty()) {
                conn.rollback();
                return 0;
            }

            // delete order_detail per order_id via batch
            try (PreparedStatement psDelDetail = conn.prepareStatement(sqlDeleteDetail)) {
                for (Integer id : ids) {
                    psDelDetail.setInt(1, id);
                    psDelDetail.addBatch();
                }
                psDelDetail.executeBatch();
            }

            // delete orders rows
            int deleted;
            try (PreparedStatement psDelOrder = conn.prepareStatement(sqlDeleteOrder)) {
                psDelOrder.setString(1, date);
                deleted = psDelOrder.executeUpdate();
            }

            conn.commit();
            return deleted;

        } catch (Exception e) {
            e.printStackTrace();
            // nếu lỗi thì trả về 0
            return 0;
        }
    }
}
