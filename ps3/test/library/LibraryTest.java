package library;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test suite for Library ADT.
 */
@RunWith(Parameterized.class)
public class LibraryTest {

    /*
     * Note: all the tests you write here must be runnable against any
     * Library class that follows the spec.  JUnit will automatically
     * run these tests against both SmallLibrary and BigLibrary.
     */

    /**
     * Implementation classes for the Library ADT.
     * JUnit runs this test suite once for each class name in the returned array.
     * @return array of Java class names, including their full package prefix
     */
    @Parameters(name="{0}")
    public static Object[] allImplementationClassNames() {
        return new Object[] { 
            "library.SmallLibrary", 
            "library.BigLibrary"
        }; 
    }

    /**
     * Implementation class being tested on this run of the test suite.
     * JUnit sets this variable automatically as it iterates through the array returned
     * by allImplementationClassNames.
     */
    @Parameter
    public String implementationClassName;    

    /**
     * @return a fresh instance of a Library, constructed from the implementation class specified
     * by implementationClassName.
     */
    public Library makeLibrary() {
        try {
            Class<?> cls = Class.forName(implementationClassName);
            return (Library) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     * Testing strategy
     * ==================
     * 
     * buy():
     *  already a copy in library. no copy in library.
     * checkout():
     *  only one copy in library. multiple copies in library
     * checkin():
     *  only copy of book.  Multiple copies of book.
     * isAvailable():
     *  is available. is checkout out. not in library at all
     * allCopies():
     *  one copy. multiple copies. copy available. copy not available. If mutate return does not alter behavior.
     * availableCopies():
     *  one copy. multiple copies. copy available. copy not available. If mutate return does not alter behavior.
     * find():
     *  exact title match. exact author match. multiple matches. same title and authors, but diff date, newest first.
     *  multiple copies of same book (only once in list).  If mutate return does not alter behavior. 
     * lose():
     *  multiple copies. one copy. copy started as available. copy started as not available.
     */
    
    private final Book normalBook = new Book("Normal Book", Arrays.asList("Normal Author"), 1992);
    private final Book newerNormalBook = new Book("Normal Book", Arrays.asList("Normal Author"), 1995);
    
    @Test
    public void testExampleTest() {
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals(Collections.emptySet(), library.availableCopies(book));
    }
    
    public void testLoseTwo() {
        Library library = makeLibrary();
        library.buy(normalBook);
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 2, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 2, library.allCopies(normalBook).size());
        BookCopy cache = new BookCopy(newerNormalBook);
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            cache = copy;
        }
        library.lose(cache);
        assertFalse("Copy should be gone after lose.", library.isAvailable(cache));
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testLoseOneOut() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy);
            library.lose(copy);
            assertFalse("Copy shouldn't be available.", library.isAvailable(copy));
        }
        assertEquals("Number of avail copies not one.", 0, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 0, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testLoseOneIn() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.lose(copy);
            assertFalse("Copy shouldn't be available.", library.isAvailable(copy));
        }
        assertEquals("Number of avail copies not one.", 0, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 0, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testFindExactAuthorMatch() {
        Library library = makeLibrary();
        library.buy(normalBook);
        List<Book> books = library.find(normalBook.getAuthors().get(0));
        Set<Book> bookSet = new HashSet<Book>(books);
        assertEquals("List from find violates set property", bookSet.size(), books.size());
        assertTrue("Find must return a list containing" + normalBook.toString(), books.contains(normalBook));
    }
    
    @Test
    public void testFindMutateReturn() {
        Library library = makeLibrary();
        library.buy(normalBook);
        List<Book> books1 = library.find(normalBook.getAuthors().get(0));
        List<Book> books2 = library.find(normalBook.getAuthors().get(0));
        books2.add(newerNormalBook);
        assertEquals("List from Find should not be mutated", books1, library.find(normalBook.getAuthors().get(0)));
    }
    
    @Test
    public void testFindDifferentDates() {
        Library library = makeLibrary();
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
    public void testAllCopiesMutate() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        library.allCopies(normalBook).add(new BookCopy(normalBook));
        library.availableCopies(normalBook).add(new BookCopy(normalBook));
        assertEquals("Number of avail copies not 1.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not 1.", 1, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testIsAvailableNotInLibrary() {
        Library library = makeLibrary();
        BookCopy book = new BookCopy(normalBook);
        assertFalse("Book should not be available if not in library", library.isAvailable(book));
    }
    
    @Test
    public void testCheckoutOne() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy);
            assertFalse("Copy shouldn't be available.", library.isAvailable(copy));
        }
        assertEquals("Number of avail copies not one.", 0, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 1, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testCheckinOne() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        BookCopy cache = new BookCopy(normalBook);
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy);
            cache = copy;
            assertFalse("Copy shouldn't be available.", library.isAvailable(copy));
        }
        library.checkin(cache);
        assertTrue("Copy should be available.", library.isAvailable(cache));
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 1, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testCheckinMulti() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        library.buy(normalBook);
        BookCopy cache = new BookCopy(normalBook);
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy);
            cache = copy;
            assertFalse("Copy shouldn't be available.", library.isAvailable(copy));
            
        }
        library.checkin(cache);
        assertTrue("Copy should be available.", library.isAvailable(cache));
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 2, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testCheckoutMultiple() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy); 
            assertFalse("Copy shouldn't be available.", library.isAvailable(copy));
        }
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 2, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testCheckoutAll() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        library.buy(normalBook);
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy); 
            assertFalse("Copy shouldn't be available.", library.isAvailable(copy));
        }
        assertEquals("Number of avail copies not one.", 0, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 2, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testBuy() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        library.buy(normalBook);
        assertEquals("Number of avail copies not two.", 2, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 2, library.allCopies(normalBook).size());
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
