package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.entity.MarkEntity;
import ru.skibin.farmsystem.repository.rowMapper.MarkRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MarkDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Long save(Long profileId, Integer mark, LocalDate date) {
        String sql = """
                INSERT INTO day_mark (profile_id, mark, date)
                VALUES (:profile_id, :mark, :date)
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
                SELECT id, profile_id, mark, date
                FROM day_mark
                WHERE id = :id;
                """;
        var params = Map.of(
                "id", markId
        );
        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new MarkRowMapper()));
    }

    public Collection<MarkEntity> findByProfileId(Long profileId, Integer limit, Integer offset) {
        String sql = """
                SELECT id, profile_id, mark, date
                FROM day_mark
                WHERE profile_id = :id
                LIMIT :limit
                OFFSET :offset;
                """;
        var params = Map.of(
                "id", profileId,
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new MarkRowMapper());
    }

    public Collection<MarkEntity> findAllByPeriod(LocalDate start, LocalDate end, Integer limit, Integer offset) {
        String sql = """
                SELECT id, profile_id, mark, date
                FROM day_mark
                WHERE date >= :start
                  AND date <= :end
                LIMIT :limit
                OFFSET :offset;
                """;
        var params = Map.of(
                "start", start,
                "end", end,
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new MarkRowMapper());
    }

    public Collection<MarkEntity> findAllByDay(LocalDate day, Integer limit, Integer offset) {
        String sql = """
                SELECT id, profile_id, mark, date
                FROM day_mark
                WHERE DATE(date) = :day
                LIMIT :limit
                OFFSET :offset;
                """;
        var params = Map.of(
                "day", day,
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new MarkRowMapper());
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
