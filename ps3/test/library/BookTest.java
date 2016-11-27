package library;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for Book ADT.
 */
public class BookTest {

    /*
     * Testing strategy
     * ==================
     * 
     * Ensure the rep invariant is maintained.
     * Test constructor only allows new Books to be created that follow preconditions.
     * 
     * Ensure that getTitle returns correct title.
     * Ensure that getYear returns the correct year.
     * Ensure that the list of Authors is not mutated when passed to the object. Ensure that order is maintained.
     * Ensure that mutating the list from getAuthors does not effect the rep. (no rep exposure)
     * 
    */
    
    private final String badTitle = " ";
    private final int badYear = -1;
    private final String badAuthor = " ";
    private final String goodTitle = "This is a title";
    private final int goodYear = 1992;
    private final String goodAuthor = "Zach Gale-Day";
    
    
    @Test(expected=AssertionError.class)
    public void testConstructorBadTitle() {
        Book book = new Book(badTitle, Arrays.asList(goodAuthor), goodYear);    //ensure assertion error on badTitle
    }
    
    @Test(expected=AssertionError.class)
    public void testConstructorNoAuthor() {
        Book book = new Book(badTitle, Arrays.asList(), goodYear);              //ensure assertion error on empty author List
    }
    
    @Test(expected=AssertionError.class)
    public void testConstructorBadAuthor() {
        Book book = new Book(goodTitle, Arrays.asList(badAuthor), goodYear);    //ensure assertion error on badAuthor
    }
    
    @Test(expected=AssertionError.class)
    public void testConstructorBadYear() {
        Book book = new Book(goodTitle, Arrays.asList(goodAuthor), badYear);    //ensure assertion error on badYear
    }
    
    @Test
    public void testGetTitle() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals("This Test Is Just An Example", book.getTitle());
    }
    
    @Test
    public void testGetYear() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals(1990, book.getYear());
    }
    
    @Test
    public void testGetAuthors() {
        List<String> authors = new ArrayList<String>(Arrays.asList("You Should", "Replace It", "With Your Own Tests"));
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals("Lists are the same size", authors.size(), book.getAuthors().size());
        for (int i = 0; i < authors.size(); i++) {
            assertEquals("Author list is changes in book", authors.get(i), book.getAuthors().get(i));
        }
    }  
    
    @Test
    public void testMutateConstructor() {
        List<String> authors = new ArrayList<String>(Arrays.asList(goodTitle));
        Book book = new Book("This Test Is Just An Example", authors, 1990);
        authors.set(0, "Ghost in the system");
        assertEquals("Authors is the correct size", 1, book.getAuthors().size());
        assertEquals("Mutating list changed value in book", goodTitle, book.getAuthors().get(0));
    }  
    
    @Test
    public void testGetAuthorsImmutable() {
        List<String> authors = new ArrayList<String>(Arrays.asList("You Should", "Replace It", "With Your Own Tests"));
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        List<String> alias = book.getAuthors();
        alias.remove(0);
        assertEquals("Lists are the same size", authors.size(), book.getAuthors().size());
        for (int i = 0; i < authors.size(); i++) {
            assertEquals("Author list is changes in book", authors.get(i), book.getAuthors().get(i));
        }
    }  
    
    @Test
    public void testHashCode() {
        List<String> authors = new ArrayList<String>(Arrays.asList(goodAuthor));
        Book book = new Book(goodTitle, authors, goodYear);
        assertFalse("Hashcode not overriden", book.hashCode() == System.identityHashCode(book));
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
