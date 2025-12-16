package storage;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCconnect {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/restaurant";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}