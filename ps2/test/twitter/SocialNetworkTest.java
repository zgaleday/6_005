package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * Partions for guessFollowsGraph:
     * tweets: not mutated by method, empty
     * Author: no follows either empty set or not in map, different casing in Author doesn't give two entries
     * Follows:can't follow self, not case sensitive
     * @mentions: if user @ mentions follow in map. Test across multiple tweets.
     * same user different cases. Multiple @ one tweet.(don't stop parse after self mention)
     */
    
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "", d1);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "@alyssa", d1);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "@bbitdiddle", d1);
    private static final Tweet tweet6 = new Tweet(6, "alyssa", "@Bbitdiddle", d1);
    private static final Tweet tweet7 = new Tweet(7, "Alyssa", "@bbitdiddle", d1);
    private static final Tweet tweet8 = new Tweet(8, "alyssa", "@alyssa @bbitdiddle @coolcat", d1);
    private static final Tweet tweet9 = new Tweet(9, "alyssa", "@popping", d1);
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphNoInfo() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet3));
        
        if (followsGraph.containsKey("alyssa"))
            assertEquals("author supposed to have no follows", 0, followsGraph.get("alyssa").size());
        else
            assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphFollowCase() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5, tweet6));
        
        assertTrue("expect non-empty Map", followsGraph.size() >= 1);
        for (String user : followsGraph.keySet())
            if (user.toLowerCase().equals("alyssa"))
                assertEquals("expected one follow", 1, followsGraph.get(user).size());
        assertEquals("user not in map", 0, 1);
    }
    
    @Test
    public void testGuessFollowsGraphMultipleAt() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet8, tweet9));
        
        for (String user : followsGraph.keySet())
            if (user.toLowerCase().equals("alyssa"))
                assertEquals("expected one follow", 3, followsGraph.get(user).size());
        assertEquals("user not in map", 0, 1);
    }
    
    @Test
    public void testGuessFollowsGraphAuthorCase() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5, tweet7));
        
        assertEquals("expected one follow", 1, followsGraph.size());
    }
    
    @Test
    public void testGuessFollowsSelfAt() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet4));
        
        if (followsGraph.containsKey("alyssa"))
            assertEquals("author supposed to have no follows", 0, followsGraph.get("alyssa").size());
        else
            assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsNotMutateList() {
        Tweet[] tweets = {tweet1, tweet2};
        Tweet[] tweets2 = {tweet1, tweet2};
        
        assertTrue("equals", Arrays.equals(tweets, tweets2));
        SocialNetwork.guessFollowsGraph(Arrays.asList(tweets2));
        
        assertTrue("immutable", Arrays.equals(tweets, tweets2)); //Ensure list immutable
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
