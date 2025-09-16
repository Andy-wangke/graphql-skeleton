package com.it.graphql_gateway_skeleton.config;

import com.it.graphql_gateway_skeleton.entity.Product;
import com.it.graphql_gateway_skeleton.service.ProductService;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Configuration
public class DataLoaderConfig {
    public static final String PRODUCT_LOADER = "PRODUCT_LOADER";


    @Bean
    public DataLoaderRegistry registry(ProductService productService) {
        DataLoaderRegistry registry = new DataLoaderRegistry();


        BatchLoader<String, Product> batchLoader = (List<String> ids) -> 
            CompletableFuture.supplyAsync(() -> {
                Map<String, Product> map = productService.getByIds(ids);
                return ids.stream().map(map::get).collect(Collectors.toList());
            });

        registry.register(PRODUCT_LOADER, DataLoader.newDataLoader(batchLoader));
        return registry;
    }
}
