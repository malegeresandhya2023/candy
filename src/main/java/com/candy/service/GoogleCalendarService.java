package com.candy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
@Service
public class GoogleCalendarService {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public String getGoogleCalendarEvents(@AuthenticationPrincipal OAuth2AuthenticationToken authenticationToken) throws Exception {
        if (authenticationToken == null) {
            throw new IllegalArgumentException("User is not authenticated");
        }

        // Fetch the authorized client for the user
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(), authenticationToken.getName());

        if (client == null) {
            throw new IllegalStateException("Authorized client not found");
        }

        // Get the access token from the client
        String accessToken = client.getAccessToken().getTokenValue();

        // Use the access token to call Google Calendar API (example: list events)
        String urlString = "https://www.googleapis.com/calendar/v3/calendars/primary/events";
        URL url = new URL(urlString);

        // Open connection and set authorization header
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        // Read response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();  // Return the API response (JSON)
    }
}
