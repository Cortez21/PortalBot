package com.portalbot.main;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Cfg
public class ConfigReader {
    private String telegramBotName;
    private String telegramBotToken;

    private String mysqlDatabaseName;
    private String mysqlUserName;
    private String mysqlDatabasePassword;

    private int taskListeningTime;
    private int routerCheckListeningTime;

    public String getTelegramBotName() {
        return telegramBotName;
    }

    public String getTelegramBotToken() {
        return telegramBotToken;
    }

    public String getMysqlDatabaseName() {
        return mysqlDatabaseName;
    }

    public String getMysqlUserName() {
        return mysqlUserName;
    }

    public String getMysqlDatabasePassword() {
        return mysqlDatabasePassword;
    }

    public int getTaskListeningTime() {
        return taskListeningTime;
    }

    public int getRouterCheckListeningTime() {
        return routerCheckListeningTime;
    }

    public ConfigReader() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException, InvocationTargetException, IOException {
        ConfigParser.parse(this, "files/settings.conf");
    }
}
