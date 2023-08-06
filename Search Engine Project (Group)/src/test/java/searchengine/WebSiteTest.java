package searchengine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebSiteTest {
    
    @Test
    void testWebSite() {
        WebSite webSite = new WebSite("http://page1.com", "title1", List.of("word1", "word2"));
        assertEquals("http://page1.com", webSite.getUrl());
        assertEquals("title1", webSite.getTitle());
        assertEquals("word1 word2", webSite.getWords());
    }

    @Test
    void testWebSiteError() {
        WebSite webSite = new WebSite("http://page1.com", "title1", List.of("word1", "word2"));
        assertEquals("http://page1.com", webSite.getUrl());
        assertEquals("title1", webSite.getTitle());
        assertEquals("word1 word2", webSite.getWords());
    }


   @Test
    void isEmpty_false() {
        WebSite webSite = new WebSite("http://page1.com", "title1", List.of("word1", "word2"));
        assertEquals(false, webSite.isEmpty());
    }

    @Test
    void isEmpty_true() {
        WebSite webSite = new WebSite("http://page1.com", "title1", List.of());
        assertEquals(true, webSite.isEmpty());
    }

}