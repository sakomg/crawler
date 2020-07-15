package com.komegunov.crawler.spider;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {

    private final Set<String> pagesVisited = new HashSet<>();
    private final List<String> pagesToVisit = new LinkedList<>();

    /**
     * Главная точка запуска Спайдер. Под капотом создаются вызовы, которые совершают HTTP-запрос и анализируют ответ (веб-страница).
     *
     * @param url        - Начальная точка для поиска
     * @param searchWord - Слово или строка по которой осуществляется поиск
     * @param maxPagesToSearch - Максимальное кол-во проверяемых страниц
     */

    public void search(String url, String searchWord, int maxPagesToSearch) {
        SpiderLeg leg = new SpiderLeg();
        while (this.pagesVisited.size() < maxPagesToSearch) {
            String currentUrl;
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            } else {
                currentUrl = this.nextUrl();
            }
            leg.crawl(currentUrl);
            boolean success = leg.searchForWord(searchWord);
            if (success) {
                System.out.println(String.format("***Success*** Word %s found at %s", searchWord, currentUrl));
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println("\n***Done*** Visited " + this.pagesVisited.size() + " web page(s)");
    }

    /**
     * @return Возвращает следующий URL для посещения (в порядке, в котором они были найдены). Также делаем проверку, чтобы
     * убедиться, что этот метод не возвращает URL, который уже был посещен.
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