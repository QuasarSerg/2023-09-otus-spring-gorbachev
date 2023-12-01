package ru.otus.hw.repositories;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbc;

    public JdbcAuthorRepository(JdbcOperations jdbc, NamedParameterJdbcOperations namedParameterJdbc) {
        this.jdbc = jdbc;
        this.namedParameterJdbc = namedParameterJdbc;
    }

    @Override
    public List<Author> findAll() {
        return jdbc.query("select id, full_name from authors", new AuthorRowMapper());
    }

    @Override
    public Integer count() {
        Integer count = jdbc.queryForObject("select count(*) from authors", Integer.class);
        return isNull(count) ? 0 : count;
    }

    @Override
    public Optional<Author> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Author author = namedParameterJdbc.queryForObject(
                "select id, full_name from authors where id = :id", params, new AuthorRowMapper());
        return Optional.ofNullable(author);
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
