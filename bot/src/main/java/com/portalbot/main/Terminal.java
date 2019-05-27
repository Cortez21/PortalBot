package com.portalbot.main;

import java.util.*;

public class Terminal {
    private static Serializer serializer = new Serializer();
    private static GarbageCollector gc = new GarbageCollector();
    private static SessionsHolder holder = new SessionsHolder();
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Hi. Welcome to the BOT terminal!");
        boolean exit = false;

        while (!exit) {
            System.out.print("Enter command, please: \n");
            String answer = in.nextLine();
            if (answer.contains("/getSessions")) {
                getSessions();
            } else if (answer.contains("/exit")) {
                exit = true;
            } else if (answer.contains("/getTaskList")) {
                getTaskList();
            } else if (answer.contains("/getTasks")) {
                String[] data = answer.split(" ");
                if (data.length == 2) {
                    getTasks(data[1]);
                }
            } else if (answer.contains("/getTask")) {
                String[] data = answer.split(" ");
                if (data.length == 2) {
                    getTask(data[1]);
                }
            } else if (answer.contains("/addToTaskList")) {
                String[] data = answer.split(" ");
                if (data.length == 2) {
                    addToTaskList(data[1]);
                }
            } else if (answer.contains("/removeFromTaskList")) {
                String[] data = answer.split(" ");
                if (data.length == 2) {
                    removeFromTaskList(data[1]);
                }
            } else if (answer.contains("/clearTaskList")) {
                clearTaskList();
            }  else if (answer.contains("/turnGC")) {
                String[] data = answer.split(" ");
                if (data.length == 2) {
                    turnGC(data[1]);
                    System.out.println("Done!");
                }
            } else if (answer.contains("/openSession")) {
                String[] data = answer.split(" ");
                if (data.length == 2) {
                    openSession(data[1]);
                    System.out.println("Done!");
                }
            } else if (answer.contains("/closeSession")) {
                String[] data = answer.split(" ");
                if (data.length == 2) {
                    closeSession(data[1]);
                    System.out.println("Done!");
                }
            }
            }

    }

    public static void getSessions() {
        List<String> sessions = serializer.loadSessions();
        for (String value : sessions) {
            System.out.println(value);
        }
    }

    public static void closeSession(String chatID) {
        holder.closeSession(chatID);
    }

    public static void openSession(String chatID) {
        holder.openSession(chatID);
    }

    public static void getTasks(String chatID) {
        User user = serializer.loadTasks(chatID);
        for (Task task : user.getTasks().values()) {
            System.out.println(task.getTaskNumber());
            System.out.println(task.toMessage());
        }
    }

    public static void getTask(String taskNumber) {
        System.out.println(serializer.loadTask(taskNumber).toMessage());
    }

    public static void getTaskList() {
        System.out.println(serializer.loadTaskList());
    }

    public static void addToTaskList(String taskNumber) {
        Set<String> values = new TreeSet<>();
        values.add(taskNumber);
        serializer.addToTaskList(values);
        System.out.println("Done");
    }

    public static void removeFromTaskList(String taskNumber) {
        if (serializer.removeFromTaskList(taskNumber)) {
            System.out.println("Done!");
        }
    }

    public static void clearTaskList() {
        serializer.saveTaskList(new HashSet<>());
        if (serializer.loadTaskList().size() == 0) {
            System.out.println("Done!");
        }
    }

    public static void turnGC(String chatID) {
        gc.turn(new ArrayList<>(), chatID);
    }
}
