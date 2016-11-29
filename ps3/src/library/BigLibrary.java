package library;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.
 * 
 * In particular, every operation needs to run faster than linear time (as a function of the number of books
 * in the library).
 */
public class BigLibrary implements Library {

    // rep:
    private final Map<Book, List<BookCopy>> allBooks;
    private final Set<BookCopy> inLibrary;
    private final Set<BookCopy> checkedOut;
    
    // rep invariant:
    //    the intersection of inLibrary and checkedOut is the empty set.
    //    allBooks map each book in the library to each copy in the library
    // abstraction function:
    //      represents the collection of books is allAvailable, all available is 
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // TODO: safety from rep exposure argument
    
    public BigLibrary() {
        this.allBooks = new TreeMap<Book, List<BookCopy>>();
        this.inLibrary = new HashSet<BookCopy>();
        this.checkedOut = new HashSet<BookCopy>();
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        for (BookCopy copy: inLibrary) {
            assert !(checkedOut.contains(copy));
            assert (allBooks.containsKey(copy.getBook()));
        }
        for (BookCopy copy : checkedOut) { assert allBooks.containsKey(copy.getBook()); }
    }

    @Override
    public BookCopy buy(Book book) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public void checkout(BookCopy copy) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public void checkin(BookCopy copy) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public Set<BookCopy> availableCopies(Book book) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public List<Book> find(String query) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public void lose(BookCopy copy) {
        throw new RuntimeException("not implemented yet");
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    // @Override
    // public boolean equals(Object that) {
    //     throw new RuntimeException("not implemented yet");
    // }
    // 
    // @Override
    // public int hashCode() {
    //     throw new RuntimeException("not implemented yet");
    // }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
