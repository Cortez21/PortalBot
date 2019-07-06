package com.portalbot.main;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HttpConnectionTest {
    @Test
    public void whenSettingSomeProperties() throws IOException {
        HttpConnection httpConnection = new HttpConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://localhost/").openConnection();
        Map<String, String> properties = new HashMap<>();
        properties.put("pr1", "value1");
        properties.put("pr2", "value2");
        properties.put("pr3", "value3");
        httpConnection.setProperties(httpURLConnection, properties);
        assertThat(httpURLConnection.getHeaderFields(), is(httpURLConnection.getHeaderFields()));
    }
}
