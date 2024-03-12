package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.api.enumTypes.JwtType;
import ru.skibin.farmsystem.entity.JwtToken;
import ru.skibin.farmsystem.repository.rowMapper.JwtTokenRowMapper;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JwtDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void addJwtToken(Long profileId, String token, JwtType type) {
        String sql = """
                INSERT INTO jwt_token (profile_id, token, type)
                VALUES (:profile_id, :token, :type);
                """;
        Map<String, Object> params = Map.of(
                "profile_id", profileId,
                "token", token,
                "type", type.getName()
        );
        jdbcTemplate.update(sql, params);
    }

    public JwtToken findToken(String token) {
        String sql = """
                SELECT id, profile_id, token, type
                FROM jwt_token
                WHERE token = :token;
                """;
        Map<String, Object> params = Map.of(
                "token", token
        );

        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new JwtTokenRowMapper()));
    }

    public int deleteToken(Long id) {
        String sql = """
                DELETE FROM jwt_token
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );

        return jdbcTemplate.update(sql, params);
    }

    public int deleteProfileTokens(Long profileId) {
        String sql = """
                DELETE FROM jwt_token
                WHERE profile_id = :profile_id;
                """;
        Map<String, Object> params = Map.of(
                "profile_id", profileId
        );

        return jdbcTemplate.update(sql, params);
    }
}
