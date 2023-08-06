package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FileReader is responsible for reading the data file and assigns it to the
 * WebSite class. It also creates a list of WebSite objects.
 */

public class FileReader {

    private String filename;

    /**
     * The constructor creates an instance of the FileReader class and sets the
     * filename.
     * 
     * @param filename: is the name of the data file.
     */
    public FileReader(String filename) {
        this.filename = filename;
    }

    /**
     * The method readFile reads the data file and creates a list of WebSite
     * objects. It loops through the list of lines, and if the line starts with
     * *PAGE it adds the lines from the last *PAGE to the current line, and adds it
     * to a new
     * list. It then creates a new WebSite object and adds it to a set of
     * WebSites.
     * 
     * @return a set of WebSite objects.
     */

    public Set<WebSite> readFile() {
        List<List<String>> pages = new ArrayList<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        var lastIndex = lines.size();
        for (var i = lines.size() - 1; i >= 0; --i) {
            if (lines.get(i).startsWith("*PAGE")) {
                pages.add(lines.subList(i, lastIndex));
                lastIndex = i;

            }
        }
        Collections.reverse(pages);

        Set<WebSite> allWebSites = new HashSet<>();
        for (List<String> site : pages) {
            WebSite webSite = createWebSite(site);
            if (webSite != null) {
                allWebSites.add(webSite);
            }
        }
        return allWebSites;
    }

    /**
     * The method createWebSite creates a new WebSite object and returns it. It
     * takes a list of lines from the data file and assigns the first line to the
     * url, the second line to the title, and the rest of the lines to the content.
     * It first checks if the list of lines is less than 3, and if it is it returns
     * null (No website is made). It then removes the first 6 characters from the
     * url, and makes all the content lowercase. It then creates a new WebSite
     * by using the substring of the url, the title and the content. 
     * object and returns it.    
     * @param page is a list of lines from the data file.
     * @return a new WebSite object.
     */
    public WebSite createWebSite(List<String> page) {

        if (page.size() < 3) {
            return null;
        }

        String url = page.get(0);

        url = url.substring(6);

        String title = page.get(1);
        List<String> content = new ArrayList<>();
        for (int i = 2; i < page.size(); i++) {
            content.add(page.get(i).toLowerCase());
        }

        return new WebSite(url, title, content);

    }
}
