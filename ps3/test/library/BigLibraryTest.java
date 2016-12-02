package library;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test suite for BigLibrary's stronger specs.
 */
public class BigLibraryTest {
    
    /* 
     * NOTE: use this file only for tests of BigLibrary.find()'s stronger spec.
     * Tests of all other Library operations should be in LibraryTest.java 
     */

    /*
     * Testing strategy
     * ==================
     * 
     * Longer substring match goes first.
     * Same length substring match gives list in alpha order if same year.
     * Test that find is case insensitive.
     * 
     */
    
    private final Book normalBook = new Book("Normal Book", Arrays.asList("Normal Author"), 1992);
    private final Book aNormalBook = new Book("ANormal Book", Arrays.asList("Normal Author"), 1992);
    private final Book newerNormalBook = new Book("Normal Book", Arrays.asList("normal author"), 1995);
    private final Book shorterBook = new Book("Normal Boo", Arrays.asList("normal author"), 1992);
    
    @Test
    public void testExampleTest() {
        // this is just an example test, you should delete it
        Library library = new BigLibrary();
        assertEquals(Collections.emptyList(), library.find("This Test Is Just An Example"));
    }
    
    @Test
    public void testFindDifferentDatesCaseInsensitive() {
        Library library = new BigLibrary();
        library.buy(normalBook);
        library.buy(normalBook);
        library.buy(newerNormalBook);
        List<Book> books = library.find(normalBook.getTitle());
        Set<Book> bookSet = new HashSet<Book>(books);
        assertEquals("List from find violates set property", bookSet.size(), books.size());
        assertTrue("Find must return a list containing" + normalBook.toString(), books.contains(normalBook));
        assertTrue("Find must return a list containing" + newerNormalBook.toString(), books.contains(newerNormalBook));
        int indexNewer = books.size();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).equals(newerNormalBook)) { indexNewer = i; }
            else if (books.get(i).equals(normalBook)) { assertTrue("Newer books should come first", indexNewer < i); }
        }
    }
    
    @Test
    public void testFindSameSubstringAlpha() {
        Library library = new BigLibrary();
        library.buy(normalBook);
        library.buy(aNormalBook);
        List<Book> books = library.find(normalBook.getTitle());
        Set<Book> bookSet = new HashSet<Book>(books);
        assertEquals("List from find violates set property", bookSet.size(), books.size());
        assertTrue("Find must return a list containing" + books.toString(), books.contains(normalBook));
        assertTrue("Find must return a list containing" + shorterBook.toString(), books.contains(aNormalBook));
        int indexNewer = books.size();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).equals(aNormalBook)) { indexNewer = i; }
            else if (books.get(i).equals(normalBook)) { assertTrue("Newer books should come first" + books.toString(), indexNewer < i); }
        }
    }
    
    @Test
    public void testFindDifferentLongerSubstring() {
        Library library = new BigLibrary();
        library.buy(normalBook);
        library.buy(shorterBook);
        List<Book> books = library.find(normalBook.getTitle());
        Set<Book> bookSet = new HashSet<Book>(books);
        assertEquals("List from find violates set property", bookSet.size(), books.size());
        assertTrue("Find must return a list containing" + books.toString(), books.contains(normalBook));
        assertTrue("Find must return a list containing" + shorterBook.toString(), books.contains(shorterBook));
        int indexNewer = books.size();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).equals(normalBook)) { indexNewer = i; }
            else if (books.get(i).equals(shorterBook)) { assertTrue("Newer books should come first" + books.toString(), indexNewer < i); }
        }
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
