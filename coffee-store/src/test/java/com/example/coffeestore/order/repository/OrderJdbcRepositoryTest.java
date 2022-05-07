package com.example.coffeestore.order.repository;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static org.junit.jupiter.api.Assertions.*;

import com.example.coffeestore.order.domain.Email;
import com.example.coffeestore.order.domain.Order;
import com.example.coffeestore.order.domain.OrderItem;
import com.example.coffeestore.order.domain.OrderStatus;
import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import com.example.coffeestore.product.repository.ProductRepository;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class OrderJdbcRepositoryTest {

    static EmbeddedMysql embeddedMysql;

    @BeforeAll
    static void setup() {
        MysqldConfig config = aMysqldConfig(v5_7_latest)
            .withCharset(Charset.UTF8)
            .withPort(2215)
            .withUser("test", "test1234!")
            .withTimeZone("Asia/Seoul")
            .build();
        embeddedMysql = anEmbeddedMysql(config)
            .addSchema("test-order_mgmt", classPathScript("schema.sql"))
            .start();
    }

    @AfterAll
    static void cleanup() {
        embeddedMysql.stop();
    }

    @Autowired
    OrderJdbcRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void afterEach() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 추가 성공")
    void testInsert() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "coffee", Category.COFFEE_BEAN_PACKAGE, 1000L);
        productRepository.insert(product);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(1L, orderId, productId, Category.COFFEE_BEAN_PACKAGE, 1000L, 3));
        Order newOrder = new Order(orderId, "example@gamil.com", "Seoul", "010", orderItems, OrderStatus.ACCEPTED);
        orderRepository.insert(newOrder);
        Optional<Order> findOrder = orderRepository.findById(newOrder.getOrderId());
        Assertions.assertThat(findOrder.isEmpty()).isFalse();
    }
}