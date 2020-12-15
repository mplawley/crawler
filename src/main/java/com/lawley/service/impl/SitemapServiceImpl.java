package com.lawley.service.impl;

import com.lawley.domain.Crawl;
import com.lawley.service.SitemapService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SitemapServiceImpl implements SitemapService {
    private static final String USER_AGENT_ID = "com.lawley.sitecrawler";
    private static final String CRAWL_STORAGE = "src/main/resources/crawler4j";

    @Override
    public Crawl crawlSite(Crawl crawl) throws Exception {
        CrawlConfig config = configureCrawler();
        int numCrawlers = 12;
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(crawl.getUrl());
        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = () -> new HtmlCrawler(crawl);
        controller.start(factory, numCrawlers);
        return crawl;
    }

    private CrawlConfig configureCrawler() {
        File crawlStorage = new File(CRAWL_STORAGE);
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        config.setMaxDepthOfCrawling(16);
        config.setMaxPagesToFetch(1000);
        config.setUserAgentString(USER_AGENT_ID);
        config.setIncludeBinaryContentInCrawling(true);
        return config;
    }
}
