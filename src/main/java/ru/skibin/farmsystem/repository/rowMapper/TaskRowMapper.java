package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.entity.TaskEntity;
import ru.skibin.farmsystem.exception.common.RowMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<TaskEntity> {
    @Override
    public TaskEntity mapRow(ResultSet rs, int rowNum) {
        try {
            return new TaskEntity(
                        rs.getLong("id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getString("description"),
                        rs.getLong("profile_id"),
                        rs.getLong("product_id"),
                        rs.getFloat("value"),
                        rs.getFloat("collected_value"),
                        rs.getBoolean("is_done"),
                        rs.getBoolean("is_aborted")
                    );
        } catch (SQLException e) {
            throw new RowMapperException("Statistic row map exception: " + e.getMessage());
        }
    }
}
