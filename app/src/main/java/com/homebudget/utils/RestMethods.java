package com.homebudget.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;

public class RestMethods implements Callable<Response> {

    String request;
    String endpoint;
    String token;
    String method;
    Map<String, String> headers;

    private RestMethods(RestBuilder builder) {

        request = builder.request;
        endpoint = builder.endpoint;
        token = builder.token;
        method = builder.method;
        headers = builder.headers;
    }

    private HttpURLConnection init() {
        try {
            URL url = new URL("http://192.168.100.103:8080" + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if (token != null) {
                connection.setRequestProperty("Authorization", token);
            }
            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }
            connection.setRequestMethod(method);
            if (method.equalsIgnoreCase("POST")) {
                connection.setDoOutput(true);
            }
            connection.connect();
            return connection;
        } catch (Exception e) {
            Log.e("", e.toString(), e);
            return null;
        }
    }


    private void writeToStream(HttpURLConnection connection, String request) {
        try (OutputStream stream = connection.getOutputStream()) {
            byte[] input = request.getBytes("utf-8");
            stream.write(input, 0, input.length);
        } catch (Exception e) {
            Log.e("", e.toString(), e);
        }
    }

    private Response readFromStream(HttpURLConnection connection) {
        StringBuilder builder = new StringBuilder();
        try {
            int code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            Log.i("", "HTTP " + code + " " + message);
            if (code == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return new Response(code, builder.toString());
            } else {
                Log.e("", "Server respond with status  " + code);
            }
            return new Response();
        } catch (Exception e) {
            Log.e("", e.toString(), e);
            return new Response();
        }
    }

    @Override
    public Response call() {
        HttpURLConnection connection = init();
        if (request != null) {
            writeToStream(connection, request);
        }
        return readFromStream(connection);
    }

    public static class RestBuilder {

        public RestBuilder() {
        }

        String request;
        String endpoint;
        String token;
        String method;
        Map<String, String> headers;

        public RestBuilder setRequest(String request) {
            this.request = request;
            return this;
        }

        public RestBuilder setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public RestBuilder setToken(String token) {
            this.token = token;
            return this;
        }

        public RestBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public RestBuilder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public RestMethods create() {
            return new RestMethods(this);
        }
    }
}
