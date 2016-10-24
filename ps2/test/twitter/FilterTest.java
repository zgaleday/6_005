package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * Paritions for writtenBy tests:
     * List: not mutated
     * Tweets: 0 by author, multiple by author appear in same order
     * Formatting name: ensure that list is same regardless of author capitalization in call
     * 
     * Partitions for inTimespan tests:
     * List: Not mutated, empty
     * TweetsL 0 in timespan,  multiple in timespan
     * Timespan: start and stop at same point as tweet
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant dAfter = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant dBefore = Instant.parse("2016-02-17T09:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "this is my second tweet", d2);
    private static final Tweet tweetBefore = new Tweet(4, " bbitdiddle", " cool rivest talk in 30 minutes #hype", dBefore);
    private static final Tweet tweetAfter = new Tweet(5, "alyssa", "boot this is my second tweet", dAfter);

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testWrittenByAuthorFormat() {
        List<Tweet> writtenByLower = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        List<Tweet> writtenByUpper = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");
        
        assertEquals("expected singleton list", 1, writtenByLower.size());
        assertTrue("expected list to contain tweet", writtenByLower.contains(tweet1));
        assertTrue("two results should be equal", writtenByLower.equals(writtenByUpper));
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleAuthor() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.get(0).equals(tweet1));
        assertTrue("expected list to contain tweet", writtenBy.get(1).equals(tweet3));
    }
    
    @Test
    public void testWrittenNoMatches() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2), "alyssa");
        
        assertEquals("expected singleton list", 0, writtenBy.size());
    }
    
    @Test
    public void testWrittenByNotMutabateList() {
        Tweet[] tweets = {tweet1, tweet2};
        Tweet[] tweets2 = {tweet1, tweet2};
        
        assertTrue("equals", Arrays.equals(tweets, tweets2));
        Filter.writtenBy(Arrays.asList(tweets2), "alyssa");
        
        assertTrue("immutable", Arrays.equals(tweets, tweets2)); //Ensure list immutable
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testInTimespanMultipleTweetsBracketed() {

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweetBefore, tweetAfter), new Timespan(d1, d2));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    @Test
    public void testInTimespanEmptyList() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(testStart, testEnd));
        
        assertTrue("expected non-empty list", inTimespan.isEmpty());
    }
    
    @Test
    public void testInTimespanSinglePoint() {
    
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(d1, d2));
        
        assertTrue("expected contains tweet at same point", inTimespan.contains(tweet1));
    }
    
    @Test
    public void testInTimespanNotMutabateList() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        Tweet[] tweets = {tweet1, tweet2};
        Tweet[] tweets2 = {tweet1, tweet2};
        
        assertTrue("equals", Arrays.equals(tweets, tweets2));
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet2), new Timespan(testStart, testEnd));

        assertTrue("immutable", Arrays.equals(tweets, tweets2)); //Ensure list immutable
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
