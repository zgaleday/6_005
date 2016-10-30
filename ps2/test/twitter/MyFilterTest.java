package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import org.junit.Test;

public class MyFilterTest {

    /*
     * Paritions for usedHashTag tests:
     * List: not mutated
     * Tweets: no Hashtags, 2 Hashtags
     * Hashtags: multiple cases of same hashtag give on entry in map, hashtag at start of tweet, hashtag at end of tweet
     *          multiple users same hashtag
     * 
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweetRepeat = new Tweet(3, "alyssa", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweetUpper = new Tweet(4, " bbitdiddle", "cool rivest talk in 30 minutes #HYPE", d1);
    private static final Tweet tweetBegin = new Tweet(5, "alyssa", "#test is cool", d1);
    private static final Tweet tweetEnd = new Tweet(6, "alyssa", "is cool #test", d1);
    private static final Tweet tweet3 = new Tweet(7, "alyssa", "is cool #test baby", d1);
    

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testUsedHashtagNoHashtags() {
        Tweet[] tweets = {tweet1};
        
        Map<String, HashSet<String>> hashtagAssociations = Filter.usedHashtag(Arrays.asList(tweets));
       
        assertEquals("empty map", 0, hashtagAssociations.size()); //Ensure list immutable
    }
    
    @Test
    public void testUsedHashtagBeginning() {
        Tweet[] tweets = {tweetBegin};
        
        Map<String, HashSet<String>> hashtagAssociations = Filter.usedHashtag(Arrays.asList(tweets));
       
        assertEquals("hashtag in map", 1, hashtagAssociations.size()); //Ensure list immutable
        for (String hashtag : hashtagAssociations.keySet()) {
            assertEquals("hashtag in set", "test", hashtag.toLowerCase());
        }
    }
    
    @Test
    public void testUsedHashtagMultipleHashtags() {
        Tweet[] tweets = {tweet2, tweet3};
        
        Map<String, HashSet<String>> hashtagAssociations = Filter.usedHashtag(Arrays.asList(tweets));
       
        assertEquals("hashtags in map", 2, hashtagAssociations.size()); //Ensure list immutable
        for (String hashtag : hashtagAssociations.keySet()) {
            assertTrue("hashtag not in in set", hashtag.toLowerCase().equals("test") || 
                    hashtag.toLowerCase().equals("hype"));
        }
    }
    
    @Test
    public void testUsedHashtagEnd() {
        Tweet[] tweets = {tweetEnd};
        
        Map<String, HashSet<String>> hashtagAssociations = Filter.usedHashtag(Arrays.asList(tweets));
       
        assertEquals("hashtag in map", 1, hashtagAssociations.size()); //Ensure list immutable
        for (String hashtag : hashtagAssociations.keySet()) {
            assertEquals("hashtag in set", "test", hashtag.toLowerCase());
        }
    }
    
    @Test
    public void testUsedHashtagMultipleAuthorsSameHashtag() {
        Tweet[] tweets = {tweet2, tweetRepeat};
        
        Map<String, HashSet<String>> hashtagAssociations = Filter.usedHashtag(Arrays.asList(tweets));
       
        assertEquals("1 hashtag in map", 1, hashtagAssociations.size()); //Ensure list immutable
        for (String hashtag : hashtagAssociations.keySet())
            for (String author : hashtagAssociations.get(hashtag))
                assertTrue("user in map", author.equals("alyssa") || author.equals("bbitdiddle"));
    }
    
    @Test
    public void testUsedHashtagCase() {
        Tweet[] tweets = {tweet2, tweetUpper};
        
        Map<String, HashSet<String>> hashtagAssociations = Filter.usedHashtag(Arrays.asList(tweets));
       
        assertEquals("1 hashtag in map", 1, hashtagAssociations.size()); //Ensure list immutable
        for (String hashtag : hashtagAssociations.keySet())
            assertEquals("user in map", "bbitdiddle", hashtagAssociations.get(hashtag).toArray()[0]);
    }
    
    @Test
    public void testUsedHashtagNotMutabateList() {
        Tweet[] tweets = {tweet1, tweet2};
        Tweet[] tweets2 = {tweet1, tweet2};
        assertTrue("equals", Arrays.equals(tweets, tweets2));
        
        Filter.usedHashtag(Arrays.asList(tweets2));
       
        assertTrue("mutated list", Arrays.equals(tweets, tweets2)); //Ensure list immutable
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
