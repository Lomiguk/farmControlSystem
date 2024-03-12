package ru.skibin.farmsystem.repository.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skibin.farmsystem.api.enumTypes.JwtType;
import ru.skibin.farmsystem.entity.JwtToken;
import ru.skibin.farmsystem.exception.common.RowMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JwtTokenRowMapper implements RowMapper<JwtToken> {
    @Override
    public JwtToken mapRow(ResultSet rs, int rowNum) {
        try {
            return new JwtToken(
                    rs.getLong("id"),
                    rs.getLong("profile_id"),
                    rs.getString("token"),
                    JwtType.valueOf(rs.getString("type"))
            );
        } catch (SQLException e) {
            throw new RowMapperException("Jwt token entity map exception: " + e.getMessage());
        }
    }
}
