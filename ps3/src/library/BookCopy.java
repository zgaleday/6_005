package library;

/**
 * BookCopy is a mutable type representing a particular copy of a book that is held in a library's
 * collection.
 */
public class BookCopy {

    private final Book book;
    private final BookCopy.Condition condition = BookCopy.Condition.GOOD;
    
    // Rep Invariant:
    //  The initial condition must be GOOD. book is a good instance of Book
    // Abstraction Function:
    //  Book is the type of book of this copy. condition maps to the condition of this copy of the book
    // Safety from Rep exposure. 
    //  book is immutable and it's reference is final.  condition is private and therefore can only be accessed using getters and setters. 
    //  get returns a copy of the current condition.
    
    public static enum Condition {
        GOOD, DAMAGED
    };
    
    /**
     * Make a new BookCopy, initially in good condition.
     * @param book the Book of which this is a copy
     */
    public BookCopy(Book book) {
        this.book = book;
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        assert this.book != null;
        assert this.condition == BookCopy.Condition.GOOD;
    }
    
    /**
     * @return the Book of which this is a copy
     */
    public Book getBook() {
        throw new RuntimeException("not implemented yet");
    }
    
    /**
     * @return the condition of this book copy
     */
    public Condition getCondition() {
        throw new RuntimeException("not implemented yet");
    }

    /**
     * Set the condition of a book copy.  This typically happens when a book copy is returned and a librarian inspects it.
     * @param condition the latest condition of the book copy
     */
    public void setCondition(Condition condition) {
        throw new RuntimeException("not implemented yet");
    }
    
    /**
     * @return human-readable representation of this book that includes book.toString()
     *    and the words "good" or "damaged" depending on its condition
     */
    public String toString() {
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
