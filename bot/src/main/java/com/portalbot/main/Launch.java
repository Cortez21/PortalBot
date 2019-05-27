package com.portalbot.main;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Launch {
    private static DataStream data;
    private static Connection connection;
    public static void main(String[] args) {
        data = new DataStream();
        connection = new Connection();
        System.out.println(new Date().toString());
        Serializer serializer = new Serializer();
        SessionsHolder holder = new SessionsHolder();
        String session = "797003480";
        holder.openSession(session);


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
