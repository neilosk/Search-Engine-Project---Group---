package searchengine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
  static final int PORT = 8080;
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;

  private SearchSites pages;
  HttpServer server;

  WebServer(int port, String filename) throws IOException {
    pages = new SearchSites(filename);

    server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
    server.start();
    buildServer();
    printWelcomeMessage(port);
  }

  private void buildServer() {
    server.createContext("/", io -> respond(io, 200, "text/html", getFile("web/index.html")));
    server.createContext("/search", io -> search(io));
    server.createContext(
        "/favicon.ico", io -> respond(io, 200, "image/x-icon", getFile("web/favicon.ico")));
    server.createContext(
        "/code.js", io -> respond(io, 200, "application/javascript", getFile("web/code.js")));
    server.createContext(
        "/style.css", io -> respond(io, 200, "text/css", getFile("web/style.css")));
  }

  private void printWelcomeMessage(int port) {
    String msg = " WebServer running on http://localhost:" + port + " ";
    System.out.println("╭" + "─".repeat(msg.length()) + "╮");
    System.out.println("│" + msg + "│");
    System.out.println("╰" + "─".repeat(msg.length()) + "╯");
  }

  // void search(HttpExchange io) {
  // var searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
  // var response = new ArrayList<String>();
  // for (var page : search(searchTerm)) {
  // response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
  // page.get(0).substring(6), page.get(1)));
  // }
  // var bytes = response.toString().getBytes(CHARSET);
  // respond(io, 200, "application/json", bytes);
  // }

  /**
   * The searchTerm could not be handled as a string so URLDecoder and toString
   * methods were implemented.
   * 
   * @param string
   */
  void search(HttpExchange string) {
    var searchTerm = string.getRequestURI().getRawQuery().split("=")[1];
    try {
      searchTerm = URLDecoder.decode(searchTerm, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      System.err.println(e);
    }
    var response = new ArrayList<String>();
    for (var page : pages.search(searchTerm)) {
      response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
          page.getUrl(), page.getTitle()));
    }
    var bytes = response.toString().getBytes(CHARSET);
    respond(string, 200, "application/json", bytes);
  }

  byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  void respond(HttpExchange io, int code, String mime, byte[] response) {
    try {
      io.getResponseHeaders()
          .set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
      io.sendResponseHeaders(200, response.length);
      io.getResponseBody().write(response);
    } catch (Exception e) {
    } finally {
      io.close();
    }
  }

  public static void main(final String... args) throws IOException {
    var filename = Files.readString(Paths.get("config.txt")).strip();
    new WebServer(PORT, filename);
  }
}