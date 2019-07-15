package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private static Map<ISBN, Book> availableBooks = new HashMap<>();
    private static Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public void addBooks(List<Book> books){
        books.forEach((b) -> {   
            this.availableBooks.put(b.getIsbn(), b);
        });
    }
    
    public void addBook(Book book){   
        this.availableBooks.put(book.getIsbn(), book);
    }
    
    public void subtractBook(Book book){   
        this.availableBooks.remove(book.getIsbn());
    }

    public Book findBook(long isbnCode) {
        Book b = null;
        for (Map.Entry<ISBN, Book> entry : availableBooks.entrySet()) {
            if(Long.compare(entry.getKey().getIsbnCode(), isbnCode) == 0)
                b = entry.getValue();               
	}
        return b;
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt){
        borrowedBooks.put(book, borrowedAt);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        LocalDate d = null;
        for (Map.Entry<Book, LocalDate> entry : borrowedBooks.entrySet()) {
            if(Long.compare(entry.getKey().getIsbn().getIsbnCode(), book.getIsbn().getIsbnCode()) == 0)
                d = entry.getValue();               
	}
        return d;
    }
}
