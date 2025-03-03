package database;

import java.sql.*;

public class DatabaseConnection {
    public static Connection getDatabaseConnection() throws SQLException, ClassNotFoundException {
        String url="jdbc:mysql://localhost:3306/VRS";
        String user="root";
        String password="";
        String driver="com.mysql.cj.jdbc.Driver";

        Class.forName(driver);

        Connection con=DriverManager.getConnection(url,user,password);

        return con;
    }
}