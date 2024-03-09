package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.exception.common.RowMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActionRowMapper implements RowMapper<ActionEntity> {
    @Override
    public ActionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new ActionEntity(
                    rs.getLong("id"),
                    rs.getLong("profile_id"),
                    rs.getLong("product_id"),
                    rs.getFloat("value"),
                    rs.getTimestamp("time").toInstant(),
                    rs.getBoolean("is_actual")
            );
        } catch (SQLException e) {
            throw new RowMapperException("Action entity map exception: " + e.getMessage());
        }
    }
}
