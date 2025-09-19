package com.it.graphql_gateway_skeleton.resolver;

import com.it.graphql_gateway_skeleton.entity.Order;
import com.it.graphql_gateway_skeleton.entity.User;
import com.it.graphql_gateway_skeleton.service.OrderService;
import com.it.graphql_gateway_skeleton.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;


import java.util.List;

@Controller
public class UserResolver {
    private final OrderService orders;

    private final UserService users;


    public UserResolver(OrderService orders, UserService users) {
        this.users = users;
        this.orders = orders;
    }


    @SchemaMapping(typeName = "User", field = "orders")
    public List<Order> orders(User user, @Argument int limit) {
        return orders.listByUser(user.id, limit);
    }

    @QueryMapping
    public User getUser(@Argument String id) {
        return users.getById(id);
    }

    @MutationMapping
    public void updateUser(@Argument String id, @Argument String name) {
        users.updateUser(id, name);
    }
}
