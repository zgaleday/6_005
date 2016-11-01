package twitter;

import java.util.List;
import java.util.ArrayList;
import java.time.Instant;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> byAuthor = new ArrayList<Tweet>();
        username = username.toLowerCase();
        for (Tweet tweet : tweets) {
            if(tweet.getAuthor().toLowerCase().equals(username))
                byAuthor.add(tweet);
        }
        return byAuthor;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        Instant start = timespan.getStart();
        Instant end = timespan.getEnd();
        List<Tweet> inTimespan = new ArrayList<Tweet>();
        
        for (Tweet tweet : tweets) {
            boolean startCompare = start.compareTo(tweet.getTimestamp()) <= 0;
            boolean endCompare = end.compareTo(tweet.getTimestamp()) >= 0;
            if (startCompare && endCompare)
                inTimespan.add(tweet);                
        }
        return inTimespan;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        HashSet<Tweet> matches = new HashSet<Tweet>();
        for (Tweet tweet : tweets) {
            for (String word : words) {
                if (tweet.getText().toLowerCase().contains(word.toLowerCase())) 
                    matches.add(tweet);
            }
        }
        
        return new ArrayList<Tweet>(matches);
    }
    
    /**
     * Find users who used a certain HashTag. 
     * Hashtags are defined as a string of letter, numbers, _, or - proceeded by a # character
     * Hashtags are not case sensitive so there should only be on entry for a hashtag even if it is seen 
     * with multiple capitalizations.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * 
     * @return hashtagMap
     *          a map that contains all hashtags (as defined above) seen in the tweets
     *          and the authors who wrote the tweets containing the hashtags. Authors are 
     *          case insensitive and should only appear once in each hashtags entry. 
     */
    public static Map<String, HashSet<String>> usedHashtag(List<Tweet> tweets) {
        Map<String, HashSet<String>> usedHashtags = new HashMap<String, HashSet<String>>();
        Pattern myPattern = Pattern.compile("(^|[^a-zA-Z0-9-_])[#][a-zA-Z0-9-_]+");    
        for (Tweet tweet : tweets) {
            Scanner scanner = new Scanner(tweet.getText());
            String nextHashtag = scanner.findInLine(myPattern);
            while (nextHashtag != null) {
                if (nextHashtag.charAt(1) == '#')
                    nextHashtag = nextHashtag.substring(2).toLowerCase();
                else
                    nextHashtag = nextHashtag.substring(1).toLowerCase();
                if (!usedHashtags.containsKey(nextHashtag))
                    usedHashtags.put(nextHashtag, new HashSet<String>());
                usedHashtags.get(nextHashtag).add(tweet.getAuthor().toLowerCase());
                nextHashtag = scanner.findInLine(myPattern);
            }
            scanner.close();
        }
        return usedHashtags;
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
