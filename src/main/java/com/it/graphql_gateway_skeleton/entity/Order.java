package com.it.graphql_gateway_skeleton.entity;

import java.util.List;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
public class Order {

    public String id;
    public double totalPrice;
    public List<OrderItem> items;

    public Order(String id, double totalPrice, List<OrderItem> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.items = items;
    }
}
