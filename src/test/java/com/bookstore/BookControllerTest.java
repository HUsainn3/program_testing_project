package com.bookstore;

import com.bookstore.controller.BookController;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.ui.ConcurrentModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookControllerTest {

    private BookController bookController;
    private BookRepository bookRepository;
    private Model model;

    @BeforeEach
    void setUp() {
        // Initialize the controller with a real repository
        bookRepository = new BookRepository();
        bookController = new BookController();
        model = new ConcurrentModel();  // Using Spring's ConcurrentModel for testing purposes
    }

    @Test
    void testGetAllBooks_NoSearch() {
        String viewName = bookController.getAllBooks(null, model);

        assertEquals("bookList", viewName);  // Ensure the view name is correct
        List<Book> books = (List<Book>) model.getAttribute("books");
        assertNotNull(books);
        assertEquals(3, books.size());  // 3 books are added in the repository
        assertEquals("Showing all books", model.getAttribute("message"));
    }

    @Test
    void testGetAllBooks_WithSearch() {
        String viewName = bookController.getAllBooks("Java", model);

        assertEquals("bookList", viewName);
        List<Book> books = (List<Book>) model.getAttribute("books");
        assertNotNull(books);
        assertEquals(2, books.size());  // Two books contain "Java" in the title
        assertEquals("Showing results for: Java", model.getAttribute("message"));
    }

    @Test
    void testGetAllBooks_NoBooksFound() {
        String viewName = bookController.getAllBooks("Nonexistent", model);

        assertEquals("bookList", viewName);
        List<Book> books = (List<Book>) model.getAttribute("books");
        assertTrue(books.isEmpty());  // Ensure no books are found
        assertEquals("No books found", model.getAttribute("message"));
    }

    @Test
    void testGetBookDetails_BookExists() {
        String viewName = bookController.getBookDetails(1L, model);

        assertEquals("bookDetail", viewName);
        Book book = (Book) model.getAttribute("book");
        assertNotNull(book);
        assertEquals("Effective Java", book.getTitle());
    }

    @Test
    void testGetBookDetails_BookDoesNotExist() {
        String viewName = bookController.getBookDetails(999L, model);

        assertEquals("error", viewName);  // Ensure the error page is returned
        assertEquals("Book not found!", model.getAttribute("message"));
    }

    @Test
    void testPurchaseBook_Success() {
        String viewName = bookController.purchaseBook(1L, model);

        assertEquals("bookDetail", viewName);
        Book book = (Book) model.getAttribute("book");
        assertNotNull(book);
        assertEquals("Effective Java", book.getTitle());
        assertEquals(9, book.getQuantity());  // Quantity should reduce by 1
        assertEquals("Purchase successful! 9 left in stock.", model.getAttribute("message"));
    }

    @Test
    void testPurchaseBook_OutOfStock() {
        // Purchase the book until it's out of stock
        for (int i = 0; i < 10; i++) {
            bookController.purchaseBook(1L, model);
        }

        String viewName = bookController.purchaseBook(1L, model);

        assertEquals("bookDetail", viewName);
        Book book = (Book) model.getAttribute("book");
        assertNotNull(book);
        assertEquals(0, book.getQuantity());  // Stock is zero now
        assertEquals("Purchase failed! Book is out of stock or unavailable.", model.getAttribute("message"));
    }

    @Test
    void testPurchaseBook_BookDoesNotExist() {
        String viewName = bookController.purchaseBook(999L, model);

        assertEquals("error", viewName);
        assertEquals("Book not found!", model.getAttribute("message"));
    }
}
