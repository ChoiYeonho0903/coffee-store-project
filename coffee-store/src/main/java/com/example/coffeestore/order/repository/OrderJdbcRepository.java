package com.example.coffeestore.order.repository;

import com.example.coffeestore.order.domain.Email;
import com.example.coffeestore.order.domain.Order;
import com.example.coffeestore.order.domain.OrderItem;
import com.example.coffeestore.order.domain.OrderStatus;
import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.util.JdbcUtils;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Order> findAll() {
        String orderSql = "SELECT * FROM orders";
        String orderItemSql = "SELECT * FROM order_item WHERE order_id = UNHEX(REPLACE(:orderId, '-', ''))";
        List<Order> orders = jdbcTemplate.query(orderSql, orderRowMapper);
        orders.forEach((order) -> {
            List<OrderItem> orderItems = jdbcTemplate.query(orderItemSql,
                Collections.singletonMap("orderId", order.getOrderId().toString().getBytes()), orderItemRowMapper);
            order.setOrderItems(orderItems);
        });
        return orders;
    }

    @Override
    public Order insert(Order order) {
        String orderSql = "INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) "
            + "VALUES (UNHEX(REPLACE(:orderId, '-', '')), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)";
        String orderItemsSql = "INSERT INTO order_item(order_id, product_id, category, price, quantity, created_at, updated_at) "
            + "VALUES (UNHEX(REPLACE(:orderId, '-', '')), UNHEX(REPLACE(:productId, '-', '')), :category, :price, :quantity, :createdAt, :updatedAt)";
        jdbcTemplate.update(orderSql, toOrderParamMap(order));
        order.getOrderItems()
            .forEach(item -> jdbcTemplate.update(orderItemsSql, toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
        return order;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        String orderSql = "SELECT * FROM orders WHERE order_id = UNHEX(REPLACE(:orderId, '-', ''))";
        String orderItemSql = "SELECT * FROM order_item WHERE order_id = UNHEX(REPLACE(:orderId, '-', ''))";
        try {
            List<OrderItem> orderItems = jdbcTemplate.query(orderItemSql,
                Collections.singletonMap("orderId", orderId.toString().getBytes()),
                orderItemRowMapper);
            Order order = jdbcTemplate.queryForObject(orderSql,
                Collections.singletonMap("orderId", orderId.toString().getBytes()), orderRowMapper);
            order.setOrderItems(orderItems);
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(UUID orderId) {
        String sql = "DELETE FROM orders WHERE order_id = UNHEX(REPLACE(:orderId, '-', ''))";
        jdbcTemplate.update(sql, Collections.singletonMap("orderId", orderId.toString().getBytes()));
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM orders";
        jdbcTemplate.update(sql, Collections.emptyMap());
    }

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        UUID orderId = JdbcUtils.toUUId(resultSet.getBytes("order_id"));
        Email email = new Email(resultSet.getString("email"));
        String address = resultSet.getString("address");
        String postcode = resultSet.getString("postcode");
        OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        LocalDateTime createdAt = JdbcUtils.toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = JdbcUtils.toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, email, address, postcode, orderStatus, createdAt, updatedAt);
    };

    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        long id = resultSet.getLong("seq");
        UUID orderId = JdbcUtils.toUUId(resultSet.getBytes("order_id"));
        UUID productId = JdbcUtils.toUUId(resultSet.getBytes("order_id"));
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        int quantity = resultSet.getInt("quantity");
        return new OrderItem(id, orderId, productId, category, price, quantity);
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", JdbcUtils.toTimestamp(order.getCreatedAt()));
        paramMap.put("updatedAt", JdbcUtils.toTimestamp(order.getUpdatedAt()));
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.getProductId().toString().getBytes());
        paramMap.put("category", item.getCategory().toString());
        paramMap.put("price", item.getPrice());
        paramMap.put("quantity", item.getQuantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }
}
