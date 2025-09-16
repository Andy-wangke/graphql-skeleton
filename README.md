# GraphQL Gateway Skeleton

一个完整的 GraphQL Java 项目骨架，使用 Spring Boot + Spring GraphQL 实现。

## 核心特性

- ✅ **SDL Schema**: 完整的 GraphQL Schema 定义
- ✅ **Resolver**: Query 和字段解析器
- ✅ **DataLoader**: 批量加载，避免 N+1 查询问题
- ✅ **JDK HttpClient**: 下游服务调用
- ✅ **JSON 解析**: 最小化 JSON 解析工具
- ✅ **Spring Boot**: 完整的 Web 应用框架

## 项目结构

```
src/main/java/com/it/graphql_gateway_skeleton/
├── GraphqlGatewaySkeletonApplication.java  # 主应用入口
├── config/
│   └── DataLoaderConfig.java              # DataLoader 配置
├── entity/
│   ├── User.java                          # 用户实体
│   ├── Order.java                         # 订单实体
│   ├── OrderItem.java                     # 订单项实体
│   └── Product.java                       # 产品实体
├── graphql/
│   ├── QueryResolver.java                 # 查询解析器
│   ├── UserResolver.java                  # 用户字段解析器
│   └── OrderItemResolver.java             # 订单项解析器 (使用DataLoader)
├── service/
│   ├── UserService.java                   # 用户服务
│   ├── OrderService.java                  # 订单服务
│   ├── ProductService.java                # 产品服务 (支持批量查询)
│   └── RestClient.java                    # HTTP 客户端
└── util/
    └── JsonParser.java                    # JSON 解析工具
```

## 快速开始

### 1. 启动应用

```bash
mvn spring-boot:run
```

### 2. 访问 GraphiQL

打开浏览器访问: http://localhost:8080/graphiql

### 3. 示例查询

```graphql
query {
  user(id: "1") {
    id
    name
    orders(limit: 5) {
      id
      totalPrice
      items {
        quantity
        product {
          id
          name
          price
        }
      }
    }
  }
}
```

## DataLoader 优化

项目使用 DataLoader 实现批量加载优化：

- **ProductService**: 支持批量产品查询 `getByIds()`
- **DataLoaderConfig**: 配置产品批量加载器
- **OrderItemResolver**: 使用 DataLoader 避免 N+1 查询

## 下游服务模拟

项目期望以下下游服务：

- **用户服务**: http://localhost:9001
- **订单服务**: http://localhost:9002  
- **产品服务**: http://localhost:9003

### 模拟服务响应格式

**GET /users/{id}**:
```json
{"id": "1", "name": "John Doe"}
```

**GET /orders?userId={id}&limit={n}**:
```json
[
  {
    "id": "order1",
    "totalPrice": 99.99,
    "items": [
      {"productId": "prod1", "quantity": 2}
    ]
  }
]
```

**GET /products?ids=1,2,3**:
```json
[
  {"id": "1", "name": "Product A", "price": 29.99},
  {"id": "2", "name": "Product B", "price": 39.99}
]
```

## 技术栈

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring GraphQL**
- **GraphQL Java**
- **DataLoader**
- **JDK HttpClient**
- **Jackson (JSON 处理)**

## 配置

主要配置在 `application.yaml`:

```yaml
server:
  port: 8080

spring:
  graphql:
    graphiql:
      enabled: true
    path: /graphql

logging:
  level:
    com.it.graphql_gateway_skeleton: DEBUG
    org.dataloader: DEBUG
```