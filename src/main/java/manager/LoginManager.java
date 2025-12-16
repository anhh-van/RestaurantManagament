package manager;

import storage.JDBCconnect;

import java.sql.*;

public class LoginManager {
    public boolean checkLogin(String username, String password){
        String sql = "SELECT * FROM employees WHERE username = ? AND password = ?";

        try(Connection conn = JDBCconnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
