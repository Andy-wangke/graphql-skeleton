package com.it.graphql_gateway_skeleton.entity;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
public class OrderItem {

    public String productId;
    public int quantity;

    public OrderItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
