package searchengine;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueryHandlerTest {
private QueryHandler queryHandler;
private Map<String, Set<WebSite>> index;

@BeforeEach
public void Setup(){
        Set<WebSite> websites = new HashSet<>();
        SearchSites aw = new SearchSites("data/queryHandler-test.txt");
        index = aw.populateIndex(websites);
        queryHandler = new QueryHandler(index, 5);
}

@Test
public void querySplitTest(){
        String query = "Cat dog OR horse donkey";
        List<String[]> actual = new ArrayList<>();
        String[] expected = {"cat","dog"};
        String[] expected2 = {"horse","donkey"};
        actual = queryHandler.querySplit(query);
        assertEquals(2,actual.size());
        assertEquals(expected[0], actual.get(0)[0]);
        assertEquals(expected[1], actual.get(0)[1]);
        assertEquals(expected2[0], actual.get(1)[0]);
        assertEquals(expected2[1], actual.get(1)[1]);
}



@Test
void searchQyery_Test(){
        String query = "Cat horse";
        List<WebSite> actualRanking = new ArrayList<>();
        actualRanking = queryHandler.searchQuery(query);
        assertEquals(2,actualRanking.size());
}

@Test  
void searchQyery_Test_OR(){
        String query = "Cat OR word1";
        List<WebSite> actualRanking = new ArrayList<>();
        actualRanking = queryHandler.searchQuery(query);
        assertEquals(5,actualRanking.size());
}

@Test  
void searchQyery_Test_rank(){
        String query = "word1";
        List<WebSite> actualRanking = new ArrayList<>();
        actualRanking = queryHandler.searchQuery(query);
        assertEquals("http://word1favorite.com",actualRanking.get(0).getUrl());
}

@Test  
void searchQyery_Test_rank_OR(){
        String query = "Dog OR word2";
        List<WebSite> actualRanking = new ArrayList<>();
        actualRanking = queryHandler.searchQuery(query);
        assertEquals("http://google4.com",actualRanking.get(0).getUrl());
}
@Test
void searchFalseword_Test(){
        String query = "word5 word2 OR word2 word6";
        List<WebSite> actualRanking = new ArrayList<>();
        actualRanking = queryHandler.searchQuery(query);
        assertEquals(0,actualRanking.size());
}

@Test
void searchStringsTest1(){
        String[] query = {"word1"};
        Set<WebSite> actualRanking = new HashSet<WebSite>();
        actualRanking = queryHandler.searchStrings(query);
        assertEquals(3,actualRanking.size());
}

@Test
void searchStringsTest2(){
        String[] query = {"word1","word2"};
        Set<WebSite> actualRanking = new HashSet<WebSite>();
        actualRanking = queryHandler.searchStrings(query);
        assertEquals(2,actualRanking.size());
}

@Test
void searchStringsTestFail(){
        String[] query = {"wfsfddsfdsf"};
        Set<WebSite> actualRanking = new HashSet<WebSite>();
        actualRanking = queryHandler.searchStrings(query);
        assertEquals(0,actualRanking.size());
}

@Test
void scoreFinderTest(){
        String[] query = {"dog"};
        List<String[]> queryList = new ArrayList<>();
        queryList.add(query);
        Set<WebSite> actualRanking = new HashSet<WebSite>();
        actualRanking.addAll(queryHandler.searchStrings(query));
        List<WebSite> finalResult = new ArrayList<WebSite>();
        queryHandler.setQuery(queryList);
        finalResult = queryHandler.scoreFinder(actualRanking, finalResult);
        assertEquals(0.5364793041447,finalResult.get(0).getScore());
}

@Test
void scoreFinderTest2(){
        String[] query1 = {"word2"};
        String[] query2 = {"word1"};
        List<String[]> queryList = new ArrayList<>();
        queryList.add(query1);
        queryList.add(query2);
        List<WebSite> finalResult = new ArrayList<WebSite>();
        queryHandler.setQuery(queryList);
        for (String[] queryWords : queryList) {
        finalResult.addAll(queryHandler.scoreFinder(queryHandler.searchStrings(queryWords), finalResult));
        }
        assertEquals(0.34657359027997264, finalResult.stream().filter(webSite -> webSite.getTitle().equals("title1")).findFirst().get().getScore());
        // assertEquals("title2", finalResult.get(0).getTitle());
}

@Test
void scoreFinderTest3(){
        String[] query1 = {"word2"};
        String[] query2 = {"word3"};
        List<String[]> queryList = new ArrayList<>();
        queryList.add(query1);
        queryList.add(query2);
        List<WebSite> finalResult = new ArrayList<WebSite>();
        queryHandler.setQuery(queryList);
        for (String[] queryWords : queryList) {
        finalResult.addAll(queryHandler.scoreFinder(queryHandler.searchStrings(queryWords), finalResult));
        }
        assertEquals(0.3218875824868201,finalResult.stream().filter(webSite -> webSite.getTitle().equals("title2")).findFirst().get().getScore());
}

@Test
void scoreFinderTest4(){
        String[] query1 = {"word3"};
        String[] query2 = {"word2"};
        List<String[]> queryList = new ArrayList<>();
        queryList.add(query1);
        queryList.add(query2);
        List<WebSite> finalResult = new ArrayList<WebSite>();
        queryHandler.setQuery(queryList);
        for (String[] queryWords : queryList) {
        finalResult.addAll(queryHandler.scoreFinder(queryHandler.searchStrings(queryWords), finalResult));
        }
        assertEquals(0.3218875824868201,finalResult.stream().filter(webSite -> webSite.getTitle().equals("title2")).findFirst().get().getScore());
}

@Test
void rankedResultsTest1(){
        String[] query = {"word1"};
        Set<WebSite> actualRanking = new HashSet<>();
        actualRanking = queryHandler.searchStrings(query);
        List<WebSite> actualRankingList = new ArrayList<>(actualRanking);
        List<WebSite> finalResult = new ArrayList<WebSite>();
        finalResult = queryHandler.rankedResults(actualRankingList);
        assertEquals("titleofword1",finalResult.get(0).getTitle());
}

@Test
void rankedResultsTest2(){
        String[] query = {"word1","word2"};
        Set<WebSite> actualRanking = new HashSet<>();
        actualRanking = queryHandler.searchStrings(query);
        List<WebSite> actualRankingList = new ArrayList<>(actualRanking);
        List<WebSite> finalResult = new ArrayList<WebSite>();
        finalResult = queryHandler.rankedResults(actualRankingList);
        assertEquals("title2",finalResult.get(0).getTitle());
}

@Test
void inverseRankTest(){
        String[] query = {"word1","word2"};
        Set<WebSite> websites = new HashSet<>();
        websites = queryHandler.searchStrings(query);
        double actual = queryHandler.inverseRank(websites);
        assertEquals(0.6931471805599453,actual);
}

@Test
void Scoring_Test(){
        String query = "cat";
        List<WebSite> actualRanking = new ArrayList<>();
        Double initialScore = index.get("cat").iterator().next().getScore();
        actualRanking = queryHandler.searchQuery(query);
        assertEquals( 0.34657359027997264 , actualRanking.get(0).getScore() );
        assertTrue( initialScore < actualRanking.get(0).getScore() );
        
}



}
