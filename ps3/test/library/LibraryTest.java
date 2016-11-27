package library;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

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
    
    @Test
    public void testCheckoutOne() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy);
            assertTrue("Copy shouldn't be available.", library.isAvailable(copy));
        }
        assertEquals("Number of avail copies not one.", 0, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not two.", 1, library.allCopies(normalBook).size());
    }
    
    @Test
    public void testCheckoutMultiple() {
        Library library = makeLibrary();
        library.buy(normalBook);
        assertEquals("Number of avail copies not one.", 1, library.availableCopies(normalBook).size());
        assertEquals("Number of total copies not one.", 1, library.allCopies(normalBook).size());
        for (BookCopy copy : library.availableCopies(normalBook)) { 
            library.checkout(copy); 
            assertTrue("Copy shouldn't be available.", library.isAvailable(copy));
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
            assertTrue("Copy shouldn't be available.", library.isAvailable(copy));
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
