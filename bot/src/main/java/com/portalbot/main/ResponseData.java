package com.portalbot.main;

import java.util.*;

public class ResponseData {
    private Map<String, List<Task>> tasks = new HashMap<>();
    private List<String> alarmsForClosingTasks = new ArrayList<>();

    public void setTasks(Map<String, List<Task>> tasks) {
        this.tasks = tasks;
    }

    public Map<String, List<Task>> getTasks() {
        return tasks;
    }

    public void setAlarmsForClosingTasks(List<String> alarmsForClosingTasks) {
        this.alarmsForClosingTasks = alarmsForClosingTasks;
    }

    public List<String> getAlarmsForClosingTasks() {
        return alarmsForClosingTasks;
    }
}
