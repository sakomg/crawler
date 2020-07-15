package com.komegunov.crawler.spider;

import javafx.application.Platform;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SpiderLeg {

    // Мы будем использовать фальшивый USER_AGENT, чтобы веб-сервер думал, что робот - это обычный веб-браузер.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final List<String> links = new LinkedList<>();
    private Document htmlDocument;

    /**
     * Эта часть выполняет всю работу. Он делает HTTP-запрос, проверяет ответ, а затем
     * собирает все ссылки на странице. Выполните searchForWord после успешного сканирования
     *
     * @param url - Посещаемый URL
     * @return было ли сканирование успешным
     */

    public boolean crawl(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT); //  получаем коннект, подставляя наш USER_AGENT
            Document htmlDocument = connection.get(); // сохраняем все в переменную
            this.htmlDocument = htmlDocument;
            if (connection.response().statusCode() == 200) { // делаем проверку на успешный HTTP запрос и показываем, что все окей.
                System.out.println("\n***OK*** Received web page at " + url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("***Fail*** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for (Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
            return true;
        } catch (IOException ioe) {
            System.out.println("We were not successful in our HTTP request");
            return false;
        }
    }

    /**
     * Выполняет поиск в теле HTML-документа, который был получен.
     * Этот метод следует вызывать только после успешного сканирования.
     *
     * @param searchWord - Слово или строка для поиска
     * @return было ли найдено слово
     */

    public boolean searchForWord(String searchWord) {
        int countSearchWord = 0;
        if (this.htmlDocument == null) {
            System.out.println("ERROR! Call  before performing analysis on the document");
            return false;
        }
        System.out.println("Searching for the word " + searchWord + "...");

        final boolean contains = this.htmlDocument.body().text().contains(searchWord);
        final String[] wordWithText = searchGivenWord(htmlDocument.text());
        for (int i = 0; i < countWords(htmlDocument.text()); i++) {
            if (wordWithText[i].equals(searchWord)) {
                countSearchWord++;
            }
        }
        System.out.println("Quantity find word: " + countSearchWord);
        return contains;
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

    public String getPrintReceivedWebPage(String str){
        return str;
    }

}
