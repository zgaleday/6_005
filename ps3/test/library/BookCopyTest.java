package library;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test suite for BookCopy ADT.
 */
public class BookCopyTest {

    /*
     * Testing strategy
     * ==================
     * 
     * Constructor:  Ensure the initial condition is good.
     * getBook:  Ensure that a Book object is returned. Ensure correct book is returned. 
     * getCondition and setCondtion: ensure one reflects the other. 
     */
    
    private final Book goodBook = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
    
    @Test
    public void testExampleTest() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        BookCopy copy = new BookCopy(book);
        assertEquals("Incorrect book stored in bookCopy",book, copy.getBook());
    }
    
    @Test(expected=AssertionError.class)
    public void testBookNotNull() {
        Book book = null;
        BookCopy copy = new BookCopy(book);
    }
    
    @Test
    public void testInitialCondition() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        BookCopy copy = new BookCopy(book);
        assertEquals("Initial condition not GOOD.",BookCopy.Condition.GOOD, copy.getCondition());
    }
    
    @Test
    public void testSetCondition() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        BookCopy copy = new BookCopy(book);
        assertEquals("Initial condition not GOOD",BookCopy.Condition.GOOD, copy.getCondition());
        copy.setCondition(BookCopy.Condition.DAMAGED);
        assertEquals("Condition doesn't match after setCondition",BookCopy.Condition.DAMAGED, copy.getCondition());
    }
    
    @Test
    public void testGetCondition() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        BookCopy copy = new BookCopy(book);
        assertEquals("Initial condition not GOOD",BookCopy.Condition.GOOD, copy.getCondition());
        copy.setCondition(BookCopy.Condition.DAMAGED);
        BookCopy.Condition condition = copy.getCondition();
        condition = BookCopy.Condition.GOOD;
        assertEquals("Condition doesn't match after setCondition",BookCopy.Condition.DAMAGED, copy.getCondition());
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
