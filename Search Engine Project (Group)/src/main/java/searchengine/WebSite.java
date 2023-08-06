package searchengine;

import java.util.Comparator;
import java.util.List;

/**
 * The WebSite Class is created in order to handle the WebSites as objects in
 * the Pages lists
 * i.e. instead of having a List<List<String> we can have List<WebSite>.
 * Parameters given on the website are stored in the WebSite object and are as
 * follows:
 * 
 * @param url,
 * @param title,
 * @param content,
 */

public class WebSite implements Comparable<WebSite> {
    private String url;
    private String title;
    private List<String> content;
    private double score;

    /**
     * The constructor creates an instance of the WebSite class and sets the
     * parameters.
     * 
     * @param url:     is the url of the website
     * @param title:   is the title of the website
     * @param content: is the content of the website
     *@param score:   is the score of the website and it is set initially to 0  
     */

    public WebSite(String url, String title, List<String> content) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.score = 0;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getContent() {
        return content;
    }

    /**
     * The method getWords returns a string of all the words in the content of the
     * website.
     * 
     * @return a string of all the words in the content of the website.
     */

    public String getWords() {
        content = getContent();
        String allWords = "";
        for (String word : content) {
            allWords += word + " ";
        }
        return allWords = allWords.substring(0, allWords.length() - 1);
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public double getScore() {
        return score;
    }

    public void setScore(double weight) {
        this.score = weight;
    }

    /**
    *   The method equals overrides the equal method and checks if the url of the website is the same as the url of the other website.
    *   @param o: is the other website.
    *   @return true if the urls are the same and false if they are not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WebSite page = (WebSite) o;
        return url.equals(page.url);
    }

    /**
     *   The method compareTo overrides the compareTo method and compares the websites by their score and then by their url.
     *   @param o: is the other website.
     */
    @Override
    public int compareTo(WebSite o) {
        return Comparator.comparing(WebSite::getScore).reversed().thenComparing(WebSite::getUrl).compare(this, o);
    }
}