package com.it.graphql_gateway_skeleton.graphql;

import com.it.graphql_gateway_skeleton.entity.Order;
import com.it.graphql_gateway_skeleton.entity.User;
import com.it.graphql_gateway_skeleton.service.OrderService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;


import java.util.List;

@Controller
public class UserResolver {
    private final OrderService orders;


    public UserResolver(OrderService orders) {
        this.orders = orders;
    }


    @SchemaMapping(typeName = "User", field = "orders")
    public List<Order> orders(User user, @Argument int limit) {
        return orders.listByUser(user.id, limit);
    }
}
