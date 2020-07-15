package com.komegunov.crawler.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLLinkExtractor {

    private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
    public static final String FILE = "src/main/resources/data.csv";
    private final Pattern patternTag;
    private final Pattern patternLink;
    Document doc = Jsoup.connect("http://www.mit.edu/").get();

    public HTMLLinkExtractor() throws IOException {
        patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
        patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
    }

    public Stack<HtmlLink> grabHTMLLinks(final String html) {
        Stack<HtmlLink> result = new Stack<>();
        Matcher matcherTag = patternTag.matcher(html);
        while (matcherTag.find()) {
            String href = matcherTag.group(1);
            String linkText = matcherTag.group(2);
            Matcher matcherLink = patternLink.matcher(href);
            while (matcherLink.find()) {
                String link = matcherLink.group(1);
                HtmlLink obj = new HtmlLink();
                obj.setLink(link);
                obj.setLinkText(linkText);
                result.add(obj);
            }
        }
        return result;
    }

    public void fileWriter(String buffer, String path) throws IOException {
        FileWriter wFile = new FileWriter(path, true);
        wFile.write(buffer);
        wFile.close();
    }

    public int countElon() {
        int counter = 0;
        for (int count = 0; count < doc.text().length(); count++) {
            if (doc.text().contains("Education")) {
                counter++;
            }
        }
        return counter;
    }

    public void print() {
        Elements questions = doc.select("a[href]");
        for (Element linked : questions) {
            System.out.println(linked.attr("href"));
        }
    }

    public static void main(String[] args) throws IOException {
        String html = "<a href='http://f1.hdnewfilm.club/films/3134-parazity.html'>hi1</a>" +
                ", <a href='http://f1.hdnewfilm.club/films/3134-parazity.html'>hi2</a>" +
                ", <a href='http://f1.hdnewfilm.club/films/3134-parazity.html'>hi3</a>";



        HTMLLinkExtractor hTMLLinkExtractor = new HTMLLinkExtractor();
        Stack<HtmlLink> htmlLinks = hTMLLinkExtractor.grabHTMLLinks(html);
        for (HtmlLink link : htmlLinks) {
            System.out.println(link.getLink());
            System.out.println(link.getLinkText());
            hTMLLinkExtractor.fileWriter(link.getLink() + " text: " + link.getLinkText() + "\n", FILE);
        }

        hTMLLinkExtractor.print();
        System.out.println("Кол-во Илонов: " + hTMLLinkExtractor.countElon());
    }
}
