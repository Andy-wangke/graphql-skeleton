package com.it.graphql_gateway_skeleton.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * TODO didn't register the application
 * @Author Andy wang
 * @Created 2025/9/12
 */
@RestController
//@ConditionalOnProperty(name = "custom.graphql.enabled", havingValue = "true", matchIfMissing = false)
public class GraphQLController {

    private final GraphQL graphql;

    @Autowired
    public GraphQLController(GraphQL graphql) {
        this.graphql = graphql;
    }


    @RequestMapping(value="/graphql", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object graphQL(@RequestParam("query") String query,
        @RequestParam(value = "operationName", required = false) String operationName,
        @RequestParam(value = "variables", required = false) String variablesJson,
        HttpServletResponse httpServletResponse) throws Exception {
        
        return executeGraphQLQuery(query, operationName, variablesJson, httpServletResponse);
    }

    @RequestMapping(value="/graphql", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object graphQLPost(@RequestBody String requestBody, HttpServletResponse httpServletResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(requestBody);
        
        String query = jsonNode.get("query").asText();
        String operationName = jsonNode.has("operationName") ? jsonNode.get("operationName").asText() : null;
        String variablesJson = jsonNode.has("variables") ? jsonNode.get("variables").toString() : null;
        
        return executeGraphQLQuery(query, operationName, variablesJson, httpServletResponse);
    }

    private Object executeGraphQLQuery(String query, String operationName, String variablesJson, HttpServletResponse httpServletResponse) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> variables = new HashMap<>();
            
            if (variablesJson != null && !variablesJson.isEmpty()) {
                variables = mapper.readValue(variablesJson, Map.class);
            }
            
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(query)
                .operationName(operationName)
                .variables(variables)
                .build();
            
            ExecutionResult executionResult = graphql.execute(executionInput);
            
            Map<String, Object> result = new HashMap<>();
            result.put("data", executionResult.getData());
            
            if (!executionResult.getErrors().isEmpty()) {
                result.put("errors", executionResult.getErrors().stream()
                    .map(error -> {
                        Map<String, Object> errorMap = new HashMap<>();
                        errorMap.put("message", error.getMessage());
                        errorMap.put("locations", error.getLocations());
                        errorMap.put("path", error.getPath());
                        return errorMap;
                    })
                    .collect(Collectors.toList()));
            }
            return result;
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", List.of(Map.of("message", "Internal server error: " + e.getMessage())));
            return errorResponse;
        }
    }

    @RequestMapping(value="/hello", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object hello() throws Exception {
        return Map.of("message", "Hello from GraphQL Gateway!");
    }
}
