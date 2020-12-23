package flink.learning.example.visualization.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.inferred.freebuilder.FreeBuilder;

import java.io.IOException;

@FreeBuilder
@JsonDeserialize(builder = Response.Builder.class)
public interface Response {
    /**
     * Returns a new {@link Builder} with the same property values as this {@link Response}
     */
    Builder toBuilder();

    /**
     * Builder of {@link Response} instances
     * auto generated builder className which cannot be modified
     */
    class Builder extends Response_Builder {
        private ObjectMapper objectMapper = new ObjectMapper();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder() {
            success(true);
            reason("");
            data("");
        }

        public String toJson() throws JsonProcessingException {
            return objectMapper.writeValueAsString(build());
        }

        public String toJsonSilently() {
            try {
                return objectMapper.writeValueAsString(build());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public Response parseFromJson(String json) throws IOException {
            return objectMapper.readValue(json, Response.class);
        }
    }

    boolean success();

    String reason();

    Object data();
}
