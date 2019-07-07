package com.portalbot.main;

import com.portalbot.main.exceptions.BadLoggingException;

public class PortalRequester {
    private DataStream data;
    private HttpConnection httpConnection;
    private String login;
    private String password;

    public PortalRequester(String chatID) throws BadLoggingException {
        User user = new Serializer().loadUser(chatID);
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


}
