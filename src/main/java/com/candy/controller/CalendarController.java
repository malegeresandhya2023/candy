package com.candy.controller;

import com.candy.model.Event;
import com.candy.service.GoogleCalendarService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarController {

    private final GoogleCalendarService googleCalendarService;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper for parsing JSON

    public CalendarController(GoogleCalendarService googleCalendarService, ObjectMapper objectMapper) {
        this.googleCalendarService = googleCalendarService;
        this.objectMapper = objectMapper;
    }

    // This is the endpoint that renders the calendar view page
    @GetMapping("/")
    public String showCalendar(Model model, OAuth2AuthenticationToken authenticationToken) {
        if (authenticationToken == null) {
            return "redirect:/custom-login";  // Redirect to login page
        }
        return "calendar";  // Serve the calendar.html page
    }

    // This endpoint returns events for FullCalendar in JSON format
    @GetMapping("/get-events")
    @ResponseBody
    public List<Event> getEvents(OAuth2AuthenticationToken authenticationToken) {
        List<Event> events = new ArrayList<>();

        try {
            String googleCalendarEvents = googleCalendarService.getGoogleCalendarEvents(authenticationToken);
            JsonNode eventsNode = objectMapper.readTree(googleCalendarEvents);

            // Assuming the events are in a node named "items"
            if (eventsNode.has("items")) {
                for (JsonNode eventNode : eventsNode.get("items")) {
                    String title = eventNode.has("summary") ? eventNode.get("summary").asText() : "No title";
                    String startTime = getStartTime(eventNode);
                    String endTime = getEndTime(eventNode);
                    String description = eventNode.has("description") ? eventNode.get("description").asText() : "No description";

                    Event event = new Event(title, "colleague", startTime, endTime, description);
                    events.add(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    // Helper method to get start time, handling missing fields or different formats
    private String getStartTime(JsonNode eventNode) {
        if (eventNode.has("start")) {
            JsonNode startNode = eventNode.get("start");
            if (startNode.has("dateTime")) {
                return startNode.get("dateTime").asText();
            } else if (startNode.has("date")) {
                return startNode.get("date").asText();
            }
        }
        return "No Start Time";
    }

    // Helper method to get end time, handling missing fields or different formats
    private String getEndTime(JsonNode eventNode) {
        if (eventNode.has("end")) {
            JsonNode endNode = eventNode.get("end");
            if (endNode.has("dateTime")) {
                return endNode.get("dateTime").asText();
            } else if (endNode.has("date")) {
                return endNode.get("date").asText();
            }
        }
        return "No End Time";
    }
}
