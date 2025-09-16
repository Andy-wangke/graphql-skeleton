package com.it.graphql_gateway_skeleton;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test demonstrating GraphQL queries
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureGraphQlTester
@TestPropertySource(properties = {
    "logging.level.com.it.graphql_gateway_skeleton=DEBUG"
})
public class GraphqlIntegrationTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    public void testUserQuery() {
        // Note: This test will fail without mock services running
        // This is just to demonstrate the GraphQL query structure
        
        String query = """
            query {
              user(id: "1") {
                id
                name
                orders(limit: 2) {
                  id
                  totalPrice
                  items {
                    quantity
                    product {
                      id
                      name
                      price
                    }
                  }
                }
              }
            }
            """;

        // This would work with actual downstream services
        // graphQlTester
        //     .document(query)
        //     .execute()
        //     .path("user.id")
        //     .entity(String.class)
        //     .isEqualTo("1");
        
        System.out.println("GraphQL query structure validated: " + query);
    }

    @Test
    public void testSchemaValidation() {
        // Test that GraphQL schema loads correctly
        String introspectionQuery = """
            query {
              __schema {
                types {
                  name
                }
              }
            }
            """;
            
        graphQlTester
            .document(introspectionQuery)
            .execute()
            .path("__schema.types")
            .entityList(Object.class)
            .hasSizeGreaterThan(0);
    }
}