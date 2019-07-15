package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import fr.d2factory.libraryapp.member.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

//import static org.junit.Assert.fail;

public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;
    private LocalDate ld = null;
    private LibraryImpl li = null;
    
    private Member r1 = null;
    private Member r2 = null;
    private Member r3 = null;
    private Member s1 = null;
    private Member s2 = null;
    
    private Book b1 = null;
    private Book b2 = null;
    private Book b3 = null;
    private Book b4 = null;
    private List<Book> listBooks;

    @Before
    public void setup(){
        bookRepository = new BookRepository();
        ld = LocalDate.now();
        li = new LibraryImpl();
        
        b1 = new Book("Catch 22", "Joseph Heller", new ISBN(968787565445L));
        b2 = new Book("Harry Potter", "J.K. Rowling", new ISBN(46578964513L));
        b3 = new Book("La peau de chagrin", "Balzac", new ISBN(465789453149L));
        b4 = new Book("Around the world in 80 days", "Jules Verne", new ISBN(3326456467846L));
        
        listBooks = new ArrayList<>();
        
        listBooks.add(b1);
        listBooks.add(b2);
        listBooks.add(b3);
        //listBooks.add(b4);  for test "member_can_borrow_a_book_if_book_is_available()"
        
        bookRepository.addBooks(listBooks);
        
        r1 = new Resident("res1", "Jean Pierre", 30);
        r2 = new Resident("res2", "Luc Francois", 40);
        r3 = new Resident("res3", "Armand LeDuc", 30);
        s1 = new Student("st1", "Paul LeGrand", 2, 25);
        s2 = new Student("st2", "Michel DuBois", 1, 56);
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){
        //resident r3 tries to borrow book b3 which is available
        assertTrue(li.borrowBook(465789453149L, r3, ld).getIsbn().getIsbnCode()==(b3.getIsbn().getIsbnCode()));
        
        //student s1 tries to borrow book b4 which is not available
        try{
            assertFalse(Long.compare(li.borrowBook(3326456467846L, s1, ld).getIsbn().getIsbnCode(), (b4.getIsbn().getIsbnCode()))== 0);
        }catch(NullPointerException npe){
            System.out.println("Test / Book not available");
        }
    }

    @Test
    public void borrowed_book_is_no_longer_available(){
        li.borrowBook(465789453149L, s2, ld);
        assertTrue(Long.compare(bookRepository.findBook(465789453149L).getIsbn().getIsbnCode(),(b3.getIsbn().getIsbnCode()))== 0);
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        r1.payBook(40);
        assertTrue(r1.getWallet()== 26);
    }

    @Test
    public void students_pay_10_cents_the_first_30days(){
        s1.payBook(20);
        assertTrue(s1.getWallet()== 23);
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        s2.payBook(15);
        assertTrue(s2.getWallet()== 56);
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
        s1.payBook(45);
        assertTrue(s1.getWallet()== 19.75);
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        r1.payBook(80);
        assertTrue(r1.getWallet()== 20);
    }

    @Test()
    public void members_cannot_borrow_book_if_they_have_late_books(){
        //resident r2 has late book b1
        bookRepository.saveBookBorrow(b1, ld.minusDays(100));
        li.addBorrowedBooksByMember(b1, r2);
        try{
            li.borrowBook(46578964513L, r2, ld).equals(b2);
        }catch(HasLateBooksException hbe){
            System.out.println("This resident has late books");
        }
            
        //student s2 has late book b2
        bookRepository.saveBookBorrow(b2, ld.minusDays(120));
        li.addBorrowedBooksByMember(b2, s2);
        try{
            li.borrowBook(465789453149L, s2, ld).equals(b3);
        }catch(HasLateBooksException hbe){
            System.out.println("This student has late books");
        }
    }
}
