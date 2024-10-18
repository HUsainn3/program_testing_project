package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private final BookRepository bookRepository;

    public BookController() {
        this.bookRepository = new BookRepository();
    }

    // Support search functionality
    @GetMapping("/books")
    public String getAllBooks(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Book> books;
        
        if (search != null && !search.isEmpty()) {
            books = bookRepository.searchBooks(search);
            model.addAttribute("message", "Showing results for: " + search);
        } else {
            books = bookRepository.getAllBooks();
            model.addAttribute("message", "Showing all books");
        }

        if (books.isEmpty()) {
            model.addAttribute("message", "No books found");
        }

        model.addAttribute("books", books);
        return "bookList";  // Return bookList.jsp
    }

    // Show book details
    @GetMapping("/books/{id}")
    public String getBookDetails(@PathVariable("id") Long id, Model model) {
        Optional<Book> bookOptional = bookRepository.getBookById(id);

        if (bookOptional.isPresent()) {
            model.addAttribute("book", bookOptional.get());
            return "bookDetail";  // Return bookDetail.jsp
        } else {
            model.addAttribute("message", "Book not found!");
            return "error";  // Return error.jsp
        }
    }

    // New method to handle purchasing of a book
    @GetMapping("/books/purchase/{id}")
    public String purchaseBook(@PathVariable("id") Long id, Model model) {
        boolean success = bookRepository.purchaseBook(id);
        Optional<Book> bookOptional = bookRepository.getBookById(id);
    
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            model.addAttribute("book", book);
            if (success) {
                model.addAttribute("message", "Purchase successful! " + book.getQuantity() + " left in stock.");
            } else {
                model.addAttribute("message", "Purchase failed! Book is out of stock or unavailable.");
            }
            return "bookDetail";  // Stay on the book details page after purchase
        } else {
            model.addAttribute("message", "Book not found!");
            return "error";  // Return error.jsp if the book is not found
        }
    }
}