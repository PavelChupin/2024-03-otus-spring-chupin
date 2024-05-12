package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        final SqlParameterSource param = new MapSqlParameterSource("id", id);
        return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(" select bk.id as book_id " +
                        ", bk.title     as book_title " +
                        ", ar.id        as author_id " +
                        ", ar.full_name as author_full_name " +
                        ", ge.id        as genre_id" +
                        ", ge.name      as genre_name " +
                        "  from books bk " +
                        " inner join authors ar " +
                        "    on ar.id = bk.author_id " +
                        " inner join genres ge " +
                        "    on ge.id = bk.genre_id " +
                        " where bk.id = :id "
                , param
                , new BookRowMapper()));
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.getJdbcOperations().query(" select bk.id as book_id " +
                        ", bk.title     as book_title " +
                        ", ar.id        as author_id " +
                        ", ar.full_name as author_full_name " +
                        ", ge.id        as genre_id" +
                        ", ge.name      as genre_name " +
                        "  from books bk " +
                        " inner join authors ar " +
                        "    on ar.id = bk.author_id " +
                        " inner join genres ge " +
                        "    on ge.id = bk.genre_id "
                , new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        final SqlParameterSource param = new MapSqlParameterSource("id", id);
        namedParameterJdbcOperations.update(" delete from books where id = :id ", param);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        final SqlParameterSource param = new MapSqlParameterSource("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("genre_id", book.getGenre().getId())
                .addValue("author_id", book.getAuthor().getId());

        final String[] keyColumn = {"id"};

        namedParameterJdbcOperations.update(" insert into books " +
                        " (title, author_id, genre_id) " +
                        " values(:title, :author_id, :genre_id) "
                , param
                , keyHolder
                , keyColumn);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        final SqlParameterSource param = new MapSqlParameterSource("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("genre_id", book.getGenre().getId())
                .addValue("author_id", book.getAuthor().getId());

        int rowcount = namedParameterJdbcOperations.update(" update books " +
                        "set title = :title " +
                        "   ,author_id = :author_id " +
                        "   ,genre_id = :genre_id " +
                        " where id = :id "
                , param);

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        if (rowcount == 0) {
            throw new EntityNotFoundException("Zero records were updated");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            final var author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
            final var genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            return new Book(rs.getLong("book_id"), rs.getString("book_title"), author, genre);
        }
    }
}
