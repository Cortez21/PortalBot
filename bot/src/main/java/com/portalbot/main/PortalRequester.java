package com.portalbot.main;

import com.portalbot.main.exceptions.BadLoggingException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PortalRequester {
    private DataStream data;
    private HttpConnection httpConnection;
    private String login;
    private String password;

    public PortalRequester(String chatID) throws BadLoggingException {
        User user = new MySQLRequester().getUser(chatID);
        data = new DataStream();
        httpConnection = new HttpConnection();
        this.login = user.getPortalLogin();
        this.password = user.getPortalPassword();
        tryToLogging(5);
    }

    public boolean loggingRequest() {
        boolean result = false;
        LogWriter.add(String.format("Trying to logging: %s...", login));
        data.setQuery("https://portal.alpm.com.ua/index.php?action=login");
        data.setParams(String.format("login=%s&password=%s", login, password));
        data = httpConnection.start(data);
        if (data.getBody().contains("Сменить пароль")) {
            result = true;
            LogWriter.add("Success!");
        }
        return result;
    }

    public void tryToLogging(int tryings) throws BadLoggingException {
        boolean success = false;
        int currentTryings = 0;
        while (!success && currentTryings < tryings) {
            success = loggingRequest();
            currentTryings++;
        }
        if (!success) {
            throw new BadLoggingException("Wrong pair: login/password or httpConnection was broken");
        }
    }

    public String switchingToDate(String date) {
        LogWriter.add(String.format("Switching to date: %s", date));
        data.setQuery("https://portal.alpm.com.ua/headless.php");
        data.setParams(String.format("action=workschedule1&city=&data=%s", date));
        data = httpConnection.start(data);
        return data.getBody();
    }

    public String switchingToTask(String portalTaskNumber) {
        data.setQuery(String.format("https://portal.alpm.com.ua/index.php?action=editWorkRequest_new&id=%s", portalTaskNumber));
        data = httpConnection.start(data);
        return data.getBody();
    }

    public String getAddressInfo(String id) {
        data.setQuery("https://portal.alpm.com.ua/headless.php");
        data.setParams(String.format("action=addrBrief&id=%s", id));
        data = httpConnection.start(data);
        return data.getBody();
    }

    public String checkRouter(String account) {
        data.setQuery("https://portal.alpm.com.ua/headless.php");
        data.setParams(String.format("action=checkSBMSRouter&account=%s", account));
        data = httpConnection.start(data);
        return data.getBody();
    }

    public String getQueue() {
        data.addProperty("Referer", "https://portal.alpm.com.ua/index.php?action=requestlist_new&data_from=2019-07-08&data_to=2019-07-08&status=1&iscitygroup=0&type=1&show=1");
        data.setQuery("https://portal.alpm.com.ua/tablerjson.php?query=requestlist_new");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String params = new StringBuilder()
                .append("query=requestlist_new&template=tplTable_json&vars=%7B%22data_from%22%3A%22")
                .append(date)
                .append("%22%2C%22data_to%22%3A%22")
                .append(date)
                .append("%22%2C%22network%22%3A%22%22%2C%22type%22%3A%22%22%2C%22subtype%22%3A%22%22%2C%22status%22%3A%221%22%2C%22city%22%3A%22%22%2C%22iscitygroup%22%3A0%7D&bodyOnly=0&buttons=1&showtotal=true&dynamiclimit=30")
                .toString();

        data.setParams(params);
        data = httpConnection.start(data);
        return data.getBody();
    }

    public String getTaskBody(String portalNumber) {
        data.setQuery("https://portal.alpm.com.ua/index.php");
        data.setParams(String.format("action=viewRequest&id=%s", portalNumber));
        data = httpConnection.start(data);
        return data.getBody();
    }
}
