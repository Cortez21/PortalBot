package com.portalbot.main;

import com.portalbot.main.queue.QueueParser;
import com.portalbot.main.queue.QueueTask;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLRequester {
    private ConfigReader config;
    private String mysqlUser;
    private String pass;
    private String dbName;
    private String query;

    public MySQLRequester() {
        try {
            config = new ConfigReader();
            dbName = config.getMysqlDatabaseName();
            mysqlUser = config.getMysqlUserName();
            pass = config.getMysqlDatabasePassword();
            if (dbName == null) {
                dbName = "bot_db";
            }
            query = String.format("jdbc:mysql://localhost:3306/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", dbName);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IOException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public User getUser(String chatID) {
        User result = null;
        try (Connection conn = DriverManager.getConnection(query, mysqlUser, pass);
        Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(String.format("SELECT * FROM users WHERE chat_id=%s", chatID));
            while (results.next()) {
                result = new User(results.getString("chat_id"), results.getString("portalLogin"), results.getString("portalPassword"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getQueueTaskList() {
        List<String> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(query, mysqlUser, pass);
             Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(String.format("SELECT portal_number FROM tasks WHERE entry_date='%s'", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
            while (results.next()) {
                result.add(results.getString("portal_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insertTask(QueueTask task) {
        try (Connection conn = DriverManager.getConnection(query, mysqlUser, pass);
             Statement statement = conn.createStatement()) {
            statement.execute(String.format("INSERT INTO tasks VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    task.getPortalNumber(),
                    task.getTaskNumber(),
                    task.getType(),
                    task.getSubType(),
                    task.getAccount(),
                    task.getAddress(),
                    task.getFlat(),
                    task.getName(),
                    task.getTariff(),
                    task.getPhoneNumber(),
                    task.getPhoneNumber2(),
                    task.getPlanedDate(),
                    task.isUrgent() ? 1 : 0,
                    task.getInstallation(),
                    task.getStatusMessage(),
                    task.getAddressId(),
                    task.getOwnersChatId(),
                    task.isQueueMessage() ? 1 : 0,
                    task.isInWorkMessage() ? 1 : 0,
                    task.getEntryDate(),
                    task.getPort(),
                    task.getPairA(),
                    task.getPairB()
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






}
