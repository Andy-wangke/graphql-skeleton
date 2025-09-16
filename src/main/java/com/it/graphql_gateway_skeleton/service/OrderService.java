package com.it.graphql_gateway_skeleton.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.graphql_gateway_skeleton.entity.Order;
import com.it.graphql_gateway_skeleton.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
@Service
public class OrderService {
    private final RestClient rest = new RestClient();
    private final ObjectMapper mapper = new ObjectMapper();


    public List<Order> listByUser(String userId, int limit) {
        // 示例后端：GET http://order-svc/orders?userId=..&limit=..
        String url = "http://localhost:9002/orders?userId=" + userId + "&limit=" + limit;
        String json = rest.get(url);
        try {
            var arr = mapper.readTree(json);
            List<Order> out = new ArrayList<>();
            for (var node : arr) {
                String id = node.get("id").asText();
                double total = node.get("totalPrice").asDouble();
                List<OrderItem> items = mapper.convertValue(
                    node.get("items"), new TypeReference<List<OrderItem>>() {});
                out.add(new Order(id, total, items));
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
