package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.entity.MarkEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarkRowMapper implements RowMapper<MarkEntity> {
    @Override
    public MarkEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MarkEntity(
                rs.getLong("id"),
                rs.getLong("profile_id"),
                rs.getInt("mark"),
                rs.getDate("birth_date").toLocalDate()
        );
    }
}
