package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.api.data.enumTypes.Role;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.StatisticRow;
import ru.skibin.farmsystem.exception.common.RowMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticRowMapper implements RowMapper<StatisticRow> {
    @Override
    public StatisticRow mapRow(ResultSet rs, int rowNum) {
        try {
            return new StatisticRow(
                    rs.getLong("prId"),
                    rs.getString("prFio"),
                    rs.getString("prEmail"),
                    Role.valueOf(rs.getString("prRole")),
                    rs.getLong("pdId"),
                    rs.getString("pdName"),
                    ValueType.valueOf(rs.getString("pdValue")),
                    rs.getFloat("count")
            );
        } catch (SQLException e) {
            throw new RowMapperException("Statistic row map exception: " + e.getMessage());
        }
    }
}
