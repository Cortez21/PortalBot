package com.portalbot.main;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TaskListener {
    private DataStream data;
    private Connection connection;
    private Serializer serializer;
    private Parser parser;
    private GarbageCollector gc = new GarbageCollector();
    private PortalRequester requester;

    public TaskListener() {
        data = new DataStream();
        connection = new Connection();
        serializer = new Serializer();
        parser = new Parser();
    }

    public List<Task> listen(String chatID) throws BadLoggingException {
        User user = serializer.loadTasks(chatID);
        requester = new PortalRequester(user.getPortalLogin(), user.getPortalPassword());
        List<Task> newTasks = new ArrayList<>();
        Map<String, Task> oldTasks;
        List<Task> result;


        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = new SimpleDateFormat("MM").format(new Date());
        String day = new SimpleDateFormat("dd").format(new Date());
        DateIterator dateIterator = new DateIterator(year, month, day);



        for (int i = 0; i < 7; i++) {
            if (!user.getPortalLogin().equals("") && !user.getPortalPassword().equals("")) {
                logging(user.getPortalLogin(), user.getPortalPassword());
            } else {
                break;
            }
            String currentDate = dateIterator.next();
            switching(currentDate);
            List<Task> temp;
            try {
                temp = parser.execute(data.getBody());
            } catch (NullPointerException npe) {
                i--;
                LogWriter.add("Connection is broke. One more trying to connect...");
                continue;
            }
            setDate(temp, currentDate);
            setOwnersChatID(temp, chatID);
            newTasks.addAll(temp);
        }

        oldTasks = serializer.loadTasks(chatID).getTasks();
        result = compareTasks(oldTasks, newTasks);
        newTasks = removeTaskNumbersDuplicates(newTasks);
        gc.turn(newTasks, chatID);
        user.setTasks(newTasks.stream().collect(Collectors.toMap(o -> o.getTaskNumber(), o -> o)));
        serializer.saveTasks(user);
        return result;
    }


    public void logging(String login, String password) {
        LogWriter.add(String.format("Logging in to login: %s", login));
        data.setQuery("https://portal.alpm.com.ua/index.php?action=login");
        data.setParams(String.format("login=%s&password=%s", login, password));
        data = connection.start(data);
    }

    public void switching(String date) {
        LogWriter.add(String.format("Switching to date: %s", date));
        data.setQuery("https://portal.alpm.com.ua/headless.php");
        data.setParams(String.format("action=workschedule1&city=&data=%s", date));
        data = connection.start(data);
    }

    public List<Task> compareTasks(Map<String, Task> oldTasks, List<Task> newTasks) {
        List<Task> result = new ArrayList<>();
        for (Task newTask : newTasks) {
            Task oldTask = oldTasks.get(newTask.getTaskNumber());
            if (!oldTasks.containsKey(newTask.getTaskNumber())) {
                LogWriter.add(String.format("Detected a new task %s", newTask.getTaskNumber()));
                newTask.setStatusMessage(String.format("ДОБАВЛЕНА [НОВАЯ ЗАЯВКА](https://portal.alpm.com.ua/index.php?action=editWorkRequest_new&id=%s): ", newTask.getPortalTaskNumber()));
                result.add(newTask);
            } else if ((oldTask != null && !oldTask.getDate().equals(newTask.getDate()) && oldTask.getAddress().equals(newTask.getAddress()))
                    || (oldTask != null && !oldTask.getTime().equals(newTask.getTime())) && oldTask.getAddress().equals(newTask.getAddress())) {
                LogWriter.add(String.format("Detected moving the task %s", newTask.getTaskNumber()));
                newTask.setStatusMessage(String.format("[ЗАЯВКА](https://portal.alpm.com.ua/index.php?action=editWorkRequest_new&id=%s) ПЕРЕНЕСЕНА С %s(%s) НА %s(%s)",newTask.getPortalTaskNumber(), oldTask.getDate(), oldTask.getTime(), newTask.getDate(), newTask.getTime()));
                result.add(newTask);
            }
        }
        return result;
    }

    public void setDate(List<Task> tasks, String date) {
        tasks.stream().forEach(task -> task.setDate(date));
    }

    public List<Task> removeTaskNumbersDuplicates(List<Task> tasks) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            List<String> strResult = result.stream().collect(() -> new ArrayList<String>(), (list, element) -> list.add(element.getTaskNumber()), (list1, list2)-> list1.addAll(list2));
            if (!strResult.contains(task.getTaskNumber())) {
                result.add(task);
            }
        }
        return result;
    }

    public boolean checkingForClosing(String chatID) {
        boolean result = false;
        User user = serializer.loadTasks(chatID);
        for (Task task : user.getTasks().values()) {
            if (task.getStatus().equals("Назначено") && task.getDate().equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void setOwnersChatID(List<Task> tasks, String chatID) {
        tasks.forEach(task -> task.setOwnersChatID(chatID));
    }
}
