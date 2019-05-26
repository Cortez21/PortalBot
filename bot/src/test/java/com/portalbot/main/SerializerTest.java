package com.portalbot.main;

import org.junit.Test;

import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SerializerTest {
    SessionsHolder holder = new SessionsHolder();
    @Test
    public void whenSavingTasksAndLoadingIts() {
        Serializer serializer = new Serializer();
        Map<String, Task> tasks = new HashMap<>();
        tasks.put("1111", new Task("1111"));
        tasks.put("2222", new Task("2222"));
        tasks.put("3333", new Task("3333"));
        User user = new User("0000", "login", "pass");
        serializer.saveTasks(user);
        User result = serializer.loadTasks(user.getChatID());
        assertThat(result.getTasks(), is(user.getTasks()));
    }
}
