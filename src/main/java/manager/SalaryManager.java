package manager;

import model.Salary;
import storage.JDBCconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryManager {

    public List<Salary> getAll() {
        List<Salary> list = new ArrayList<>();
        String sql = "SELECT * FROM salary";

        try (Connection c = JDBCconnect.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Salary(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getDouble("base_salary"),
                        rs.getDouble("bonus"),
                        rs.getDouble("total"),
                        rs.getInt("month"),
                        rs.getInt("year")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Salary s) {
        String sql = "INSERT INTO salary(employee_id, base_salary, bonus, month, year) VALUES(?,?,?,?,?)";
        try (Connection c = JDBCconnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, s.getEmployeeId());
            ps.setDouble(2, s.getBaseSalary());
            ps.setDouble(3, s.getBonus());
            ps.setInt(4, s.getMonth());
            ps.setInt(5, s.getYear());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Salary s) {
        String sql = "UPDATE salary SET employee_id=?, base_salary=?, bonus=?, month=?, year=? WHERE id=?";
        try (Connection c = JDBCconnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, s.getEmployeeId());
            ps.setDouble(2, s.getBaseSalary());
            ps.setDouble(3, s.getBonus());
            ps.setInt(4, s.getMonth());
            ps.setInt(5, s.getYear());
            ps.setInt(6, s.getId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM salary WHERE id=?";
        try (Connection c = JDBCconnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
