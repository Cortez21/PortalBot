package com.portalbot.main;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Date;


public class Launch {
    private static DataStream data;
    private static HttpConnection connection;
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        data = new DataStream();
        connection = new HttpConnection();
        System.out.println(new Date().toString());
        Serializer serializer = new Serializer();
        SessionsHolder holder = new SessionsHolder();
        User user = new Serializer().loadUser("551140537");
        PortalRequester requester;
        System.out.println(String.format("chatID: %s, Login: %s, Password: %s", user.getChatID(), user.getPortalLogin(), user.getPortalPassword()));
        MySQLRequester mySQLRequester = new MySQLRequester();

    }

    public static void logging(String login, String password) {
        data.setQuery("https://portal.alpm.com.ua/index.php?action=login");
        data.setParams(String.format("login=%s&password=%s", login, password));
        data = connection.start(data);
    }

    public static void switching(String date) {
        data.setQuery("https://portal.alpm.com.ua/headless.php");
        data.setParams(String.format("action=workschedule1&city=&data=%s", date));
        data = connection.start(data);
    }
}
