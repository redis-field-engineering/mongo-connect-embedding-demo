package com.redis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.connect.dto.ChangeEventDTO;
import com.redis.connect.dto.JobPipelineStageDTO;
import com.redis.connect.pipeline.event.handler.impl.BaseCustomStageHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class EmbeddingStage extends BaseCustomStageHandler {
    private final HttpClient httpClient;
    private final String openAiToken;
    public EmbeddingStage(String jobId, String jobType, JobPipelineStageDTO jobPipelineStage) {
        super(jobId, jobType, jobPipelineStage);
        this.httpClient = HttpClient.newHttpClient();
        this.openAiToken = System.getenv("OPENAI_API_KEY");
    }

    @Override
    public void onEvent(ChangeEventDTO changeEventDTO) throws Exception {
        float[] result = getEmbedding(changeEventDTO.getValues().get("message").toString());
        changeEventDTO.getValues().put("embedding", result);
    }

    @Override
    public void init() throws Exception {
        String openAiToken = System.getenv("OPENAI_API_KEY");
        if (openAiToken == null) {
            throw new Exception("OPENAI_API_KEY environment variable not set");
        }
    }

    @Override
    public void shutdown() throws Exception {

    }

    public float[] getEmbedding(String txt) throws Exception{
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("input", txt);
        requestPayload.put("model", "text-embedding-ada-002");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/embeddings"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.openAiToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() == 200) {
            JsonNode jsonResponse = objectMapper.readTree(response.body());
            JsonNode embeddingNode = jsonResponse.at("/data/0/embedding");
            float[] embedding = objectMapper.convertValue(embeddingNode, float[].class);

            return embedding;
        } else {
            throw new RuntimeException("Error: " + response.statusCode() + " Response: " + response.body());
        }
    }
}
