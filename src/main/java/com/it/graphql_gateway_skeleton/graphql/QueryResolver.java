package com.it.graphql_gateway_skeleton.graphql;

import com.it.graphql_gateway_skeleton.entity.User;
import com.it.graphql_gateway_skeleton.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
@Controller
public class QueryResolver {
    private final UserService users;


    public QueryResolver(UserService users) {
        this.users = users;
    }


    @QueryMapping
    public User user(@Argument String id) {
        return users.getById(id);
    }
}
