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
//        String mysqlUser = "root";
//        String pass = "qweds44r";
//        String query = "jdbc:mysql://localhost:3306/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        try (Connection conn = DriverManager.getConnection(query, mysqlUser, pass);
//             Statement statement = conn.createStatement()) {
//            System.out.println("We're connected");
//            ResultSet results = statement.executeQuery("SELECT title, author, year_issue, publisher FROM book, author, publisher WHERE book.id_author=author.id_author && book.id_publisher=publisher.id_publisher");
//            while (results.next()) {
//                System.out.println(String.format("Title: %s, Author: %s, Year: %s, Publisher: %s", results.getString(1), results.getString(2), results.getString(3), results.getString(4)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
        MySQLRequester mySQLRequester = new MySQLRequester();
        System.out.println(mySQLRequester.getUser("1111111111").getPortalPassword());

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
