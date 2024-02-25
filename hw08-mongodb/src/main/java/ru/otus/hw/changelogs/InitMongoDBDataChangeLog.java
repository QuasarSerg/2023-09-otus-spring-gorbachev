package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private final List<Author> authors = new ArrayList<>();

    private final List<Genre> genres = new ArrayList<>();

    private final List<Book> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "gorbachev", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "gorbachev", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        for (int i = 1; i < 4; i++) {
            authors.add(repository.save(new Author("Author_%s".formatted(i))));
        }
    }

    @ChangeSet(order = "002", id = "initGenres", author = "gorbachev", runAlways = true)
    public void initGenres(GenreRepository repository) {
        for (int i = 1; i < 4; i++) {
            genres.add(repository.save(new Genre("Genre_%s".formatted(i))));
        }
    }

    @ChangeSet(order = "003", id = "initBooks", author = "gorbachev", runAlways = true)
    public void initBooks(BookRepository repository) {
        for (int i = 1; i < 4; i++) {
            books.add(repository.save(new Book("BookTitle_%s".formatted(i), authors.get(i - 1), genres.get(i - 1))));
        }
    }

    @ChangeSet(order = "004", id = "initComments", author = "gorbachev", runAlways = true)
    public void initComments(CommentRepository repository) {
        for (int i = 1; i < 4; i++) {
            repository.save(new Comment("CommentText_%s", books.get(i - 1)));
        }
    }
}