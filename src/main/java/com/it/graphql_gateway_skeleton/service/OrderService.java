package com.it.graphql_gateway_skeleton.service;

import com.it.graphql_gateway_skeleton.entity.Order;
import com.it.graphql_gateway_skeleton.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
@Service
public class OrderService {

    private static final Map<String, List<Order>> MOCK_ORDERS = new HashMap<>();

    static {
        MOCK_ORDERS.put("1", Arrays.asList(
            new Order("101", 1079.98, Arrays.asList(
                new OrderItem("1", 1),
                new OrderItem("2", 2)
            )),
            new Order("102", 449.98, Arrays.asList(
                new OrderItem("5", 1),
                new OrderItem("4", 1)
            ))
        ));

        MOCK_ORDERS.put("2", Arrays.asList(
            new Order("201", 779.98, Arrays.asList(
                new OrderItem("6", 1),
                new OrderItem("3", 1)
            )),
            new Order("202", 89.99, Arrays.asList(
                new OrderItem("8", 1)
            )),
            new Order("203", 1299.97, Arrays.asList(
                new OrderItem("1", 1),
                new OrderItem("4", 1)
            ))
        ));

        MOCK_ORDERS.put("3", Arrays.asList(
            new Order("301", 959.97, Arrays.asList(
                new OrderItem("7", 1),
                new OrderItem("5", 1),
                new OrderItem("2", 17)
            ))
        ));
    }

    public List<Order> listByUser(String userId, int limit) {
        List<Order> userOrders = MOCK_ORDERS.getOrDefault(userId, new ArrayList<>());
        return userOrders.stream()
                .limit(limit)
                .collect(Collectors.toList());

        /**
         * // 示例后端：GET http://order-svc/orders?userId=..&limit=..
         *         String url = "http://localhost:9002/orders?userId=" + userId + "&limit=" + limit;
         *         String json = rest.get(url);
         *         try {
         *             var arr = mapper.readTree(json);
         *             List<Order> out = new ArrayList<>();
         *             for (var node : arr) {
         *                 String id = node.get("id").asText();
         *                 double total = node.get("totalPrice").asDouble();
         *                 List<OrderItem> items = mapper.convertValue(
         *                     node.get("items"), new TypeReference<List<OrderItem>>() {});
         *                 out.add(new Order(id, total, items));
         *             }
         *             return out;
         *         } catch (Exception e) {
         *             throw new RuntimeException(e);
         *         }
         */
    }
}
