package org.hust.subsystem.interbank;


import java.io.*;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This class is used to represent the interbank boundary
 * which is used to send request to interbank.
 */
public class InterbankBoundary {

    private static final Logger LOGGER = Logger.getLogger(InterbankBoundary.class.getName());
    private final String interbankUrl = "https://ecopark-system-api.herokuapp.com";

    // Used to reset credit card's balance
    public static void main(String[] args) {
        try {
            System.out.println(new InterbankBoundary().resetBalance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Response requestToMakeTransaction(Request request) {
        String responseText = null;
        try {
            responseText = post(request, request.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response(responseText);
    }

    /**
     * Send a GET request to interbank.
     *
     * @param url   the interbank's url
     * @param token the token of the request
     * @return Response from interbank in form of a String if success
     * @throws Exception if fail to send request to interbank
     */
    public String get(String url, String token) throws Exception {
        LOGGER.info("Request URL: " + url + "\n");
        HttpURLConnection conn = setupConnection(url);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder respone = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            respone.append(inputLine + "\n");
        }
        in.close();
        LOGGER.info("Response Info: " + respone.substring(0, respone.length() - 1));
        return respone.substring(0, respone.length() - 1);
    }

    /**
     * Send data to interbank.
     *
     * @param data data to be sent to interbank
     * @param path path to interbank
     * @return Response from interbank in form of a String if success
     * @throws IOException - if fail to send request to interbank
     */
    public String post(Object data, String path) throws IOException {
        allowMethods("PATCH");
        String payload;
        if (data instanceof Request) {
            Request request = (Request) data;
            payload = request.toData();
        } else {
            payload = data.toString();
        }
        LOGGER.info("Request Info:\nRequest URL: " + interbankUrl + "\n"
                    + "Payload Data: " + payload + "\n");
        HttpURLConnection conn = setupConnection(interbankUrl + path);
        conn.setRequestMethod("PATCH");
        conn.setRequestProperty("Content-Type", "application/json");
        Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        writer.write(payload);
        writer.close();
        BufferedReader in;
        String inputLine;
        if (conn.getResponseCode() / 100 == 2) {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        LOGGER.info("Respone Info: " + response);
        return response.toString();
    }

    private HttpURLConnection setupConnection(String url) throws IOException {
        URL lineApiUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) lineApiUrl.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

    private String resetBalance() throws IOException {
        String data = "{\"cardCode\":\"ict_group7_2021\","
                      + "\"owner\":\"Group 7\",\"cvvCode\":\"279\",\"dateExpired\":\"1125\"}";
        return post(data, "/api/card/reset-balance");
    }

    private void allowMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
            System.setProperty("illegal-access", "permit");
            methodsField.setAccessible(true);

            VarHandle modifiers;
            try {
                var lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
                modifiers = lookup.findVarHandle(Field.class, "modifiers", int.class);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }

            int mods = methodsField.getModifiers();
            if (Modifier.isFinal(mods)) {
                modifiers.set(methodsField, mods & ~Modifier.FINAL);
            }

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null/* static field */, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
