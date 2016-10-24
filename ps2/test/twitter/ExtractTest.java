package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * Tests for the getTimespan method of Extract.
     * 
     * Tests edge case behavior with 1, 2 tweets
     * Tests on max and min values for Instant
     * Tests on tweets with same Instant
     * Tests that the tweet list is not mutated
     * 
     * Tests for the getMentionedUsers method of Extract.
     * 
     * Tests with no tweets
     * Tests that tweet list no mutated
     * Tests mention as first char
     * Tests with multiple occurrences of same user
     * Tests with multiple users
     * tests with no users.
     * Tests with same user with different casing
     * Tests to make sure that no @ proceeded by valid char included.
     */
    
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.MIN;
    private static final Instant d4 = Instant.MAX;
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweetMin = new Tweet(3, "alyssa", "is it reasonable to talk about rivest so much?", d3);
    private static final Tweet tweetMax = new Tweet(4, "bbitdiddle", "rivest talk in 30 minutes #hype", d4);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweetIndex = new Tweet(6, "alyssa", "@abc", d1);
    private static final Tweet tweetUpperLower = new Tweet(7, "alyssa", "I like @abc and @AbC", d1);
    private static final Tweet tweetMultiple = new Tweet(8, "alyssa", "Man I feel @abc and @def", d1);
    private static final Tweet tweetParser = new Tweet(9, "alyssa", "cool@abc.com def @efc", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
       
    @Test
    public void testImmutableTweetsTime() {
        Tweet[] tweets = {tweet1, tweet2};
        Tweet[] tweets2 = {tweet1, tweet2};
        
        assertTrue("equals", Arrays.equals(tweets, tweets2));
        Extract.getTimespan(Arrays.asList(tweets2));
        
        assertTrue("immutable", Arrays.equals(tweets, tweets2)); //Ensure list immutable
    }
    
    @Test
    public void testMinInstant() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweetMin));
        
        assertEquals("expected start", d3, timespan.getStart());  //Test can parse min Instant
        assertEquals("expected end", d1, timespan.getEnd());
    }
    
    @Test
    public void testMaxInstant() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweetMax));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d4, timespan.getEnd());                //Test can parse max Instant
    }
    
    @Test
    public void testMaxTimespan() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweetMin, tweetMax));
        
        assertEquals("expected start", d3, timespan.getStart());
        assertEquals("expected end", d4, timespan.getEnd());                //Test can parse with min and max instants 
    }
    
    @Test
    public void testSameInstant() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet5));
        
        //Test that Timespan start and stops at same Instant
        assertTrue("start equals end", timespan.getStart().equals(timespan.getEnd()));
    }
    
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testImmutableTweetsMention() {
        Tweet[] tweets = {tweet1, tweet2};
        Tweet[] tweets2 = {tweet1, tweet2};
        
        assertTrue("equals", Arrays.equals(tweets, tweets2));
        Extract.getMentionedUsers(Arrays.asList(tweets2));
        
        assertTrue("immutable timespan", Arrays.equals(tweets, tweets2)); //Ensure list immutable
    }
    
    @Test
    public void testGetMentionedUsersIsSet() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetUpperLower));
        
        assertTrue("expected one user", mentionedUsers.size() == 1);        //Only one user
        for (String user : mentionedUsers) {
            assertEquals("expected user once", user.toLowerCase(), "abc");  //Set property
        }
    }
    
    @Test
    public void testGetMentionedUsersMultipleUsers() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetMultiple));
        
        assertTrue("expected one user", mentionedUsers.size() == 2);        //Only one user
        for (String user : mentionedUsers) {
            assertTrue("expected user once", user.toLowerCase().equals("abc") ||
                    user.toLowerCase().equals("def"));  //Set property
        }
    }
    
    @Test
    public void testGetMentionedUsersIgnoresBadAts() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetParser));
        
        assertTrue("expected one user", mentionedUsers.size() == 1);        //Only one user
        for (String user : mentionedUsers) {
            assertTrue("expected user once", user.toLowerCase().equals("def"));  //Set property
        }
    }
    
    

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
