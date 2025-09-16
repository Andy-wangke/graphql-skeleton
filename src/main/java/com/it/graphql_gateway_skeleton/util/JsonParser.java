package com.it.graphql_gateway_skeleton.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Minimal JSON parsing utilities with batch processing support
 */
@Component
public class JsonParser {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Parse JSON string to JsonNode
     */
    public JsonNode parse(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }

    /**
     * Parse JSON array and convert each element using provided mapper function
     */
    public <T> List<T> parseArray(String json, Function<JsonNode, T> mapper) {
        try {
            JsonNode array = this.parse(json);
            List<T> result = new ArrayList<>();
            for (JsonNode node : array) {
                result.add(mapper.apply(node));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON array", e);
        }
    }

    /**
     * Parse JSON to specific type using TypeReference
     */
    public <T> T parseToType(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to type", e);
        }
    }

    /**
     * Convert object to JSON string
     */
    public String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Safe get string value from JsonNode
     */
    public String getString(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null ? field.asText() : null;
    }

    /**
     * Safe get int value from JsonNode
     */
    public int getInt(JsonNode node, String fieldName, int defaultValue) {
        JsonNode field = node.get(fieldName);
        return field != null ? field.asInt(defaultValue) : defaultValue;
    }

    /**
     * Safe get double value from JsonNode
     */
    public double getDouble(JsonNode node, String fieldName, double defaultValue) {
        JsonNode field = node.get(fieldName);
        return field != null ? field.asDouble(defaultValue) : defaultValue;
    }
}