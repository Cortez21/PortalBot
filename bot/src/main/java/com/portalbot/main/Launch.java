package com.portalbot.main;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Launch {
    private static DataStream data;
    private static Connection connection;
    public static void main(String[] args) throws IOException {
        data = new DataStream();
        connection = new Connection();
        System.out.println(new Date().toString());
        Serializer serializer = new Serializer();
        SessionsHolder holder = new SessionsHolder();
        User user = new Serializer().loadUser("551140537");
        PortalRequester requester;
//        try {
//            requester = new PortalRequester(user.getPortalLogin(), user.getPortalPassword());
//            System.out.println(requester.switchingToDate("2019-06-08"));
//        } catch (BadLoggingException e) {
//            e.printStackTrace();
//        }
        logging(user.getPortalLogin(), user.getPortalPassword());
        for (int i = 0; i < 10; i++) {
            data.setQuery("https://portal.alpm.com.ua/headless.php");
            data.setParams(String.format("action=checkSBMSRouter&account=9039690"));
            data = connection.start(data);
            System.out.println(data.getBody());
        }
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
