package org.example.app.services;

import org.example.web.dto.Book;
import org.example.web.dto.FilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean remove(String typeRemove, Object valueRemove) throws NumberFormatException {
        return  bookRepo.remove(typeRemove, valueRemove);
    }

    public void initFilter(FilterForm filterForm) { bookRepo.initFilter(filterForm); }
}
