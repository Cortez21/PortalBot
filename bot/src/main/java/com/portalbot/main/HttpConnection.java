package com.portalbot.main;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpConnection {

    public DataStream start(DataStream data) {

        installCerts();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(data.getQuery()).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Length", "" + data.getParams().getBytes().length);
            setProperties(connection, data.getProperties());
            connection.setInstanceFollowRedirects(data.isAutoRedirect());
            sendOutputStream(connection, data.getParams());

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                if (connection.getHeaderField("Set-Cookie") != null) {
                    data.setPortal3030(connection.getHeaderField("Set-Cookie").split("portal3030=")[1].substring(0, 32));
                }
                String body = readResponseBody(connection);
                data.setBody(body);
                if (body.contains("var securitycode=")) {
                    data.setSecurityCode(body.split("var securitycode=\"")[1].substring(0, 32));
                }
            }  else {
                data = null;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }

    private void sendOutputStream(HttpURLConnection connection, String params) throws IOException {
        OutputStream os = connection.getOutputStream();
        byte[] data = params.getBytes("UTF-8");
        os.write(data);
        os.close();
    }

    public void setProperties(HttpURLConnection connection, Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private String readResponseBody(HttpURLConnection connection) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        bf.close();

        return sb.toString();
    }

    private void installCerts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }
}
