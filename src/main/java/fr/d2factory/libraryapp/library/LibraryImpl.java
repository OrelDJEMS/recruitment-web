/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DJEMS
 */
public class LibraryImpl implements Library{
    private BookRepository br = new BookRepository();    
    private Map<Book, Member> borrowedBooksByMember = new HashMap<>();
    
    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
        Book b = null;
        if("Student".equals(member.getClass().getSimpleName()) || "Resident".equals(member.getClass().getSimpleName())){
            if(HasLateBooks(member)){ 
                throw new HasLateBooksException("This member owns late books");                
            }else{
                try{
                    b = this.br.findBook(isbnCode);
                    if(!"".equals(b.getIsbn().getIsbnCode())){
                        this.br.saveBookBorrow(b, borrowedAt);
                        this.br.subtractBook(b);
                        addBorrowedBooksByMember(b, member);    
                    }
                }catch(NullPointerException npe){
                   System.out.println("Book not available"); 
                }                              
            } 
        } else {
            System.out.println("Only Members can borrow books at the library");
        }        
        return b;
    }

    @Override
    public void returnBook(Book book, Member member) {
        br.addBook(book);
        LocalDate ld = br.findBorrowedBookDate(book);
        int numberOfDays = Period.between(LocalDate.now(), ld).getDays();
        member.payBook(numberOfDays);
        substractBorrowedBooksByMember(book);
    }

    @Override
    public void addBorrowedBooksByMember(Book b, Member m) {
        borrowedBooksByMember.put(b, m);
    }
    
    @Override
    public void substractBorrowedBooksByMember(Book b) {
        borrowedBooksByMember.remove(b);
    }
    
    @Override
    public boolean HasLateBooks(Member m) {
        boolean b = false;
        for (Map.Entry<Book, Member> entry : borrowedBooksByMember.entrySet()) {
            if(entry.getValue().equals(m)){
                LocalDate ld = br.findBorrowedBookDate(entry.getKey());
                long numberOfDays = ChronoUnit.DAYS.between(ld,LocalDate.now());
                if("Student".equals(m.getClass().getSimpleName())){
                    if(numberOfDays > 30){
                        b = true;                   
                    }
                }else if("Resident".equals(m.getClass().getSimpleName())){
                    if(numberOfDays > 60){
                        b = true;                   
                    }
                }                    
            }
	}
        return b;
    }
}
