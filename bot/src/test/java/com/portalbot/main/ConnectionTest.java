package com.portalbot.main;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ConnectionTest {
    @Test
    public void whenSettingSomeProperties() throws IOException {
        Connection connection = new Connection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://localhost/").openConnection();
        Map<String, String> properties = Map.of("pr1", "value1", "pr2", "value2", "pr3", "value3");
        connection.setProperties(httpURLConnection, properties);
        assertThat(httpURLConnection.getHeaderFields(), is(httpURLConnection.getHeaderFields()));
    }
}
