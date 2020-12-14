package com.lawley.service.impl;

import com.lawley.service.CrawlService;
import com.lawley.domain.Crawl;
import com.lawley.repository.CrawlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Crawl}.
 */
@Service
@Transactional
public class CrawlServiceImpl implements CrawlService {

    private final Logger log = LoggerFactory.getLogger(CrawlServiceImpl.class);

    private final CrawlRepository crawlRepository;

    public CrawlServiceImpl(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }

    @Override
    public Crawl save(Crawl crawl) {
        log.debug("Request to save Crawl : {}", crawl);
        return crawlRepository.save(crawl);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Crawl> findAll(Pageable pageable) {
        log.debug("Request to get all Crawls");
        return crawlRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Crawl> findOne(Long id) {
        log.debug("Request to get Crawl : {}", id);
        return crawlRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Crawl : {}", id);
        crawlRepository.deleteById(id);
    }
}
