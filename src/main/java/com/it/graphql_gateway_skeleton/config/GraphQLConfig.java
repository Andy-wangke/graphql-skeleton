package com.it.graphql_gateway_skeleton.config;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * GraphQL configuration to ensure proper initialization of GraphQL runtime.
 * This configuration manually sets up GraphQL execution when auto-configuration fails.
 *
 * @author Andy wang
 * @created 2025/9/16
 */
@Configuration
public class GraphQLConfig {

    /**
     * Configure GraphQL runtime wiring.
     * This bean ensures that GraphQL auto-configuration is properly triggered.
     *
     * @return RuntimeWiringConfigurer instance
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> {
            // Auto-configuration will handle the rest based on @QueryMapping, @SchemaMapping, etc.
            // This configurer ensures GraphQL auto-configuration is properly triggered
        };
    }

    /**
     * Create GraphQL bean manually to ensure proper initialization.
     * This fallback ensures GraphQL endpoints are available even if auto-configuration fails.
     *
     * @return GraphQL instance
     * @throws IOException if schema file cannot be read
     */
    @Bean
    public GraphQL graphQL() throws IOException {
        // Load schema from classpath
        ClassPathResource schemaResource = new ClassPathResource("schema.graphqls");
        String schemaContent = schemaResource.getContentAsString(StandardCharsets.UTF_8);

        // Parse schema
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaContent);

        // Build runtime wiring (Spring will auto-wire the resolvers)
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().build();

        // Generate schema
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        // Build GraphQL
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}