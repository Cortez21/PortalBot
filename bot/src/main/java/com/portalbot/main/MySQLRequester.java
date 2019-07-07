package com.portalbot.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class MySQLRequester {
    private ConfigReader config;
    private String mysqlUser;
    private String pass;
    private String db_name;
    private String query;

    public MySQLRequester() {
        try {
            config = new ConfigReader();
            db_name = config.getMysql_database_name();
            mysqlUser = config.getMysql_user_name();
            pass = config.getMysql_database_password();
            if (db_name == null) {
                db_name = "bot_db";
            }
            query = String.format("jdbc:mysql://localhost:3306/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", db_name);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IOException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public User getUser(String chatID) {
        User result = null;
        try (Connection conn = DriverManager.getConnection(query, mysqlUser, pass);
        Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery("SELECT * FROM users WHERE chatID=chatID");
            while (results.next()) {
                result = new User(results.getString("chatID"), results.getString("portalLogin"), results.getString("portalPassword"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
    }




}
