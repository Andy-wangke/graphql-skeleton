package com.it.graphql_gateway_skeleton.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.graphql_gateway_skeleton.entity.Product;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private final RestClient rest = new RestClient();
    private final ObjectMapper mapper = new ObjectMapper();


    /** 批量查询：GET /products?ids=1,2,3 -> Map(id->Product) */
    public Map<String, Product> getByIds(Collection<String> ids) {
        if (ids.isEmpty()) return Collections.emptyMap();
        String joined = String.join(",", ids);
        String url = "http://localhost:9003/products?ids=" + URLEncoder.encode(joined, StandardCharsets.UTF_8);
        String json = rest.get(url);
        try {
            Map<String, Product> map = new HashMap<>();
            JsonNode arr = mapper.readTree(json);
            for (JsonNode n : arr) {
                Product p = new Product(
                    n.get("id").asText(),
                    n.get("name").asText(),
                    n.get("price").asDouble()
                );
                map.put(p.id, p);
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
