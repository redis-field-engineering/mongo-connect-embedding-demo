package com.redis;

import com.redis.connect.dto.JobPipelineStageDTO;
import com.redis.connect.pipeline.event.handler.ChangeEventHandler;
import com.redis.connect.pipeline.event.handler.ChangeEventHandlerFactory;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomChangeEventHandlerFactory implements ChangeEventHandlerFactory {
    private final String instanceId = ManagementFactory.getRuntimeMXBean().getName();

    private static final String TYPE_EMBEDDING_PREP_STAGE = "EMBEDDING_PREP_STAGE";

    private static final String TYPE = "CUSTOM";

    private static final Set<String> supportedTypes = new HashSet<>();

    static {
        supportedTypes.add(TYPE_EMBEDDING_PREP_STAGE);
    }

    @Override
    public ChangeEventHandler getInstance(String s, String s1, JobPipelineStageDTO jobPipelineStageDTO){
        if (jobPipelineStageDTO.getStageName().equals(TYPE_EMBEDDING_PREP_STAGE)) {
            return new EmbeddingStage(s, s1, jobPipelineStageDTO);
        } else {
            throw new IllegalArgumentException("Unsupported stage type: " + jobPipelineStageDTO.getStageName());
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean contains(String s) {
        return supportedTypes.contains(s);
    }
}
