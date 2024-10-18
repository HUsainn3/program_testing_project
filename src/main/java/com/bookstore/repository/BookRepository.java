package com.bookstore.repository;

import com.bookstore.model.Book;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        // Add some sample books
        books.add(new Book(1L, "Effective Java", "Joshua Bloch", 40.00, 10));
        books.add(new Book(2L, "Clean Code", "Robert Martin", 35.00, 8));
        books.add(new Book(3L, "Java Concurrency in Practice", "Brian Goetz", 45.00, 5));
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);  // Return a copy to avoid external modification
    }

    // Search books by title or author
    public List<Book> searchBooks(String keyword) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || book.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Get a book by its ID
    public Optional<Book> getBookById(Long id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    // New method to handle purchasing of a book
    public boolean purchaseBook(Long id) {
        Optional<Book> bookOptional = getBookById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getQuantity() > 0) {
                book.setQuantity(book.getQuantity() - 1);  // Decrease quantity by 1
                return true;  // Purchase successful
            } else {
                return false;  // Out of stock
            }
        }
        return false;  // Book not found
    }
}