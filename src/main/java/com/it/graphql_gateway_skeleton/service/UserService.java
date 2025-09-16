package com.it.graphql_gateway_skeleton.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.graphql_gateway_skeleton.entity.User;
import org.springframework.stereotype.Service;
/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
@Service
public class UserService {
    private final RestClient rest = new RestClient();
    private final ObjectMapper mapper = new ObjectMapper();


    public User getById(String id) {
        // 示例后端：GET http://user-svc/users/{id}
        String json = rest.get("http://localhost:9001/users/" + id);
        try {
            JsonNode n = mapper.readTree(json);
            return new User(n.get("id").asText(), n.get("name").asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
