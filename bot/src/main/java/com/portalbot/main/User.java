package com.portalbot.main;


import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private String chatID;
    private String name;
    private Map<String, Task> tasks;
    private String portalLogin;
    private String portalPassword;

    public User(String chatID, String portalLogin, String portalPassword) {
        tasks = new HashMap<>();
        this.chatID = chatID;
        this.portalLogin = portalLogin;
        this.portalPassword = portalPassword;
    }

    public String getChatID() {
        return chatID;
    }

    public String getName() {
        return name;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public String getPortalLogin() {
        return portalLogin;
    }

    public String getPortalPassword() {
        return portalPassword;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public void setPortalLogin(String portalLogin) {
        this.portalLogin = portalLogin;
    }

    public void setPortalPassword(String portalPassword) {
        this.portalPassword = portalPassword;
    }
}
