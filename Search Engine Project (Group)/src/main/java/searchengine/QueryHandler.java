package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * QueryHandler is responsible for handling the queries. It splits the query
 * into a list of words and adds it to a list of queries.
 * It then searches for the query in the index and returns
 * a set of websites that contain the query.
 */
public class QueryHandler {

    private Map<String, Set<WebSite>> index;
    private List<String[]> query = new ArrayList<String[]>();
    private int totalNumberWebsites;
    private int queryGroupIndex;

    /**
     * The constructor creates an instance of the QueryHandler class,
     * and sets the index and the total number of websites.
     * 
     * @param index: is the map of words and a set of websites that
     *               contains the word.
     * @param totalNumberWebsites: is the total number of websites.
     */
    public QueryHandler(Map<String, Set<WebSite>> index, int totalNumberWebsites) {
        this.index = index;
        this.totalNumberWebsites = totalNumberWebsites;
        queryGroupIndex = 0;
    }

    /**
     * The method searchQuery creates a set of websites that contain the query. It
     * loops through the list of queries and for each query it calls the searchStrings
     * method, and then calls the scoreFinder method, to set the score of each individual WebSite
     * to the returned Set of WebSites.
     * Then is adds the result to a list of websites and calls the rankedResults method
     * to rank the WebSites according to the given score.
     * 
     * @param searchTerm: is the input from the user that we are searching for.
     * @return a set of websites that contain the query.
     */
    public List<WebSite> searchQuery(String searchTerm) {
        query.addAll(querySplit(searchTerm));
        List<WebSite> finalResult = new ArrayList<WebSite>();
        for (String[] queryWords : query) {
            finalResult.addAll(scoreFinder(searchStrings(queryWords), finalResult));
        }
        finalResult = rankedResults(finalResult);
        return finalResult;
    }

    /**
     * This method splits the query into a list of words and adds it to a list of
     * queries. It splits the query by "OR" and then splits the query by " " and
     * adds it to the list of queries. It then returns the list of queries.
     * 
     * @param searchTerm: is the input from the user that we are searching for.
     * @return a list of queries.
     */
    public List<String[]> querySplit(String searchTerm) {
        List<String[]> result = new ArrayList<String[]>();
        String[] orQuery = searchTerm.split(" OR ");
        for (String string : orQuery) {
            result.add(string.trim().toLowerCase().split(" "));
        }
        return result;
    }



    /**
     * The searchStrings method searches for each word from each query
     * in the  inverted index, and returns a set of websites that contain each query.
     * It uses the index to search for a group of words from each group of queries.
     * In case any of the words are not found it returns an empty set.
     * 
     * @param queryWords: group of words from each group of queries (between every
     *                    "OR").
     * @return a set of websites that contain these group of words.
     */
    public Set<WebSite> searchStrings(String[] queryWords) {
        Set<WebSite> result = new HashSet<WebSite>();
        boolean falseWord = false;
        for (String word : queryWords) {
            if (falseWord == false) {
                if (result.isEmpty()) {
                    for (String i : index.keySet()) {
                        if (i.equals(word)) {
                            result.addAll(index.get(i));
                        }
                    }
                    if (result.isEmpty()) {
                        falseWord = true;
                    }
                } else {
                    result = result.stream()
                            .filter(website -> website.getContent().contains(word))
                            .collect(Collectors.toSet());
                    if (result.isEmpty()) {
                        falseWord = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * This scoreFinder method calculates the score of each website that matches the
     * query. It loops through the set of websites that match the query and
     * calculates the score of each website. It then calls the tfIdf method to
     * calculate the tfIdf score of each website. It keeps the max score of each set of words
     * of the searchTerm by comparing the score that the WebSite currently has with the last calculated one.
     * It then adds the website to a list of websites and returns the list.
     *
     * @param result: is the set of websites that match the query.
     * @return a sorted List of websites that match the query.
     */

    public List<WebSite> scoreFinder(Set<WebSite> result, List<WebSite> finalResult) {
        List<WebSite> rankedWebSites = new ArrayList<>();
        for (WebSite webSite : result) {
            List<String> content = webSite.getContent();
            double contentSize = content.size();
            double appearence = 0;
            double weight = 0;

            for (String word : query.get(queryGroupIndex)) {
                for (String contentWord : content) {
                    if (word.equals(contentWord)) {
                        appearence++;
                    }

                }

            }

            weight = (appearence / contentSize);
            var initialScore = webSite.getScore();
            webSite.setScore(weight);
            var tfIdf = tfIdf(webSite, result);
            webSite.setScore(tfIdf);
            if (webSite.getScore() < initialScore) {
                webSite.setScore(initialScore);
            }
            if (!finalResult.contains(webSite)) {
                rankedWebSites.add(webSite);
            }
        }

        queryGroupIndex++;
        return rankedWebSites;
    }

    /**
     * This rankedResults method sorts the final result by the score of the website
     * and then by the title of the website. It then returns the final result. It
     * uses the Comparator interface to compare the score of the websites and then
     * the title of the websites. It then reverses the list to get the websites
     * with the highest score first.
     * 
     * @param finalResult: is the list of websites that match the query.
     * @return a sorted List of websites that match the query.
     */ 
    public List<WebSite> rankedResults(List<WebSite> finalResult) {
        Comparator<WebSite> c = Comparator.comparingDouble(WebSite::getScore).thenComparing(WebSite::getTitle);
        Collections.sort(finalResult, c);
        Collections.reverse(finalResult);
        return finalResult;
    }
    

    /**
     * This inverseRank method calculates the inverse rank of the result and returns
     * it.
     * 
     * @param result is the set of websites that match each query group.
     * @return the inverse rank of the result.
     */
    public double inverseRank(Set<WebSite> result) {
        double inverseRank = Math.log(totalNumberWebsites / (result.size()));
        return inverseRank;
    }


    /**
     * This tfIdf method calculates the tfIdf of the website and sets it as the
     * score of the website. It multiplies the score of the website with the
     * inverseRank of the result. It then returns the tfIdf of the website.
     *
     * @param website is the website that we are calculating the tfIdf for.
     * @param result is the set of websites that match each query group.
     * @return the tfIdf of the website.
     */
    public double tfIdf(WebSite website, Set<WebSite> result) {
        double tfIdf = 0;
        tfIdf = website.getScore() * inverseRank(result);
        return tfIdf;
    }

    /**
     * This method sets the query. It is used for testing.
     * @param query is the list of queries.
     */

    public void setQuery(List<String[]> query) {
        this.query = query;
    }

    /**
     * This method sets the queryGroupIndex. It is used for testing.
     * @param queryGroupIndex
     */
    public void setQueryGroupIndex(int queryGroupIndex) {
        this.queryGroupIndex = queryGroupIndex;
    }
}


