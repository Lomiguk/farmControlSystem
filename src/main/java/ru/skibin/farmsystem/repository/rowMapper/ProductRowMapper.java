package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.RowMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductEntity> {
    @Override
    public ProductEntity mapRow(ResultSet rs, int rowNum) {
        try {
            return new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    ValueType.valueOf(rs.getString("value"))
            );
        } catch (SQLException e) {
            throw new RowMapperException("Product entity map exception");
        }
    }
}
