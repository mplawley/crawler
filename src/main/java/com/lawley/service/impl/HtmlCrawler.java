package com.lawley.service.impl;

import com.lawley.domain.Crawl;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.Set;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mp4|zip|gz))$");
    private final Crawl crawl;

    public HtmlCrawler(Crawl crawl) {
        this.crawl = crawl;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
            && href.startsWith(this.crawl.getUrl());
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            String currentResult = crawl.getResult();
            currentResult += extractPageAndStats(page);
            crawl.setResult(currentResult);
        }
    }

    private String extractPageAndStats(Page page) {
        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
        String text = htmlParseData.getText();
        String html = htmlParseData.getHtml();
        Set<WebURL> links = htmlParseData.getOutgoingUrls();

        StringBuilder sb = new StringBuilder();
        sb.append("URL: ").append(page.getWebURL().getURL())
            .append("Text length: ").append(text.length()).append("\n")
            .append("Html length: ").append("\n")
            .append(html.length())
            .append("Number of outgoing links: ").append("\n")
            .append(links.size())
            .append("============\n");
        return sb.toString();
    }
}
