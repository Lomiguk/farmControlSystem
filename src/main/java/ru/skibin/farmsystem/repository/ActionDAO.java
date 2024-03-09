package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.repository.rowMapper.ActionRowMapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ActionDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void addAction(Long profileId, Long productId, Float value, Instant time) {
        String sql = """
                INSERT INTO action (profile_id, product_id, value, time)
                VALUES (:profile_id, :product_id, :value, :time);
                """;
        Map<String, Object> params = Map.of(
                "profile_id", profileId,
                "product_id", productId,
                "value", value,
                "time", Timestamp.from(time)
        );
        jdbcTemplate.update(sql, params);
    }

    public ActionEntity findAction(Long id) {
        String sql = """
                SELECT id, profile_id, product_id, value, time, is_actual
                FROM action
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );
        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new ActionRowMapper()));
    }

    public Collection<ActionEntity> findProfileActions(Long profileId, Integer limit, Integer offset) {
        String sql = """
                SELECT id, profile_id, product_id, value, time, is_actual
                FROM action
                WHERE profile_id = :profile_id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "profile_id", profileId,
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new ActionRowMapper());
    }

    public Collection<ActionEntity> findProductActions(Long productId, Integer limit, Integer offset) {
        String sql = """
                SELECT id, profile_id, product_id, value, time, is_actual
                FROM action
                WHERE product_id = :product_id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "product_id", productId,
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new ActionRowMapper());
    }

    public Collection<ActionEntity> findDayActions(Date time, Integer limit, Integer offset) {
        String sql = """
                SELECT id, profile_id, product_id, value, time, is_actual
                FROM action
                WHERE DATE(time) = :day
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "day", time,
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new ActionRowMapper());
    }

    public Collection<ActionEntity> findPeriodActions(Date start, Date end, Integer limit, Integer offset) {
        String sql = """
                SELECT id, profile_id, product_id, value, time, is_actual
                FROM action
                WHERE DATE(time) => :start
                  AND DATE(time) <= :end
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new ActionRowMapper());
    }

    public ActionEntity findProfileActionByProductAndTime(Long profileId, Long productId, Instant time) {
        String sql = """
                    SELECT id, profile_id, product_id, value, time, is_actual
                    FROM action
                    WHERE profile_id = :profile_id AND product_id = :product_id AND time = :time
                    ORDER BY time DESC;
                    """;
        Map<String, Object> params = Map.of(
                "profile_id", profileId,
                "product_id", productId,
                "time", Timestamp.from(time)
        );
        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new ActionRowMapper()));
    }

    public void updateAction(Long id, Long profile_id, Long product_id, Float value, Instant time, Boolean isActual) {
        String sql = """
                UPDATE action
                SET profile_id = :profile_id,
                    product_id = :product_id,
                    value = :value,
                    time = :time,
                    is_actual = :is_actual
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "profile_id", profile_id,
                "product_id", product_id,
                "value", value,
                "time", Timestamp.from(time),
                "id", id,
                "is_actual", isActual
        );
        jdbcTemplate.update(sql, params);
    }

    public int deleteAction(Long id) {
        String sql = """
                DELETE FROM action
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );

        return jdbcTemplate.update(sql, params);
    }

}