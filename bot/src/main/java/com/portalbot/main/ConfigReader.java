package com.portalbot.main;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Cfg
public class ConfigReader {
    private String telegram_bot_name;
    private String telegram_bot_token;

    private String mysql_database_name;
    private String mysql_user_name;
    private String mysql_database_password;

    private int task_listening_time;
    private int router_check_listening_time;

    public int getTask_listening_time() {
        return task_listening_time;
    }

    public String getMysql_database_name() {
        return mysql_database_name;
    }

    public String getMysql_database_password() {
        return mysql_database_password;
    }

    public String getMysql_user_name() {
        return mysql_user_name;
    }

    public String getTelegram_bot_name() {
        return telegram_bot_name;
    }

    public int getRouter_check_listening_time() {
        return router_check_listening_time;
    }

    public String getTelegram_bot_token() {
        return telegram_bot_token;
    }

    public ConfigReader() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException, InvocationTargetException, IOException {
        ConfigParser.parse(this, "files/settings.conf");
    }
}
