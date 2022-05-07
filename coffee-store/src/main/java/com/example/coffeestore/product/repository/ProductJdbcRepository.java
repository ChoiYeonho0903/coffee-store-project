package com.example.coffeestore.product.repository;

import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
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
public class ProductJdbcRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public Product insert(Product product) {
        String sql = "INSERT INTO product(product_id, product_name, category, price, description, created_at, updated_at) "
            + "VALUES(UNHEX(REPLACE(:productId, '-', '')), :productName, :category, :price, :description, :createdAt, :updatedAt)";
        int update = jdbcTemplate.update(sql, toParamMap(product));
        if (update != 1) {
            throw new RuntimeException("Nothing was inserted");
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, price = :price, description = :description, created_at = :createdAt, updated_at = :updatedAt "
            + "WHERE product_id = UNHEX(REPLACE(:productId, '-', ''))";
        int update = jdbcTemplate.update(sql, toParamMap(product));
        if (update != 1) {
            throw new RuntimeException("Nothing was updated");
        }
        return product;
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        String sql = "SELECT * FROM product WHERE product_id = UNHEX(REPLACE(:productId, '-', ''))";
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql,
                    Collections.singletonMap("productId", productId.toString().getBytes()), productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String productName) {
        String sql = "SELECT * FROM product WHERE product_name = :productName";
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql,
                    Collections.singletonMap("productName", productName), productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        String sql = "SELECT * FROM product WHERE category = :category";
        return jdbcTemplate.query(sql, Collections.singletonMap("category", category.toString()), productRowMapper);
    }

    @Override
    public void deleteById(UUID productId) {
        String sql = "DELETE FROM product WHERE product_id = UNHEX(REPLACE(:productId, '-', ''))";
        jdbcTemplate.update(sql, Collections.singletonMap("productId", productId.toString().getBytes()));
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM product";
        jdbcTemplate.update(sql, Collections.emptyMap());
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        UUID productId = JdbcUtils.toUUId(resultSet.getBytes("product_id"));
        String productName = resultSet.getString("product_name");
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        String description = resultSet.getString("description");
        LocalDateTime createdAt = JdbcUtils.toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = JdbcUtils.toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap (Product product) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", product.getProductId().toString());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", JdbcUtils.toTimestamp(product.getCreatedAt()));
        paramMap.put("updatedAt", JdbcUtils.toTimestamp(product.getUpdatedAt()));
        return paramMap;
    }
}
