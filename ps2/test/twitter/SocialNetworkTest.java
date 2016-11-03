package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
     * 
     * Partitions for influencers:
     * Map: empty, one, three (ensure unique and in correct order)
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
    public void testGuessFollowsGraphFollowCase() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5, tweet6));
        
        assertTrue("expect non-empty Map", followsGraph.size() >= 1);
        boolean check = false;
        for (String user : followsGraph.keySet()) {
            if (user.toLowerCase().equals("alyssa") && check == true)
                assertFalse("case sensitivity: ", check);
            else if (user.toLowerCase().equals("alyssa") && check == false)
                check = true;
        }
        assertTrue("User in once: ", check);
    }
    
    @Test
    public void testGuessFollowsGraphMultipleAt() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet8, tweet9));
        
        assertTrue("expect non-empty Map", followsGraph.size() >= 1);
        boolean check = false;
        for (String user : followsGraph.keySet()) {
            if (user.toLowerCase().equals("alyssa") && check == true)
                assertFalse("case sensitivity: ", check);
            else if (user.toLowerCase().equals("alyssa") && check == false)
                check = true;
        }
        assertTrue("User in once: ", check);
    }
    
    @Test
    public void testGuessFollowsGraphAuthorCase() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5, tweet7));
        
        assertEquals("expected one follow", 1, followsGraph.size());
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
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersOne() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> mySet = new HashSet<String>();
        mySet.add("paul");
        followsGraph.put("alyssa", mySet);
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected 1 person in list", 1, influencers.size());
        assertTrue("Correct person in list", influencers.get(0).equals("alyssa"));
    }
    
    @Test
    public void testInfluencersTwoUsersOneFollowing() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> mySet = new HashSet<String>();
        mySet.add("paul");
        followsGraph.put("alyssa", mySet);
        followsGraph.put("paul", new HashSet<String>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected 1 person in list", 2, influencers.size());
        assertTrue("Correct person in list", influencers.get(0).equals("alyssa"));
    }
    
    @Test
    public void testInfluencersThree() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        String[] authors = {"alyssa", "max", "thomas"};
        ArrayList<HashSet<String>> follows = new ArrayList<HashSet<String>>();
        for (int i = 0; i < 3; i++) { follows.add(new HashSet<String>()); }
        follows.get(0).add("paul");
        follows.get(1).add("mark");
        follows.get(1).add("paul");
        follows.get(2).add("paul");
        follows.get(2).add("mark");
        follows.get(2).add("luke");
        for (int i = 0; i < 3; i++) { followsGraph.put(authors[i], follows.get(i)); }
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected 1 person in list", 3, influencers.size());
        assertTrue("Expect alyssa got: " + influencers.get(2) , influencers.get(2).equals("alyssa"));
        assertTrue("Correct person in list index 1", influencers.get(1).equals("max"));
        assertTrue("Correct person in list index 0", influencers.get(0).equals("thomas"));
    }

    public void testInfluencersCase() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        String[] authors = {"alyssa", "Alyssa"};
        ArrayList<HashSet<String>> follows = new ArrayList<HashSet<String>>();
        for (int i = 0; i < 3; i++) { follows.add(new HashSet<String>()); }
        follows.get(0).add("paul");
        follows.get(1).add("mark");
        follows.get(1).add("paul");
        for (int i = 0; i < 2; i++) { followsGraph.put(authors[i], follows.get(i)); }
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected 1 person in list", 1, influencers.size());
        assertTrue("Expect alyssa got: " + influencers.get(0) , influencers.get(0).toLowerCase().equals("alyssa"));
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
