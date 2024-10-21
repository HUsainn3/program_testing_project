package com.bookstore;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepositoryInterface;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryInterfaceTest {

    private BookRepositoryInterface bookRepositoryInterface;

    @BeforeEach
    void setUp() {
        // Initialize the BookRepository using the interface
        bookRepositoryInterface = new BookRepository();
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = bookRepositoryInterface.getAllBooks();

        assertNotNull(books);  // Ensure the list is not null
        assertEquals(3, books.size());  // Ensure it returns the correct number of books (3 added in constructor)
    }

    @Test
    void testSearchBooks_TitleMatch() {
        List<Book> books = bookRepositoryInterface.searchBooks("Effective Java");

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).getTitle());
    }

    @Test
    void testSearchBooks_AuthorMatch() {
        List<Book> books = bookRepositoryInterface.searchBooks("Robert Martin");

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
    }

    @Test
    void testSearchBooks_NoMatch() {
        List<Book> books = bookRepositoryInterface.searchBooks("Non-Existing Book");

        assertNotNull(books);
        assertTrue(books.isEmpty());  // Ensure no books are found
    }

    @Test
    void testGetBookById_BookExists() {
        Optional<Book> bookOptional = bookRepositoryInterface.getBookById(1L);

        assertTrue(bookOptional.isPresent());
        assertEquals("Effective Java", bookOptional.get().getTitle());
    }

    @Test
    void testGetBookById_BookDoesNotExist() {
        Optional<Book> bookOptional = bookRepositoryInterface.getBookById(999L);

        assertFalse(bookOptional.isPresent());
    }

    @Test
    void testPurchaseBook_Success() {
        boolean result = bookRepositoryInterface.purchaseBook(1L);

        assertTrue(result);  // Purchase should succeed
        Optional<Book> bookOptional = bookRepositoryInterface.getBookById(1L);
        assertEquals(9, bookOptional.get().getQuantity());  // Quantity should be reduced by 1
    }

    @Test
    void testPurchaseBook_OutOfStock() {
        bookRepositoryInterface.purchaseBook(1L);  // Purchase 10 times to exhaust the stock
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);
        bookRepositoryInterface.purchaseBook(1L);

        boolean result = bookRepositoryInterface.purchaseBook(1L);  // Try purchasing again when out of stock

        assertFalse(result);  // Purchase should fail
    }

    @Test
    void testPurchaseBook_BookNotFound() {
        boolean result = bookRepositoryInterface.purchaseBook(999L);  // Try to purchase a non-existing book

        assertFalse(result);  // Should return false since the book doesn't exist
    }
}
