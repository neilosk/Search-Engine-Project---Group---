package searchengine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSitesTest {
@Test
void populateIndex_testfileSize() {
FileReader fr = new FileReader("data/test-file.txt");
Set<WebSite> websites = fr.readFile();
Map<String, Set<WebSite>> index = new HashMap<>();
SearchSites aw = new SearchSites("data/test-file.txt");
index = aw.populateIndex(websites);
assertEquals(3, index.size());
}

@Test
void populateIndex_testfileErrorSize(){
FileReader fr = new FileReader("data/test-file-errors.txt");
Set<WebSite> websites = fr.readFile();
Map<String, Set<WebSite>> index = new HashMap<>();
SearchSites aw = new SearchSites("data/test-file-errors.txt");
index = aw.populateIndex(websites);
assertEquals(3, index.size());
}


@Test
void search_singleword(){
String searchTerm = "word1";
SearchSites aw = new SearchSites("data/test-file.txt");
List<WebSite> result = new ArrayList<>(aw.search(searchTerm));
assertEquals(2, result.size());
}

@Test
void search_singleword_test(){
String searchTerm = "test";
SearchSites aw = new SearchSites("data/test-file.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(0, result.size());
}

@Test
void search_twoWords_OR(){
String searchTerm = "word1 OR word2";
SearchSites aw = new SearchSites("data/test-file.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(2, result.size());
}

@Test
void search_twoWords_OR_tests(){
String searchTerm = "test1 OR test2";
SearchSites aw = new SearchSites("data/test-file.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(0, result.size());
}

@Test
void search_twoWords_OR_wordandtest(){
String searchTerm = "word2 OR test2";
SearchSites aw = new SearchSites("data/test-file.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(1, result.size());
}

@Test
void search_singleword_file_errors(){
String searchTerm = "word1";
SearchSites aw = new SearchSites("data/test-file-errors.txt");
List<WebSite> result = new ArrayList<>(aw.search(searchTerm));
result.size();
assertEquals(2, result.size());
}

@Test
void search_singleword_test_file_errors(){
String searchTerm = "test";
SearchSites aw = new SearchSites("data/test-file-errors.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(0, result.size());
}

@Test
void search_twoWords_OR_file_errors(){
String searchTerm = "word1 OR word2";
SearchSites aw = new SearchSites("data/test-file-errors.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(2, result.size());
}

@Test
void search_twoWords_OR_tests_file_errors(){
String searchTerm = "test1 OR test2";
SearchSites aw = new SearchSites("data/test-file-errors.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(0, result.size());
}

@Test
void search_twoWords_OR_wordandtest_file_errors(){
String searchTerm = "word2 OR test2";
SearchSites aw = new SearchSites("data/test-file-errors.txt");
List<WebSite> result = aw.search(searchTerm);
assertEquals(1, result.size());
}

@Test
void search(){
SearchSites aw = new SearchSites("data/test-file.txt");
List<WebSite> result = aw.search("word1");
List<String> keywords1 = List.of("word1", "word2");
List<String> keywords2 = List.of("word1", "word3");

Set<WebSite> expected = Set.of(
new WebSite("http://page2.com", "title2", keywords2),
new WebSite("http://page1.com", "title1", keywords1)
);
assertEquals(expected.stream().filter(webSite ->
webSite.getUrl().equals("http://page1.com")).findFirst().get().getTitle(),
result.stream().filter(webSite ->
webSite.getUrl().equals("http://page1.com")).findFirst().get().getTitle());
assertEquals(expected.stream().filter(webSite ->
webSite.getUrl().equals("http://page2.com")).findFirst().get().getTitle(),
result.stream().filter(webSite ->
webSite.getUrl().equals("http://page2.com")).findFirst().get().getTitle());
}
}
