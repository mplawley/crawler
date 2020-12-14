package com.lawley.service.impl;

import com.lawley.service.SitemapService;
import org.springframework.stereotype.Service;

@Service
public class SitemapServiceImpl implements SitemapService {
    @Override
    public String crawlSite(String url) {
        return "Hi";
    }
}
