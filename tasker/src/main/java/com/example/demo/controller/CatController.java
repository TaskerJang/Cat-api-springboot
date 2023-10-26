package com.example.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CatController {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/random-cat")
    public String getRandomCatImage() {
        try {
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/images/search")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseBody = response.body().string();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String imageUrl = jsonNode.get(0).get("url").asText();
                return "<img src='" + imageUrl + "' alt='Random Cat Image'>";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while retrieving a cat image.";
        }
    }
}
