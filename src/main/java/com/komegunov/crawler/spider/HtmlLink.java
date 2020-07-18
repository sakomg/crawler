package com.komegunov.crawler.spider;

public class HtmlLink {

    String link;
    String linkText;

    HtmlLink() {
    }

    @Override
    public String toString() {
        return "Link : " + this.link +
                " Link Text : " + this.linkText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = replaceInvalidChar(link);
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    private String replaceInvalidChar(String link) {
        link = link.replaceAll("'", "");
        link = link.replaceAll("\"", "");
        return link;
    }
}
