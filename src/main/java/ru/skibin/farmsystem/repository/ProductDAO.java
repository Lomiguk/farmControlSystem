package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.repository.rowMapper.ProductRowMapper;

import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProductDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void addProduct(String name, ValueType valueType) {
        String sql = """
                INSERT INTO product (name, value)
                VALUES (:name, :value::value_type);
                """;
        Map<String, Object> params = Map.of(
                "name", name,
                "value", valueType.name()
        );
        jdbcTemplate.update(sql, params);
    }

    public ProductEntity getProductById(Long id) {
        String sql = """
                SELECT *
                FROM product
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );
        return jdbcTemplate.queryForObject(sql, params, new ProductRowMapper());
    }

    public ProductEntity getProductByName(String name) {
        String sql = """
                SELECT *
                FROM product
                WHERE name = :name;
                """;
        Map<String, Object> params = Map.of(
                "name", name
        );
        return jdbcTemplate.queryForObject(sql, params, new ProductRowMapper());
    }

    public Collection<ProductEntity> getProductAllWithPagination(Integer limit, Integer offset) {
        String sql = """
                SELECT *
                FROM product
                ORDER BY id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new ProductRowMapper());
    }

    public void updateProductName(Long id, String newName) {
        String sql = """
                UPDATE product
                SET name = :name
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "name", newName,
                "id", id
        );
        jdbcTemplate.update(sql, params);
    }

    public void updateProductValueType(Long id, ValueType valueType) {
        String sql = """
                UPDATE product
                SET value = :value
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "value", valueType.name(),
                "id", id
        );
        jdbcTemplate.update(sql, params);
    }

    public void updateProduct(Long id, String name, ValueType valueType) {
        String sql = """
                UPDATE product
                SET name = :name, value = :value
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "name", name,
                "value", valueType.name(),
                "id", id
        );
        jdbcTemplate.update(sql, params);
    }

    public int deleteProduct(Long id) {
        String sql = """
                DELETE FROM product
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );

        return jdbcTemplate.update(sql, params);
    }
}
