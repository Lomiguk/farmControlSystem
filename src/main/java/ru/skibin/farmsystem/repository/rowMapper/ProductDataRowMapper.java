package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.WorkResultEntity;
import ru.skibin.farmsystem.exception.common.RowMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDataRowMapper implements RowMapper<WorkResultEntity> {
    @Override
    public WorkResultEntity mapRow(ResultSet rs, int rowNum) {
        try {
            return new WorkResultEntity(
                    rs.getLong("pdId"),
                    rs.getString("pdName"),
                    ValueType.valueOf(rs.getString("pdValue")),
                    rs.getFloat("count")
            );
        } catch (SQLException e) {
            throw new RowMapperException("Product row map exception: " + e.getMessage());
        }
    }
}
