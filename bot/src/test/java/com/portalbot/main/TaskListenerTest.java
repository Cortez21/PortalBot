package com.portalbot.main;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TaskListenerTest {

    @Test
    public void whenHasOldThreeTasksAndGetTwoNew() {
        TaskListener taskListener = new TaskListener();
        Task task1 = new Task("1111");
        Task task2 = new Task("2222");
        Task task3 = new Task("3333");
        Task task4 = new Task("4444");
        Task task5 = new Task("5555");
        task1.setDate("2019-04-12");
        task2.setDate("2019-04-12");
        task3.setDate("2019-04-12");
        task4.setDate("2019-04-12");
        task5.setDate("2019-04-12");

        task1.setTime("");
        task2.setTime("");
        task3.setTime("");
        task4.setTime("");
        task5.setTime("");

        Map<String, Task> oldTasks = Stream.of(task1, task2, task3).collect(Collectors.toMap(o -> o.getTaskNumber(), o -> o));
        List<Task> newTasks = Stream.of(task1, task2, task3, task4, task5).collect(Collectors.toList());
        assertThat(taskListener.compareTasks(oldTasks, newTasks), is(Stream.of(task4, task5).collect(Collectors.toList())));
    }

    @Test
    public void whenHasThreeDuplicates() {
        TaskListener taskListener = new TaskListener();
        List<Task> source = new ArrayList<>();
        List<Task> expect = new ArrayList<>();
        Task task1 = new Task("1111");
        Task task2 = new Task("2222");
        Task task3 = new Task("3333");
        Collections.addAll(source, task1, task2, task1, task3, task1);
        Collections.addAll(expect, task1, task2, task3);
        assertThat(taskListener.removeTaskNumbersDuplicates(source), is(expect));
    }

}
