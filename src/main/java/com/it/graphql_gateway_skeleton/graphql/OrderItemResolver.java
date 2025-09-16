package com.it.graphql_gateway_skeleton.graphql;

import com.it.graphql_gateway_skeleton.config.DataLoaderConfig;
import com.it.graphql_gateway_skeleton.entity.OrderItem;
import com.it.graphql_gateway_skeleton.entity.Product;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;

/**
 * @Author Andy wang
 * @Created 2025/9/12
 */
@Controller
public class OrderItemResolver {


    @SchemaMapping(typeName = "OrderItem", field = "product")
    public CompletableFuture<Product> product(OrderItem item,
        @ContextValue(DataLoaderConfig.PRODUCT_LOADER) DataLoader<String, Product> dl) {
        return dl.load(item.productId);
    }
}
