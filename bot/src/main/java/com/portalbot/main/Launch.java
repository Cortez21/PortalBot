package com.portalbot.main;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.*;

public class Launch {
    private static DataStream data;
    private static Connection connection;
    public static void main(String[] args) {
        data = new DataStream();
        connection = new Connection();
        System.out.println(new Date().toString());
        Serializer serializer = new Serializer();
        SessionsHolder holder = new SessionsHolder();
        Map<String, Task> tasks = new HashMap<>();
        tasks.put("1111", new Task("1111"));
        tasks.put("2222", new Task("2222"));
        tasks.put("3333", new Task("3333"));
        User user = new User("0000", "login", "pass");
        user.setTasks(tasks);
        serializer.saveTasks(user);
        User result = serializer.loadTasks(user.getChatID());
        result.getTasks().values().stream().forEach(System.out::println);

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
