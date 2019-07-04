package com.portalbot.main;

import java.util.HashMap;
import java.util.Map;

public class DataStream {
    private String query;
    private String params;
    private String requestMethod;
    private String body;
    private String portal3030;
    private String securityCode;
    private boolean autoRedirect;
    private Map<String, String> properties;

    public DataStream() {
        requestMethod = "POST";
        autoRedirect = true;
        Map<String, String> properties = new HashMap<>();
        properties.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        properties.put("Accept-Encoding", "gzip, deflate, br");
        properties.put("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7,pl;q=0.6");
        properties.put("Cache-Control", "max-age=0");
        properties.put("HttpConnection", "keep-alive");
        properties.put("Host", "portal.alpm.com.ua");
        properties.put("Upgrade-Insecure-Requests", "1");
        properties.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        this.setProperties(properties);

    }


    public String getQuery() {
        return query;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getParams() {
        return params;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getBody() {
        return body;
    }

    public boolean isAutoRedirect() {
        return autoRedirect;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setProperties(Map<String, String> properties) {
        Map<String, String> temp = new HashMap<>();
        temp.putAll(properties);
        this.properties = temp;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setAutoRedirect(boolean autoRedirect) {
        this.autoRedirect = autoRedirect;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setPortal3030(String value) {
        portal3030 = value;

        if (securityCode == null) {
            properties.put("Cookie", String.format("portal3030=%s", portal3030));
        } else {
            properties.put("Cookie", String.format("portal3030=%s; sessioncode=%s", portal3030, securityCode));
        }
    }

    public void setSecurityCode(String value) {
        securityCode = value;
        if (portal3030 == null) {
            properties.put("Cookie", String.format("sessioncode=%s", securityCode));
        } else {
            properties.put("Cookie", String.format("portal3030=%s; sessioncode=%s", portal3030, securityCode));
        }
    }

    public String toStringProperties() {
        String ls = System.lineSeparator();
        return new StringBuilder()
                .append("query=").append(query).append(ls)
                .append("params=").append(params).append(ls)
                .append("requestMethod=").append(requestMethod).append(ls)
                .append("portal3030=").append(portal3030).append(ls)
                .append("securityCode=").append(securityCode).append(ls)
                .append("autoRedirect=").append(autoRedirect).append(ls)
                .append("properties=").append(properties.toString()).append(ls)
                .toString();
    }
}
