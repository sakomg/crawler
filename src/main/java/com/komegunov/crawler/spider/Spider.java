package com.komegunov.crawler.spider;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {
    private final Set<String> pagesVisited = new HashSet<>();
    private final List<String> pagesToVisit = new LinkedList<>();
    SpiderLeg spiderLeg = new SpiderLeg();

    /**
     * @param searchWord - Слово или строка по которой происходит поиск
     * @param currentUrl - текущий URL адрес
     */

    public String checkQuantitySearchWord(String searchWord, String currentUrl) {
        int quantitySearchWord = spiderLeg.countSearchForWord(searchWord);
        if (quantitySearchWord > 0) {
            return String.format("\n***Success*** Word %s found at %s", searchWord, currentUrl);
        } else {
            return "\n***Ooops*** On this page zero input words...";
        }
    }

    /**
     * @param url - Начальная точка для поиска
     * @param maxPagesToSearch - Максимальное кол-во проверяемых страниц
     */

    public String search(String url, int maxPagesToSearch) {
        while (this.pagesVisited.size() < maxPagesToSearch) {
            String currentUrl;
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            } else {
                currentUrl = this.nextUrl();
            }
            spiderLeg.crawl(currentUrl);
            this.pagesToVisit.addAll(spiderLeg.getLinks());
        }
        return "\n\n***Done*** Visited " + this.pagesVisited.size() + " web page(s)";
    }

    /**
     * @return Возвращает следующий URL для посещения (в порядке, в котором они были найдены). Также
     * производится проверка, чтобы убедиться, что этот метод не возвращает URL, который уже был посещен.
     */

    public String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
}