package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.api.data.enumTypes.Role;
import ru.skibin.farmsystem.entity.WorkerEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerEntityRowMapper implements RowMapper<WorkerEntity> {
    @Override
    public WorkerEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new WorkerEntity(
                rs.getLong("prId"),
                rs.getString("prFio"),
                rs.getString("prEmail"),
                Role.valueOf(rs.getString("prRole"))
        );
    }
}
