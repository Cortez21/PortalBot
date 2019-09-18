package com.portalbot.main;


import com.portalbot.main.exceptions.BadLoggingException;
import com.portalbot.main.queue.QueueParser;
import com.portalbot.main.queue.QueueTask;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Date;
import java.util.List;


public class Launch {
    private static DataStream data;
    private static HttpConnection connection;
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, BadLoggingException {
        data = new DataStream();
        connection = new HttpConnection();
        System.out.println(new Date().toString());
        Serializer serializer = new Serializer();
        SessionsHolder holder = new SessionsHolder();
        QueueParser parser = new QueueParser();
        MySQLRequester mySQLRequester = new MySQLRequester();
        PortalRequester requester = new PortalRequester("1111111111");

        ConfigReader configReader = new ConfigReader();
        System.out.println(configReader.getTelegramBotName());
        System.out.println(configReader.getTelegramBotToken());


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
