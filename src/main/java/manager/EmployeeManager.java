package manager;

import model.Employee;
import storage.JDBCconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    public List<Employee> getAll(){
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try(Connection conn = JDBCconnect.getConnection();
            Statement st =conn.createStatement();
            ResultSet rs =st.executeQuery(sql)){

            while (rs.next()){
                list.add(new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getString("role")));
        }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public boolean insert(Employee e) {
        String sql = "INSERT INTO employees(name, role, username, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = JDBCconnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getName());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getUsername());
            ps.setString(4, e.getPassword());

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean update(Employee e){
        String sql = "UPDATE employees SET name=?, username=?, password=?, role=?, WHERE id=?";
        try (Connection conn = JDBCconnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,e.getName());
            ps.setString(2,e.getUsername());
            ps.setString(3,e.getPassword());
            ps.setString(4,e.getRole());
            ps.setInt(5,e.getId());
            return ps.executeUpdate() >0 ;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id){
        String sql ="DELETE FROM employees WHERE id=?";

        try(Connection conn = JDBCconnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setInt(1,id);
            return ps.executeUpdate() > 0;
        }catch (Exception ex) {
            ex.printStackTrace();
        }return false;
    }
}
