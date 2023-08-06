package searchengine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class FileReaderTest {

    
    @Test
    void testReadFile() {
        FileReader fileReader = new FileReader("data/test-file.txt");
        Set<WebSite> webSites = fileReader.readFile();
        assertEquals(2, webSites.size());
        assertEquals("http://page2.com", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page2.com")).findFirst().get().getUrl());
        assertEquals("http://page1.com", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page1.com")).findFirst().get().getUrl());
        assertEquals("title1", webSites.stream().filter(webSite -> webSite.getTitle().equals("title1")).findFirst().get().getTitle());
        assertEquals("title2", webSites.stream().filter(webSite -> webSite.getTitle().equals("title2")).findFirst().get().getTitle());
        assertEquals("word1 word2", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page1.com")).findFirst().get().getWords());
        assertEquals("word1 word3", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page2.com")).findFirst().get().getWords());
    }

    @Test
    void createWebSiteTest(){
        List<String> lines = new ArrayList<>();
        lines.add("*PAGE:http://page1.com");
        lines.add("title1");
        lines.add("word1 word2");
        FileReader fileReader = new FileReader("data/test-file.txt");  
        WebSite website = fileReader.createWebSite(lines);
        assertEquals("http://page1.com", website.getUrl());
        assertEquals("title1", website.getTitle());
        assertEquals("word1 word2", website.getWords());
    }

    @Test
    void createWebSiteTest_Error(){
        List<String> lines = new ArrayList<>();
        lines.add("*PAGE:http://page1.com");
        lines.add("title1");
        FileReader fileReader = new FileReader("data/test-file.txt");  
        WebSite website = fileReader.createWebSite(lines);
        assertNull(website);
    }

    @Test
    void createWebSiteTest_Error2(){
        List<String> lines = new ArrayList<>();
        lines.add("*PAGE:http://page1.com");
        lines.add("word1 word2");
        FileReader fileReader = new FileReader("data/test-file.txt");  
        WebSite website = fileReader.createWebSite(lines);
        assertNull(website);
    }

    @Test
    void testError_ReadFile() {
        FileReader fileReader = new FileReader("data/test-file-errors.txt");
        Set<WebSite> webSites = fileReader.readFile();
        assertEquals(2, webSites.size());
        assertEquals("http://page2.com", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page2.com")).findFirst().get().getUrl());
        assertEquals("http://page1.com", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page1.com")).findFirst().get().getUrl());
        assertEquals("title1", webSites.stream().filter(webSite -> webSite.getTitle().equals("title1")).findFirst().get().getTitle());
        assertEquals("title2", webSites.stream().filter(webSite -> webSite.getTitle().equals("title2")).findFirst().get().getTitle());
        assertEquals("word1 word2", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page1.com")).findFirst().get().getWords());
        assertEquals("word1 word3", webSites.stream().filter(webSite -> webSite.getUrl().equals("http://page2.com")).findFirst().get().getWords());
       
    }

   
 }

