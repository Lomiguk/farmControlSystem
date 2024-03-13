package ru.skibin.farmsystem.repository.rowMapper;

import ru.skibin.farmsystem.api.data.enumTypes.Role;
import ru.skibin.farmsystem.entity.ProfileEntity;
import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.exception.common.RowMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileRowMapper implements RowMapper<ProfileEntity> {

    @Override
    public ProfileEntity mapRow(ResultSet rs, int rowNum) {
        try {
            return new ProfileEntity(
                    rs.getLong("id"),
                    rs.getString("fio"),
                    rs.getString("email"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("role")),
                    rs.getBoolean("is_actual")
            );
        } catch (SQLException e) {
            throw new RowMapperException("Profile entity map exception: " + e.getMessage());
        }
    }
}
