package twitter;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.time.Instant;
import java.util.Scanner;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        Instant start = null;
        Instant end = null;
        for (Tweet tweet : tweets) {
            Instant tweetInstant = tweet.getTimestamp();
            if (start == null && end == null) {
                start = tweetInstant;
                end = tweetInstant;
            }
            else if (start.compareTo(tweetInstant) > 0) { start = tweetInstant; }
            else if (end.compareTo(tweetInstant) < 0) { end = tweetInstant; }   
        }
        Timespan minInterval = new Timespan(start, end);
        return minInterval;
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<String>();
        Pattern myPattern = Pattern.compile("(^|[^a-zA-Z0-9-_])[@][a-zA-Z0-9-_]+");
        for (Tweet tweet : tweets) {
            Scanner scanner = new Scanner(tweet.getText());
            String nextMention = scanner.findInLine(myPattern);
            while (nextMention != null) {
                if (nextMention.charAt(1) == '@' )
                    mentionedUsers.add(nextMention.substring(2).toLowerCase());
                else
                    mentionedUsers.add(nextMention.substring(1).toLowerCase());
                nextMention = scanner.findInLine(myPattern);
            }
            scanner.close();
        }
        
        return mentionedUsers;
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
