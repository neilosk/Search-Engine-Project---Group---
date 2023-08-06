package searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class SearchSites is responsible for creating an instance of the FileReader
 * class,
 * and creating a list of WebSites by implementing the readFile method from the
 * FileReader class.
 * * @param filename is used as a parameter of the constructor.
 */

public class SearchSites {

    private Set<WebSite> allWebSites;
    private int totalNumberWebsites;
  
    private Map<String, Set<WebSite>> index = new HashMap<>();


    /**
     * The constructor creates an instance of the FileReader class and creates a
     * list of WebSites by implementing the readFile method from the FileReader
     * class.
     * @param filename is used as a parameter of the constructor.
     */
    public SearchSites(String filename) {
        FileReader fr = new FileReader(filename);
        allWebSites = fr.readFile();
        index = populateIndex(allWebSites);
        totalNumberWebsites = allWebSites.size();
    }

    /**
     * The method search, creates a new object of the queryhandler, and gives it the inverted index
     * and the size of websites to match against. It the creates a list of the search result, which
     * eventually is returned.
     * 
     * @param searchTerm is the input from the user that we are searching for.
     * @return a list of websites that contain the searchTerm.
     */

    public List<WebSite> search(String searchTerm) {
        QueryHandler qh = new QueryHandler(index, totalNumberWebsites);
        List<WebSite> qhResult = qh.searchQuery(searchTerm);
        return qhResult;
    }

    /**
     * The method populateIndex creates a map of words and a set of websites that contains
     * the word. It loops through the set of websites, and for each website it
     * loops through the list of words. If the word is already in the map it
     * adds the website to the set of websites, that contains the word. If the
     * word is not in the map, it creates a new set of websites and adds the
     * website to the set.
     * @param result is the Set of websites that we are indexing   
     * @return a map of words and a set of websites that contains the word       
    */
    public Map<String, Set<WebSite>> populateIndex(Set<WebSite> result) {  
    
        for (WebSite web : result) {
            for (String word : web.getContent()) {
                if (index.containsKey(word)) {
                    index.get(word).add(web);
                } else {
                    Set<WebSite> set = new HashSet<>();
                    set.add(web);
                    index.put(word, set);
                }
            }
        }
        return index;
    }

}
