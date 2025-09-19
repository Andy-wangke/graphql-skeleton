package com.it.graphql_gateway_skeleton.service;

import com.it.graphql_gateway_skeleton.entity.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
@Service
public class ProductService {

    private static final Map<String, Product> MOCK_PRODUCTS = new HashMap<>();

    static {
        MOCK_PRODUCTS.put("1", new Product("1", "Laptop", 999.99));
        MOCK_PRODUCTS.put("2", new Product("2", "Mouse", 29.99));
        MOCK_PRODUCTS.put("3", new Product("3", "Keyboard", 79.99));
        MOCK_PRODUCTS.put("4", new Product("4", "Monitor", 299.99));
        MOCK_PRODUCTS.put("5", new Product("5", "Headphones", 149.99));
        MOCK_PRODUCTS.put("6", new Product("6", "Phone", 699.99));
        MOCK_PRODUCTS.put("7", new Product("7", "Tablet", 399.99));
        MOCK_PRODUCTS.put("8", new Product("8", "Webcam", 89.99));
    }

    public Map<String, Product> getByIds(Collection<String> ids) {
        if (ids.isEmpty()) return Collections.emptyMap();

        Map<String, Product> result = new HashMap<>();
        for (String id : ids) {
            Product product = MOCK_PRODUCTS.get(id);
            if (product != null) {
                result.put(id, product);
            }
        }
        return result;

        /**        //fetch data from real backend service
         *         String joined = String.join(",", ids);
         *         String url = "http://localhost:9003/products?ids=" + URLEncoder.encode(joined, StandardCharsets.UTF_8);
         *         String json = rest.get(url);
         *         try {
         *             Map<String, Product> map = new HashMap<>();
         *             JsonNode arr = mapper.readTree(json);
         *             for (JsonNode n : arr) {
         *                 Product p = new Product(
         *                     n.get("id").asText(),
         *                     n.get("name").asText(),
         *                     n.get("price").asDouble()
         *                 );
         *                 map.put(p.id, p);
         *             }
         *             return map;
         *         } catch (Exception e) {
         *             throw new RuntimeException(e);
         *         }
         */
    }
}
