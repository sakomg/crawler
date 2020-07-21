package com.komegunov.crawler.spider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SpiderLeg {
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final String DATA_FILE = "src/main/resources/links/data.csv";
    private final List<String> links = new LinkedList<>();
    private Document htmlDocument = new Document("");
    private int countSearchWord = 0;

    /**
     * @param url - Посещаемый URL
     * @return кол-во найденных ссылок или же сообщение об их отсутствии
     */

    public String crawl(String url) {
        try {
            connectToWebPage(url);
            Elements linksOnPage = htmlDocument.select("a[href]");
            for (Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
                writeLinksInCSVFile(   link.absUrl("href").intern() + "\n", DATA_FILE);
            }
            return "\nFound (" + linksOnPage.size() + ") links";
        } catch (IOException ioe) {
            return "\nWe were not successful in our HTTP request";
        }
    }

    /**
     * @param url - посещаемый URL
     * @return информация о результате соеденения
     */

    public String connectToWebPage(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        this.htmlDocument = connection.get();
        if (connection.response().statusCode() == 200) {
            return "***OK*** Received web page at " + url;
        } else if (!connection.response().contentType().contains("text/html")) {
            return "***Fail*** Retrieved something other than HTML";
        } else
            return "***Undefined***";
    }

    /**
     * @param searchWord - слово или строка для поиска
     * @return кол-во найденных слов
     */

    public int countSearchForWord(String searchWord) {
        checkNullHtmlDocument(searchWord);
        final String[] wordWithText = parseDocumentOnTheWord(htmlDocument.text());
        for (int i = 0; i < countWords(htmlDocument.text()); i++) {
            if (wordWithText[i].equals(searchWord)) {
                countSearchWord++;
            }
        }
        return countSearchWord;
    }

    /**
     * Проверка полученной html страницы на null
     * @param searchWord - слово или строка для поиска
     */

    public String checkNullHtmlDocument(String searchWord) {
        if (this.htmlDocument == null) {
            return "\nERROR! Call  before performing analysis on the document";
        }
        return "\nSearching for the word " + searchWord + "...";
    }

    /**
     * @param html - данные содержащиеся на странице
     * @return кол-во слов на странице
     */

    public int countWords(String html) {
        Document dom = Jsoup.parse(html);
        return dom.text().split(" ").length;
    }

    /**
     * @param html - данные содержащиеся на странице
     * @return полное содержимое страницы с выделенными словами
     */

    public String[] parseDocumentOnTheWord(String html) {
        Document document = Jsoup.parse(html, "UTF-8");
        return document.text().split(" ");
    }

    /**
     * @return найденные ссылки
     */

    public List<String> getLinks() {
        return this.links;
    }

    public String displayCountSearchWord(String searchWord) {
        return "\n***End*** Quantity find word: " + countSearchForWord(searchWord);
    }

    /**
     * @param buffer - данные, которые будут записаны в файл
     * @param path - место расположения csv-файла для записи ссылок
     */

    public void writeLinksInCSVFile(String buffer, String path) throws IOException {
        FileWriter wFile = new FileWriter(path, true);
        wFile.write(buffer);
        wFile.close();
    }
}
