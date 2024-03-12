package ru.skibin.farmsystem.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skibin.farmsystem.api.enumTypes.Role;
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

    public void add(String fio, String email, String password, Role role) {
        String sql = """
                INSERT INTO profile (fio, email, password, role)
                VALUES (:fio, :email, :password, :role::profile_status);
                """;
        Map<String, Object> params = Map.of(
                "fio", fio,
                "email", email,
                "password", password,
                "role", role.name()
        );
        jdbcTemplate.update(sql, params);
    }

    public ProfileEntity findProfile(String fio, String email) {
        String sql = """
                SELECT id, fio, email, password, role, is_actual
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
                SELECT id, fio, email, password, role, is_actual
                FROM profile
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id
        );

        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new ProfileRowMapper()));
    }

    public void updateProfileInformation(Long id, String fio) {
        String sql = """
                UPDATE profile
                SET fio = :fio
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "fio", fio
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

    public void updateProfileRole(Long id, Role role) {
        String sql = """
                UPDATE profile
                SET role = :role::profile_status
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "role", role
        );

        jdbcTemplate.update(sql, params);
    }

    public void updateProfileActualStatus(Long id, Boolean isActual) {
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
            Role role,
            Boolean isActual
    ) {
        String sql = """
                UPDATE profile
                SET fio = :fio, email = :email, password = :password, role = :role::profile_status, is_actual = :is_actual
                WHERE id = :id;
                """;
        Map<String, Object> params = Map.of(
                "id", id,
                "fio", fio,
                "email", email,
                "password", password,
                "role", role,
                "is_actual", isActual
        );

        jdbcTemplate.update(sql, params);
    }

    public Collection<ProfileEntity> getAllProfileWithPagination(Integer limit, Integer offset) {
        String sql = """
                SELECT id, fio, email, password, role, is_actual
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

    public ProfileEntity findProfileByEmail(String email) {
        String sql = """
                SELECT id, fio, email, password, role, is_actual
                FROM profile
                WHERE email = :email
                """;
        Map<String, Object> params = Map.of(
                "email", email
        );
        return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new ProfileRowMapper()));
    }
}
