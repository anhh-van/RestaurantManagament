package manager;

import model.Food;
import storage.JDBCconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodManager {
    public static List<Food> getAllFoods() {
        List<Food> list = new ArrayList<>();
        String sql = "SELECT * FROM foods";

        try (Connection conn = JDBCconnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Food(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("category")));
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }return list;
    }

    public boolean addFood(Food food) {
        String sql = "INSERT INTO foods(name, price, category) VALUES (?, ?, ?)";

        try (Connection con = JDBCconnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, food.getName());
            ps.setDouble(2, food.getPrice());
            ps.setString(3, food.getCategory());

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateFood(Food food) {
        String sql = "UPDATE foods SET name=?, price=?, category=? WHERE id=?";

        try (Connection con = JDBCconnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, food.getName());
            ps.setDouble(2, food.getPrice());
            ps.setString(3, food.getCategory());
            ps.setInt(4, food.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean deleteFood(int id) {
        String sql = "DELETE FROM foods WHERE id=?";

        try (Connection con = JDBCconnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}


