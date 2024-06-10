package com.msuser.msuser.util;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;

@Service
public class TokenProvider {

    public static String getAdminToken() {
        try {
            return getToken("http://localhost:9090/realms/master/protocol/openid-connect/token",
                    "admin-cli", "", "admin", "admin");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getToken(String tokenUrl, String clientId, String clientSecret, String username, String password) throws IOException, URISyntaxException {
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        URL url = new URL(tokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        connection.setDoOutput(true);

        String data = "grant_type=password&username=" + username + "&password=" + password;

        connection.getOutputStream().write(data.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public String requestLogout(String refreshToken) throws IOException {
        String uri = "http://localhost:9090/realms/triwal-realm-dev/protocol/openid-connect/logout";
        String clientId = "triwal-app";
        String clientSecret = "2AW8lVZlq25DVBwI8UTLRYaK95bQsg9p";
        return getLogout(uri, refreshToken, clientId, clientSecret);

    }

    private static String getLogout(String logoutUrl, String refreshToken, String clientId, String clientSecret) throws IOException {
        URL url = new URL(logoutUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        String data = "refresh_token=" + refreshToken + "&client_id=" + clientId + "&client_secret=" + clientSecret;

        try (OutputStream os = connection.getOutputStream()) {
            os.write(data.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? connection.getInputStream() : connection.getErrorStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        return responseCode + ": " + response.toString();
    }

    public String requestToken(String username, String password) {
        try {
            return getToken("http://localhost:9090/realms/triwal-realm-dev/protocol/openid-connect/token",
                    "triwal-app", "2AW8lVZlq25DVBwI8UTLRYaK95bQsg9p", username, password);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }


    public String sendResetPasswordEmail(String userId) throws IOException, URISyntaxException {

        StringBuilder response = new StringBuilder();

        try {
            String accessToken = getAdminToken();

            JSONObject jsonObject = new JSONObject(accessToken);

            URL url = new URL("http://localhost:9090/admin/realms/triwal-realm-dev/users/" + userId + "/execute-actions-email");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + jsonObject.getString("access_token"));
            connection.setDoOutput(true);

            String data = "[\"UPDATE_PASSWORD\"]";
            try (OutputStream os = connection.getOutputStream()) {
                os.write(data.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                response.append("Password reset email sent successfully.");
            } else {
                System.out.println("Failed to send reset password email. HTTP response code: " + responseCode);
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String errorLine;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    System.out.println("Error response: " + errorResponse.toString());
                    response.append(errorResponse.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

}
