package com.it.graphql_gateway_skeleton.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom GraphQL controller that handles GraphQL queries and mutations.
 * This controller provides endpoints for both GET and POST requests to /graphql.
 * When enabled, this controller will handle GraphQL requests instead of Spring Boot's default endpoint.
 *
 * @author Andy wang
 * @created 2025/9/12
 */
@RestController
@ConditionalOnProperty(name = "custom.graphql.enabled", havingValue = "true", matchIfMissing = false)
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

    /**
     * GraphiQL playground endpoint that serves the GraphiQL UI.
     * Provides an interactive interface for testing GraphQL queries.
     *
     * @return GraphiQL HTML page
     * @throws IOException if the HTML file cannot be read
     */
    @RequestMapping(value="/graphiql", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String graphiql() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("static/graphiql.html");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>GraphiQL - Error</title>
                </head>
                <body>
                    <h1>Error loading GraphiQL</h1>
                    <p>Could not load the GraphiQL interface. Error: """ + e.getMessage() + """
                    </p>
                </body>
                </html>
                """;
        }
    }

    @RequestMapping(value="/hello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object hello() throws Exception {
        return Map.of("message", "Hello from GraphQL Gateway!");
    }
}
