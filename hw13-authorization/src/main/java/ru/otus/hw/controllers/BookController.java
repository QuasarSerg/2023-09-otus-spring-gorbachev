package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> listBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDto getBook(@PathVariable("id") long id) {
        return bookService.findById(id);
    }

    @PostMapping("/api/v1/books")
    public BookDto addBook(@RequestBody @Valid BookDto book) {
        return bookService.insert(book);
    }

    @PutMapping("/api/v1/books")
    public BookDto updateBook(@RequestBody @Valid BookDto book) {
        return bookService.update(book);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook (@PathVariable("id") long id) {
        bookService.deleteById(id);
    }

}