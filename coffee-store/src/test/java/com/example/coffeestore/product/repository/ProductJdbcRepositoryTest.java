package com.example.coffeestore.product.repository;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static org.assertj.core.api.Assertions.*;

import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class ProductJdbcRepositoryTest {

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
    ProductRepository productRepository;

    private static Product defaultProduct;

    @BeforeEach
    void beforeEach() {
        defaultProduct = new Product(UUID.randomUUID(), "default-product", Category.COFFEE_BEAN_PACKAGE, 1000L,
            "description", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        productRepository.insert(defaultProduct);
    }

    @AfterEach
    void afterEach() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품을 추가 성공.")
    void testInsert() {
        Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKAGE, 1000L);
        productRepository.insert(newProduct);
        List<Product> products = productRepository.findAll();
        assertThat(products.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("상품을 이름으로 조회 성공")
    void testFindByName() {
        Optional<Product> product = productRepository.findByName(defaultProduct.getProductName());
        assertThat(product.get()).isEqualTo(defaultProduct);
    }

    @Test
    @DisplayName("상품 아이디로 조회 성공")
    void testFindById() {
        Optional<Product> product = productRepository.findById(defaultProduct.getProductId());
        assertThat(product.get()).isEqualTo(defaultProduct);
    }


    @Test
    @DisplayName("상품들을 카테고리로 조회 성공")
    void testFindByCategory() {
        var products = productRepository.findByCategory(Category.COFFEE_BEAN_PACKAGE);
        assertThat(products.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("상품 수정할 수 있다")
    void testUpdate() {
        defaultProduct.setProductName("updated-product");
        defaultProduct.setPrice(2000L);
        defaultProduct.setDescription("updated-description");
        productRepository.update(defaultProduct);
        Optional<Product> product = productRepository.findById(defaultProduct.getProductId());
        assertThat(product.isEmpty()).isFalse();
    }
}