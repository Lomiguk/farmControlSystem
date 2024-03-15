package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.entity.TaskEntity;
import ru.skibin.farmsystem.repository.rowMapper.TaskRowMapper;

import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TaskDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Long add(TaskEntity taskEntity) {
        String sql = """
                INSERT INTO task (start_date, end_date, description, profile_id, product_id, value)
                VALUES (:start_date, :end_date, :description, :profile_id, :product_id, :value)
                RETURNING id;
                """;
        var params = Map.of(
                "start_date", taskEntity.getStartDate(),
                "end_date", taskEntity.getEndDate(),
                "description", taskEntity.getDescription(),
                "profile_id", taskEntity.getProfileId(),
                "product_id", taskEntity.getProductId(),
                "value", taskEntity.getValue()
        );
        return DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, params, ((rs, rowNum) -> rs.getLong("id")))
        );
    }

    public TaskEntity get(Long id) {
        String sql = """
                SELECT id, start_date, end_date, description, profile_id, product_id, value, collected_value, is_done, is_aborted
                FROM task
                WHERE id = :id;
                """;
        var params = Map.of(
                "id", id
        );
        return DataAccessUtils.requiredSingleResult(
                jdbcTemplate.query(sql, params, new TaskRowMapper())
        );
    }


    public Collection<TaskEntity> getByProfile(Long profileId) {
        String sql = """
                SELECT id, start_date, end_date, description, profile_id, product_id, value, collected_value, is_done, is_aborted
                FROM task
                WHERE profile_id = :profile_id;
                """;
        var params = Map.of(
                "profile_id", profileId
        );
        return jdbcTemplate.query(sql, params, new TaskRowMapper());
    }

    public void update(Long id, TaskEntity newEntityData) {
        String sql = """
                UPDATE task
                SET start_date = :start_date,
                    end_date = :end_date,
                    description = :description,
                    profile_id = :profile_id,
                    product_id = :product_id,
                    value = :value,
                    collected_value = :collected_value,
                    is_done = :is_done,
                    is_aborted = :is_aborted
                WHERE id = :id;
                """;
        var params = Map.of(
                "id", id,
                "start_date", newEntityData.getStartDate(),
                "end_date", newEntityData.getEndDate(),
                "description", newEntityData.getDescription(),
                "profile_id", newEntityData.getProfileId(),
                "product_id", newEntityData.getProductId(),
                "value", newEntityData.getValue(),
                "collected_value", newEntityData.getCollectedValue(),
                "is_done", newEntityData.getIsDone(),
                "is_aborted", newEntityData.getIsAborted()
        );
        jdbcTemplate.update(sql, params);
    }

    public void updateValue(Long id, Float newCollectedValue, Boolean isDone) {
        String sql = """
                UPDATE task
                SET collected_value = :collected_value,
                    is_done = :is_done
                WHERE id = :id;
                """;
        var params = Map.of(
                "id", id,
                "collected_value", newCollectedValue,
                "is_done", isDone
        );
        jdbcTemplate.update(sql, params);
    }

    public int delete(Long id) {
        String sql = """
                DELETE FROM task
                WHERE id = :id;
                """;
        var params = Map.of(
                "id", id
        );
        return jdbcTemplate.update(sql, params);
    }
}
