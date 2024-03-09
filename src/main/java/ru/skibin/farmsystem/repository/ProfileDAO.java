package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.repository.rowMapper.ProfileRowMapper;

import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProfileDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void add(String fio, String email, String password) {
        String sql = """
                INSERT INTO profile (fio, email, password)
                VALUES (:fio, :email, :password);
                """;
        Map<String, Object> params = Map.of(
                "fio", fio,
                "email", email,
                "password", password
        );
        jdbcTemplate.update(sql, params);
    }

    public void add(String fio, String email, String password, Boolean isAdmin) {
        String sql = """
                INSERT INTO profile (fio, email, password, is_admin)
                VALUES (:fio, :email, :password, :is_admin);
                """;
        Map<String, Object> params = Map.of(
                "fio", fio,
                "email", email,
                "password", password,
                "is_admin", isAdmin
        );
        jdbcTemplate.update(sql, params);
    }

    public ProfileEntity findProfile(String fio, String email) {
        String sql = """
                SELECT id, fio, email, password, is_admin, is_actual
                FROM profile
                WHERE fio = :fio AND email = :email;
                """;
        Map<String, Object> params = Map.of(
                "fio", fio,
                "email", email
        );

        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new ProfileRowMapper()));
    }

    public ProfileEntity findProfile(Long id) {
        String sql = """
                SELECT id, fio, email, password, is_admin, is_actual
                FROM profile
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );

        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new ProfileRowMapper()));
    }

    public void updateProfileInformation(Long id, String fio, String email) {
        String sql = """
                UPDATE profile
                SET fio = :fio, email = :email
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "fio", fio,
                "email", email
        );

        jdbcTemplate.update(sql, params);
    }

    public void updatePassword(Long id, String newPassHash) {
        String sql = """
                UPDATE profile
                SET password = :password
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "password", newPassHash
        );

        jdbcTemplate.update(sql, params);
    }

    public void updateProfileAdminStatus(Long id, Boolean isAdmin) {
        String sql = """
                UPDATE profile
                SET is_admin = :is_admin
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "is_admin", isAdmin
        );

        jdbcTemplate.update(sql, params);
    }

    public void updateProfileActiveStatus(Long id, Boolean isActual) {
        String sql = """
                UPDATE profile
                SET is_actual = :is_actual
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "is_actual", isActual
        );

        jdbcTemplate.update(sql, params);
    }

    public int deleteProfile(Long id) {
        String sql = """
                DELETE FROM profile
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );

        return jdbcTemplate.update(sql, params);
    }

    public void updateProfile(
            Long id,
            String fio,
            String email,
            String password,
            Boolean isAdmin,
            Boolean isActual
    ) {
        String sql = """
                UPDATE profile
                SET fio = :fio, email = :email, password = :password, is_admin = :is_admin, is_actual = :is_actual
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "fio", fio,
                "email", email,
                "password", password,
                "is_admin", isAdmin,
                "is_actual", isActual
        );

        jdbcTemplate.update(sql, params);
    }

    public Collection<ProfileEntity> getAllProfileWithPagination(Integer limit, Integer offset) {
        String sql = """
                SELECT *
                FROM profile
                ORDER BY id
                LIMIT :limit
                OFFSET :offset;
                """;
        Map<String, Object> params = Map.of(
                "limit", limit,
                "offset", offset
        );
        return jdbcTemplate.query(sql, params, new ProfileRowMapper());
    }
}
