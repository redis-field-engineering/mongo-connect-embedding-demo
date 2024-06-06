package com.redis;

import com.redis.connect.dto.ChangeEventDTO;
import com.redis.connect.dto.JobPipelineStageDTO;
import com.redis.connect.pipeline.event.handler.impl.BaseCustomStageHandler;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.List;

public class EmbeddingStage extends BaseCustomStageHandler {
    private OpenAiService openAiService;
    public EmbeddingStage(String jobId, String jobType, JobPipelineStageDTO jobPipelineStage) {
        super(jobId, jobType, jobPipelineStage);

    }

    @Override
    public void onEvent(ChangeEventDTO changeEventDTO) throws Exception {
        EmbeddingRequest embeddingRequest = new EmbeddingRequest();
        List<String> inputs = new ArrayList<>();
        inputs.add(changeEventDTO.getValues().get("message").toString());
        embeddingRequest.setInput(inputs);
        embeddingRequest.setModel("text-embedding-ada-002");
        EmbeddingResult result =  openAiService.createEmbeddings(embeddingRequest);
        changeEventDTO.getValues().put("embedding", result.getData().get(0).getEmbedding());
    }

    @Override
    public void init() throws Exception {
        String openAiToken = System.getenv("OPENAI_API_KEY");
        if (openAiToken == null) {
            throw new Exception("OPENAI_API_KEY environment variable not set");
        }

        // Initialize OpenAI API client
        openAiService = new OpenAiService(openAiToken);
    }

    @Override
    public void shutdown() throws Exception {

    }
}
