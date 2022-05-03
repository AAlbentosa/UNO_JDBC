package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DBExceptions;

public class MysqlCon {

    public String driver;
    public String database;
    public String hostname;
    public String port;
    public String url;
    public String username;
    public String password;
    private Connection conn;
    
    public MysqlCon() {
    	conn = null;
    	driver = "com.mysql.cj.jdbc.Driver";
        database = "dam2tm06uf2p1";
        hostname = "localhost";
        port = "3306";
        url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
        username = "java";//root
        password = "123";//root
    }
    
    public Connection getConnection() throws DBExceptions {
        
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
        	throw new DBExceptions(DBExceptions.ERROR_BBDD);
        }

        return conn;
    }
}