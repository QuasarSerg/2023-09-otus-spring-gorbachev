package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configurations.SecurityConfiguration;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user", authorities = "ROLE_USER")
@WebMvcTest({CommentController.class, SecurityConfiguration.class})
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private BookDto book;

    private final List<CommentDto> dbComments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        AuthorDto author = new AuthorDto(1, "Author_%s".formatted(1));
        GenreDto genre = new GenreDto(1, "Genre_%s".formatted(1));
        book = new BookDto(1, "BookTitle_%s".formatted(1), author, genre);

        for (int i = 1; i < 4; i++) {
            dbComments.add(new CommentDto(i, "Comment_%s".formatted(i), book));
        }
    }

    @DisplayName("должен загружать все комментарии для книги")
    @Test
    void shouldReturnAllCommentsByBookId() throws Exception {
        long bookId = book.getId();
        doReturn(dbComments).when(commentService).findAllByBookId(bookId);

        mockMvc.perform(get("/api/v1/books/{id}/comments", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dbComments)));

        verify(commentService, times(1)).findAllByBookId(bookId);
    }

    @DisplayName("Анонимный пользователь. Ошибка при загрузке всех комментариев для книги")
    @Test
    @WithAnonymousUser
    void shouldFailReturnAllCommentsByBookId() throws Exception {
        long bookId = book.getId();
        doReturn(dbComments).when(commentService).findAllByBookId(bookId);

        mockMvc.perform(get("/api/v1/books/{id}/comments", bookId))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}