package twitter;

import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {
    
    public static void main(String[] args) {
        Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
        Tweet tweet5 = new Tweet(5, "alyssa", "@bbitdiddle", d1);
        Tweet tweet6 = new Tweet(6, "alyssa", "@Bbitdiddle", d1);
        Map<String, Set<String>> followsGraph = guessFollowsGraph(Arrays.asList(tweet5, tweet6));
        for (String follow : followsGraph.get("alyssa"))
            System.out.println(follow);
    }
    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    
    
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<String, Set<String>>();
            for (Tweet tweet : tweets) {
                String author = tweet.getAuthor().toLowerCase();
                if (!followsGraph.containsKey(author)) {
                    Set<String> mySet = new HashSet<String>();
                    followsGraph.put(author, mySet);
                }
                for (String mention : Extract.getMentionedUsers(Arrays.asList(tweet))) {
                    mention = mention.toLowerCase();
                    if (!mention.equals(author)) {
                        followsGraph.get(author).add(mention);
                    }     
                }
            }
        return followsGraph;
    }
    

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Author> influencers = new HashMap<String, Author>();
        for (String follower : followsGraph.keySet()) {
            if (!influencers.containsKey(follower.toLowerCase()))
                influencers.put(follower.toLowerCase(), new Author(follower, 0));
            Set<String> follows = followsGraph.get(follower);
            for (String followed : follows) {
                if (influencers.containsKey(followed.toLowerCase())) {
                    influencers.get(followed.toLowerCase()).addFollower(follower.toLowerCase());
                    }
            
                else {
                    influencers.put(followed.toLowerCase(), new Author(followed.toLowerCase(), 0));
                    influencers.get(followed.toLowerCase()).addFollower(follower.toLowerCase());
                    }
                }
            }
        
        ArrayList<Author> authorsArray = new ArrayList<Author>();
        for (Author a : influencers.values()) { authorsArray.add(a); }
        Collections.sort(authorsArray, Collections.reverseOrder());
        List<String> authorStrings = new ArrayList<String>();
        for (Author tempAuthor : authorsArray) {
            authorStrings.add(tempAuthor.name); 
        }
        return authorStrings;        
    }
    
    
    
    private static class Author implements Comparable<Author>{
        
        private int followers;
        private String name;
        private Set<String> followList;
        
        public Author (String author, int followers) { 
            this.name = author;
            this.followers = followers; 
            this.followList = new HashSet<String>();
        }
        
        public void addFollower(String name) {
            followList.add(name);
            followers = followList.size();
        }
                
        public int compareTo(Author that) {
            if (this.followers < that.followers) { return -1; }
            else if (this.followers == that.followers) { return 0; }
            else { return 1; }
        }

        
    }
    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}


 