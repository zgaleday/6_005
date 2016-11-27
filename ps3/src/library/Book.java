package library;

import java.util.List;
import java.util.ArrayList;

/**
 * Book is an immutable type representing an edition of a book -- not the physical object, 
 * but the combination of words and pictures that make up a book.  Each book is uniquely
 * identified by its title, author list, and publication year.  Alphabetic case and author 
 * order are significant, so a book written by "Fred" is different than a book written by "FRED".
 */
public class Book {

    private final List<String> authors;
    private final String title;
    private final int year;
    
    
    // Rep Invariant:
    //  authors is a list of authors. (must be non-empty and each author must have at least one non-space character.
    //  title is the case sensitive title of the book (must contain at least one non-space character)
    //  year is the publication date of the book in CE (must be positive)
    // Abstraction Function:
    //  Represents a book of a given title, authors as they appear and given publication year.  Authors is order sensitive
    //  and each author is case sensitive. The title is case sensitive and should appear as it does on the cover.
    // Safety from rep exposure:
    //  All fields are private;
    //  title and year are String and int types, so are guaranteed immutable;
    //  authors is a mutable list so the constructor and getAuthors method will make
    //  defensive copies to avoid sharing the rep's author object with clients. 
    
    /**
     * Make a Book.
     * @param title Title of the book. Must contain at least one non-space character.
     * @param authors Names of the authors of the book.  Must have at least one name, and each name must contain 
     * at least one non-space character.
     * @param year Year when this edition was published in the conventional (Common Era) calendar.  Must be nonnegative. 
     */
    public Book(String title, List<String> authors, int year) {
        this.authors = new ArrayList<String>(authors);
        this.title = title;
        this.year = year;
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        assert this.authors != null;
        assert this.title != null;
        assert this.year >= 0;
        assert this.title.trim().length() > 0;
        assert this.authors.size() > 0;
        for (String author : this.authors) {
            assert author.trim().length() > 0;
        }
        
    }
    
    /**
     * @return the title of this book
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * @return the authors of this book
     */
    public List<String> getAuthors() {
        throw new RuntimeException("not implemented yet");
    }

    /**
     * @return the year that this book was published
     */
    public int getYear() {
        throw new RuntimeException("not implemented yet");
    }

    /**
     * @return human-readable representation of this book that includes its title,
     *    authors, and publication year
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
