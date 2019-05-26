package com.portalbot.main;

import java.text.SimpleDateFormat;
import java.util.*;

public class GarbageCollector {
    private Serializer serializer = new Serializer();
    private Set<String> tasks;

    public void turn(List<Task> newTasks, String chatID) {
        tasks = serializer.loadTaskList();
        Set<String> strNewTasks = new HashSet<>();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        for (String taskNumber : tasks) {
            Task currentTask = serializer.loadTask(taskNumber);
            if (currentTask != null && currentTask.getDate().compareTo(currentDate) < 0) {
                User user = serializer.loadTasks(chatID);
                if (user.getTasks().containsKey(taskNumber)) {
                    user.getTasks().remove(taskNumber);
                }
                serializer.saveTasks(user);
                serializer.removeTask(taskNumber);
                serializer.removeFromTaskList(taskNumber);
            } else if (currentTask != null) {
                strNewTasks.add(taskNumber);
            }
        }
        strNewTasks.addAll(newTasks.stream().collect(() -> new ArrayList<>(), (list, element) -> list.add(element.getTaskNumber()), (list1, list2)-> list1.addAll(list2)));
        serializer.saveTaskList(strNewTasks);
    }
}
