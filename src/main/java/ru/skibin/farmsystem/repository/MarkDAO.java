package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.entity.MarkEntity;
import ru.skibin.farmsystem.repository.rowMapper.MarkRowMapper;

import java.time.LocalDate;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MarkDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public Long save(Long profileId, Integer mark, LocalDate date) {
        String sql = """
                INSERT INTO day_mark (profile_id, mark, date)
                VALUES (:profile_Id, :mark, :date)
                RETURNING id;
                """;
        var params = Map.of(
                "profile_id", profileId,
                "mark", mark,
                "date", date
        );

        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, ((rs, rowNum) -> rs.getLong("id"))));
    }

    public MarkEntity find(Long markId) {
        String sql = """
                SELECT (id, profile_id, mark, date)
                FROM day_mark
                WHERE id = :id;
                """;
        var params = Map.of(
                "id", markId
        );
        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new MarkRowMapper()));
    }

    public Long update(Long markId, Long profileId, Integer mark, LocalDate date) {
        String sql = """
                UPDATE day_mark
                SET profile_id=:profile_id, mark=:mark, date=:date
                WHERE id = :id
                RETURNING id;
                """;
        var params = Map.of(
                "id", markId,
                "profile_id", profileId,
                "mark", mark,
                "date", date
        );
        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, ((rs, rowNum) -> rs.getLong("id"))));
    }

    public Integer delete(Long markId) {
        String sql = """
                DELETE FROM day_mark
                WHERE id = :id
                """;
        var params = Map.of(
                "id", markId
        );
        return jdbcTemplate.update(sql, params);
    }
}
