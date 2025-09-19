package com.it.graphql_gateway_skeleton.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.graphql_gateway_skeleton.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
@Service
public class UserService {
    private final RestClient rest = new RestClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Map<String, User> MOCK_USERS = new HashMap<>();
    static {
        MOCK_USERS.put("1", new User("1", "Alice Johnson"));
        MOCK_USERS.put("2", new User("2", "Bob Smith"));
        MOCK_USERS.put("3", new User("3", "Charlie Brown"));
        MOCK_USERS.put("4", new User("4", "Diana Wilson"));
        MOCK_USERS.put("5", new User("5", "Eve Davis"));
    }

    public User getById(String id) {
        return MOCK_USERS.get(id);
/*      // GET http://user-svc/users/{id}
        String json = rest.get("http://localhost:9001/users/" + id);
        try {
            JsonNode n = mapper.readTree(json);
            return new User(n.get("id").asText(), n.get("name").asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    public void updateUser(String id, String name) {
        User user = MOCK_USERS.get(id);
        if (user != null) {
            user.name = name;
        }
    }
}
