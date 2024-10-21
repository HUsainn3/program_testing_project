package com.bookstore.repository;

import com.bookstore.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepositoryInterface {
    List<Book> getAllBooks();
    List<Book> searchBooks(String keyword);
    Optional<Book> getBookById(Long id);
    boolean purchaseBook(Long id);
}
