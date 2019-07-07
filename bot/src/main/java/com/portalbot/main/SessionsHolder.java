package com.portalbot.main;

import com.portalbot.main.exceptions.BadLoggingException;

import java.text.SimpleDateFormat;
import java.util.*;

public class SessionsHolder {
    private ResponseData data = new ResponseData();
    private List<String> sessions;
    private Serializer serializer = new Serializer();
    private TaskListener listener = new TaskListener();
    private Map<String, List<Task>> tasks = new HashMap<>();

    public ResponseData ask() {
        sessions = serializer.loadSessions();
        List<String> alarms = new ArrayList<>();
        for (String chatID : sessions) {
            try {
                tasks.put(chatID, listener.listen(chatID));
            } catch (BadLoggingException ble) {
                LogWriter.add("Wrong logging data or connection was breaking. Go next...");
                continue;
            }
            if (!checkingIfWasTheMessage() && listener.checkingForClosing(chatID)) {
                LogWriter.add(String.format("Detected non-closed tasks chatID's : %s", chatID));
                alarms.add(chatID);
            }
        }
        data.setAlarmsForClosingTasks(alarms);
        if (!alarms.isEmpty()) {
            serializer.saveDateOfMessaging(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        data.setTasks(tasks);
        return data;
    }

    public boolean openSession(String chatID) {
        boolean result = false;
        sessions = serializer.loadSessions();
        if (!sessions.contains(chatID)) {
            result = sessions.add(chatID);
        }
        serializer.saveSessions(sessions);
        return result;
    }

    public boolean closeSession(String chatID) {
        boolean result = false;
        sessions = serializer.loadSessions();
        if (sessions.contains(chatID)) {
            result = sessions.remove(chatID);
        }
        serializer.saveSessions(sessions);
        return result;
    }

    public boolean checkSession(String chatID) {
        boolean result = false;
        List sessions = serializer.loadSessions();
        if (sessions.contains(chatID)) {
            result = true;
        }
        return result;
    }

    public boolean checkingIfWasTheMessage() {
        boolean result = true;
        int currentTime = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String loadedDate = serializer.loadDateOfMessaging();
        if (currentTime >= 22 && !currentDate.equals(loadedDate)) {
            result = false;
        }
        return result;
    }
}
