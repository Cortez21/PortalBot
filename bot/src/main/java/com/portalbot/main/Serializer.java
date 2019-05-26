package com.portalbot.main;

import org.telegram.telegrambots.api.objects.Message;

import java.io.*;
import java.util.*;

public class Serializer {
    private String mainPath = "~/home/PortalBot/files/";
    private String path = "~/home/PortalBot/files/";

    public Object loadObject(String filePath) throws IOException, ClassNotFoundException {
        Object result;
            FileInputStream fin = new FileInputStream(String.format("%s%s.ser", mainPath, filePath));
            ObjectInputStream ois = new ObjectInputStream(fin);
            result =  ois.readObject();
            fin.close();
            ois.close();
        return result;
    }

    public void saveObject(Object source, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(String.format("%s%s.ser", mainPath, filePath));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(source);
        fos.close();
        oos.close();
    }

    public void saveTasks(User user) {
        try {
            saveObject(user, String.format("users/%s", user.getChatID()));
             for (Task task : user.getTasks().values()) {
                 saveObject(task, String.format("users/tasks/%s", task.getTaskNumber()));
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User loadTasks(String chatID) {
        Map<String, Task> tasks = new HashMap<>();
        User user = new User("", "", "");
        try {
            user = (User) loadObject(String.format("users/%s", chatID));
            for (String taskNumber : user.getTasks().keySet()) {
                try {
                    tasks.put(taskNumber, (Task) loadObject(String.format("users/tasks/%s", taskNumber)));
                } catch (FileNotFoundException fnfe) {
                    continue;
                }
            }
        } catch (Exception e) {
            LogWriter.add(e.toString());
            e.printStackTrace();
        }
        user.setTasks(tasks);
        return user;
    }

    public void userRegistration(User user) {
        try {
            saveObject(user, String.format("users/%s", user.getChatID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSessions(List<String> session) {
        try {

            saveObject(session, "sessions");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> loadSessions() {
        List<String> sessions = new ArrayList<>();
        try {

            sessions = (List<String>) loadObject("sessions");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public void saveMessage(Message message) {
        try {
            saveObject(message, String.format("messages/%s", message.getChatId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message loadMessage(String chatID) {
        Message message = new Message();
        try {
            message = (Message) loadObject(String.format("messages/%s", chatID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public User loadUser(String chatID) {
        User user = new User(null, null, null);
        try {
            user = (User) loadObject(String.format("users/%s", chatID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void clearUserList() throws IOException {
        try {
            saveObject(new TreeSet<>(), "userList");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserList(Set<String> userList) {
        try {
        saveObject(userList, "userList");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> loadUserList() {
        Set<String> result = new TreeSet<>();
        try {
            result = (Set<String>) loadObject("userList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addToUserList(String chatID) {
        Set<String> users = loadUserList();
        if (!users.contains(chatID)) {
            users.add(chatID);
        }
        saveUserList(users);
    }

    public void removeFromUserList(String chatID) {
        Set<String> users = loadUserList();
        if (users.contains(chatID)) {
            users.remove(chatID);
        }
        saveUserList(users);
    }

    public void saveTaskList(Set<String> taskList) {
        try {
            saveObject(taskList, "taskList");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> loadTaskList() {
        Set<String> result = new HashSet<>();
        try {
            result =  (Set<String>) loadObject("taskList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addToTaskList(Set<String> newTasks) {
        Set<String> currentTasks = loadTaskList();
        currentTasks.addAll(newTasks);
        saveTaskList(currentTasks);
    }

    public boolean removeFromTaskList(String taskNumber) {
        Set<String> currentTasks = loadTaskList();
        Boolean result = currentTasks.remove(taskNumber);
        saveTaskList(currentTasks);
        return result;
    }

    public Task loadTask(String taskNumber) {
        Task result = new Task("");
        try {
            result = (Task) loadObject(String.format("users/tasks/%s", taskNumber));
        } catch (FileNotFoundException fnfe) {
            result = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean removeTask(String taskNumber) {
        return new File(String.format("%susers/tasks/%s.ser", path, taskNumber)).delete();
    }

    public void saveDateOfMessaging(String date) {
        try {
            saveObject(date, "messagingDate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadDateOfMessaging() {
        String result = "";
        try {
            result = (String) loadObject("messagingDate");
        } catch (FileNotFoundException fnfe) {
            saveDateOfMessaging("1987-02-21");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
