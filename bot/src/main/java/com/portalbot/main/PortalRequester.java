package com.portalbot.main;

public class PortalRequester {
    private DataStream data;
    private Connection connection;
    private String login;
    private String password;

    public PortalRequester(String login, String password) throws BadLoggingException {
        data = new DataStream();
        connection = new Connection();
        this.login = login;
        this.password = password;
        tryToLogging(5);
    }

    public boolean loggingRequest() {
        boolean result = false;
        LogWriter.add(String.format("Trying to logging: %s...", login));
        data.setQuery("https://portal.alpm.com.ua/index.php?action=login");
        data.setParams(String.format("login=%s&password=%s", login, password));
        data = connection.start(data);
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
            throw new BadLoggingException("Wrong pair: login/password or connection was broken");
        }
    }

    public String switchingToDate(String date) {
        LogWriter.add(String.format("Switching to date: %s", date));
        data.setQuery("https://portal.alpm.com.ua/headless.php");
        data.setParams(String.format("action=workschedule1&city=&data=%s", date));
        data = connection.start(data);
        return data.getBody();
    }

    public String switchingToTask(String portalTaskNumber) {
        data.setQuery(String.format("https://portal.alpm.com.ua/index.php?action=editWorkRequest_new&id=%s", portalTaskNumber));
        data = connection.start(data);
        return data.getBody();
    }

    public String getAddressInfo(String id) {
        data.setQuery("https://portal.alpm.com.ua/headless.php");
        data.setParams(String.format("action=addrBrief&id=%s", id));
        data = connection.start(data);
        return data.getBody();
    }


}
