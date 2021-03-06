package library;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.
 * 
 * In particular, every operation needs to run faster than linear time (as a function of the number of books
 * in the library).
 */
public class BigLibrary implements Library {

    // rep:
    private final Map<Book, Set<BookCopy>> allBooks;
    private final Set<BookCopy> inLibrary;
    private final Set<BookCopy> checkedOut;
    private Map<Book, Integer> lenMatch;
    private String lastkeyword;
    
    // rep invariant:
    //    the intersection of inLibrary and checkedOut is the empty set.
    //    allBooks map each book in the library to each copy in the library.
    //    len match is the substring match of each book stores to the last keyword.
    // abstraction function:
    //      represents the collection of books is allAvailable, all available is 
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // safety from rep exposure:
    //  all mutable objects returned to user are defensively copies. All fields are private and
    //  final where possible.  lenMatch and lastKeyword are new exposed to the user.
    // 
    
    public BigLibrary() {
        this.allBooks = new HashMap<Book, Set<BookCopy>>();
        this.inLibrary = new HashSet<BookCopy>();
        this.checkedOut = new HashSet<BookCopy>();
        this.lenMatch = new HashMap<Book, Integer>();
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
        BookCopy newCopy = new BookCopy(book);
        if (allBooks.containsKey(book)) { allBooks.get(book).add(newCopy); }
        else { 
            Set<BookCopy> tempSet = new HashSet<BookCopy>(Arrays.asList(newCopy));
            allBooks.put(book, tempSet);
        }
        inLibrary.add(newCopy);
        checkRep();
        return newCopy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        inLibrary.remove(copy);
        checkedOut.add(copy);
        checkRep();
    }
    
    @Override
    public void checkin(BookCopy copy) {
        checkedOut.remove(copy);
        inLibrary.add(copy);
        checkRep();
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        if (allBooks.containsKey(book))
            return new HashSet<BookCopy>(allBooks.get(book));
        else { return Collections.emptySet(); }
    }

    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> copies = new HashSet<BookCopy>();
        if (!allBooks.containsKey(book)) { return Collections.emptySet(); }
        for (BookCopy copy : allBooks.get(book)) 
            if (inLibrary.contains(copy)) { copies.add(copy); }
        return copies;
        
    }
   
    @Override
    public boolean isAvailable(BookCopy copy) {
        return inLibrary.contains(copy);
    }
    
    /**
     * Stronger spec than in Library find().  It returns the list of books that have a substring 
     * match to the query in the book.getAuthors and book.getTitle.  The minimum length of substring match is
     * query.length - 3.  If the substring matches are the same length then it returns the newest books first.
     * All else being equal the books will be sorted in alpha order.
     */   
    @Override
    public List<Book> find(String query) {
        query = query.toLowerCase();
        if (!query.equals(this.lastkeyword)) { this.lenMatch = new HashMap<Book, Integer>(); }
        Set<Book> findResult = new TreeSet<Book>(new BookComparator(query));
        for (Book book : this.allBooks.keySet()) {
            String aMatcher = book.getTitle() + " ";
            for (String author : book.getAuthors()) { aMatcher += author; }
            int bookMatch = longestSubstr(query, aMatcher.toLowerCase());
            lenMatch.put(book, bookMatch);
            if (bookMatch > (query.length() - 3)) { findResult.add(book); }
        }
        this.lastkeyword = query;
        return new ArrayList<Book>(findResult);
    }
    
    @Override
    public void lose(BookCopy copy) {
        Book book = copy.getBook();
        allBooks.get(book).remove(copy);
        if (allBooks.get(book).size() == 0) { allBooks.remove(book); }
        inLibrary.remove(copy);
        checkedOut.remove(copy);
        checkRep();
    }
    
    //Code used from http://stackoverflow.com/questions/17150311/
    //java-implementation-for-longest-common-substring-of-n-strings this is a lazy implementation w/o Tries
    //but assuming short strings should have less overhead.
    private static int longestSubstr(String first, String second) {
        if (first == null || second == null || first.length() == 0 || second.length() == 0) {
            return 0;
        }

        int maxLen = 0;
        int fl = first.length();
        int sl = second.length();
        int[][] table = new int[fl][sl];

        for (int i = 0; i < fl; i++) {
            for (int j = 0; j < sl; j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0) {
                        table[i][j] = 1;
                    }
                    else {
                        table[i][j] = table[i - 1][j - 1] + 1;
                    }
                    if (table[i][j] > maxLen) {
                        maxLen = table[i][j];
                    }
                }
            }
        }
        return maxLen;
    }
    
    /*
     * Comparator class that uses the longestSubstr Map to sort books.  The longest substring
     * match get the highest priority.  Meant to be used in a case insensitive manner with matches 
     * of same length giving precedence to newer books. All else being equal the list will be sorted
     * in alphabetical order. 
     */
    private class BookComparator implements Comparator<Book> {
        private String keyword;
        public BookComparator(String keyword) { this.keyword = keyword; }
        
        @Override
        public int compare(Book a, Book b) {
            if (!lenMatch.containsKey(a)) { 
                String aMatcher = a.getTitle() + " ";
                for (String author : a.getAuthors()) { aMatcher += author; }
                lenMatch.put(a, longestSubstr(this.keyword, aMatcher));
            }
            if (!lenMatch.containsKey(b)) {
                String bMatcher = b.getTitle() + " ";
                for (String author : b.getAuthors()) { bMatcher += author; }
                lenMatch.put(b, longestSubstr(this.keyword, bMatcher));
            }
            int alen = lenMatch.get(a);
            int blen = lenMatch.get(b);            
            if (alen < blen) { return 1; }
            else if (alen > blen) { return -1; }
            else { 
                if (a.getYear() < b.getYear()) { return 1; }
                else if (a.getYear() > b.getYear()) { return -1; }
                else { return a.getTitle().compareTo(b.getTitle()); }
            }
        }
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
