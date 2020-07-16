package com.komegunov.crawler.spider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SpiderLeg {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final List<String> links = new LinkedList<>();
    private Document htmlDocument;

    public String connectToWebPage(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        this.htmlDocument = connection.get();
        if (connection.response().statusCode() == 200) {
            return "\n***OK*** Received web page at " + url;
        } else if (!connection.response().contentType().contains("text/html")) {
            return "***Fail*** Retrieved something other than HTML";
        } else
            return "***Undefined***";
    }

    /**
     * Эта часть выполняет всю работу. Он делает HTTP-запрос, проверяет ответ, а затем
     * собирает все ссылки на странице. Выполните searchForWord после успешного сканирования
     *
     * @param url - Посещаемый URL
     * @return было ли сканирование успешным
     */

    public String crawl(String url) {
        try {
            connectToWebPage(url);
            Elements linksOnPage = htmlDocument.select("a[href]");
            for (Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
            return "\nFound (" + linksOnPage.size() + ") links";
        } catch (IOException ioe) {
            return "\nWe were not successful in our HTTP request";
        }
    }

    /**
     * Выполняет поиск в теле HTML-документа, который был получен.
     * Этот метод следует вызывать только после успешного сканирования.
     *
     * @param searchWord - Слово или строка для поиска
     * @return было ли найдено слово
     */

    public String searchForWord(String searchWord) {
        int countSearchWord = 0;
        checkNullHtmlDocument(searchWord);
        final String[] wordWithText = searchGivenWord(htmlDocument.text());
        for (int i = 0; i < countWords(htmlDocument.text()); i++) {
            if (wordWithText[i].equals(searchWord)) {
                countSearchWord++;
            }
        }
        return "\n***Good*** Quantity find word: " + countSearchWord;
    }

    public String checkNullHtmlDocument(String searchWord) {
        if (this.htmlDocument == null) {
            return "\nERROR! Call  before performing analysis on the document";
        }
        return "\nSearching for the word " + searchWord + "...";
    }

    public int countWords(String html) {
        Document dom = Jsoup.parse(html);
        return dom.text().split(" ").length;
    }

    public String[] searchGivenWord(String html) {
        Document document = Jsoup.parse(html, "UTF-8");
        return document.text().split(" ");
    }

    public List<String> getLinks() {
        return this.links;
    }

}
