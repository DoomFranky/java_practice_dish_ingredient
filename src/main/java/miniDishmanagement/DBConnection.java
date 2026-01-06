package miniDishmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    String url = System.getenv("url");
    String user = System.getenv("user");
    String password = System.getenv("password");

    public Connection getBDConnection (){
        try{
            Connection connection = DriverManager.getConnection(url,user,password);
            return connection;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}