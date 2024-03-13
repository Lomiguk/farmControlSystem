package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.entity.WorkResultEntity;
import ru.skibin.farmsystem.entity.StatisticRow;
import ru.skibin.farmsystem.entity.WorkerEntity;
import ru.skibin.farmsystem.repository.rowMapper.ProductDataRowMapper;
import ru.skibin.farmsystem.repository.rowMapper.StatisticRowMapper;
import ru.skibin.farmsystem.repository.rowMapper.WorkerEntityRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StatisticDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Collection<StatisticRow> getWorkerWithResultsByDay(LocalDate date, Integer limit, Integer offset) {
        String sql = """
                SELECT pf.id prId, pf.fio prFio, pf.email prEmail, pf.role prRole, pd.id pdId, pd.name pdName, pd.value pdValue, SUM(a.value) count
                FROM profile pf
                JOIN public.action a on pf.id = a.profile_id
                JOIN public.product pd on pd.id = a.product_id
                WHERE DATE(a.time) = :date
                GROUP BY pf.id, pd.id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "date", date,
                "limit", limit,
                "offset", offset
        );

        return jdbcTemplate.query(sql, params, new StatisticRowMapper());
    }

    public Collection<StatisticRow> getWorkerWithResultsByPeriod(LocalDate start, LocalDate end, Integer limit, Integer offset) {
        String sql = """
                SELECT pf.id prId, pf.fio prFio, pf.email prEmail, pf.role prRole, pd.id pdId, pd.name pdName, pd.value pdValue, SUM(a.value) count
                FROM profile pf
                JOIN public.action a on pf.id = a.profile_id
                JOIN public.product pd on pd.id = a.product_id
                WHERE time >= :start
                  AND time <= :end
                GROUP BY pf.id, pd.id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "start_date", start,
                "end_date", end,
                "limit", limit,
                "offset", offset
        );

        return jdbcTemplate.query(sql, params, new StatisticRowMapper());
    }

    public Collection<WorkerEntity> getWorkersByDay(LocalDate day, Integer limit, Integer offset) {
        String sql = """
                SELECT pf.id prId, pf.fio prFio, pf.email prEmail, pf.role prRole
                FROM profile pf
                JOIN public.action a on pf.id = a.profile_id
                WHERE DATE(a.time) = :date
                GROUP BY pf.id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "date", day,
                "limit", limit,
                "offset", offset
        );

        return jdbcTemplate.query(sql, params, new WorkerEntityRowMapper());
    }

    public Collection<WorkerEntity> getWorkersByPeriod(LocalDate start, LocalDate end, Integer limit, Integer offset) {
        String sql = """
                SELECT pf.id prId, pf.fio prFio, pf.email prEmail, pf.role prRole
                FROM profile pf
                JOIN public.action a on pf.id = a.profile_id
                WHERE time >= :start
                  AND time <= :end
                GROUP BY pf.id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "limit", limit,
                "offset", offset
        );

        return jdbcTemplate.query(sql, params, new WorkerEntityRowMapper());
    }

    public Collection<WorkResultEntity> getProductsByDay(LocalDate day, Integer limit, Integer offset) {
        String sql = """
                SELECT pd.id pdId, pd.name pdName, pd.value pdValue, SUM(a.value) count
                FROM product pd
                JOIN public.action a on pd.id = a.product_id
                WHERE DATE(a.time) = :date
                GROUP BY pd.id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "date", day,
                "limit", limit,
                "offset", offset
        );

        return jdbcTemplate.query(sql, params, new ProductDataRowMapper());
    }

    public Collection<WorkResultEntity> getProductsByPeriod(
            LocalDate start,
            LocalDate end,
            Integer limit,
            Integer offset
    ) {
        String sql = """
                SELECT pd.id pdId, pd.name pdName, pd.value pdValue, SUM(a.value) count
                FROM product pd
                JOIN public.action a on pd.id = a.product_id
                WHERE time >= :start
                  AND time <= :end
                GROUP BY pd.id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "limit", limit,
                "offset", offset
        );

        return jdbcTemplate.query(sql, params, new ProductDataRowMapper());
    }
}
