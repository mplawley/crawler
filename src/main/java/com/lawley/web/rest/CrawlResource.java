package com.lawley.web.rest;

import com.lawley.domain.Crawl;
import com.lawley.service.CrawlQueryService;
import com.lawley.service.CrawlService;
import com.lawley.service.SitemapService;
import com.lawley.service.dto.CrawlCriteria;
import com.lawley.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.lawley.domain.Crawl}.
 */
@RestController
@RequestMapping("/api")
public class CrawlResource {

    private final Logger log = LoggerFactory.getLogger(CrawlResource.class);

    private static final String ENTITY_NAME = "crawl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrawlService crawlService;

    private final CrawlQueryService crawlQueryService;

    private final SitemapService sitemapService;

    public CrawlResource(CrawlService crawlService, CrawlQueryService crawlQueryService, SitemapService sitemapService) {
        this.crawlService = crawlService;
        this.crawlQueryService = crawlQueryService;
        this.sitemapService = sitemapService;
    }

    /**
     * {@code POST  /crawls} : Create a new crawl.
     *
     * @param crawl the crawl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crawl, or with status {@code 400 (Bad Request)} if the crawl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crawls")
    public ResponseEntity<Crawl> createCrawl(@Valid @RequestBody Crawl crawl) throws Exception {
        log.debug("REST request to save Crawl : {}", crawl);
        if (crawl.getId() != null) {
            throw new BadRequestAlertException("A new crawl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sitemapService.crawlSite(crawl.getUrl());
        Crawl result = crawlService.save(crawl);
        return ResponseEntity.created(new URI("/api/crawls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crawls} : Updates an existing crawl.
     *
     * @param crawl the crawl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crawl,
     * or with status {@code 400 (Bad Request)} if the crawl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crawl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crawls")
    public ResponseEntity<Crawl> updateCrawl(@Valid @RequestBody Crawl crawl) throws URISyntaxException {
        log.debug("REST request to update Crawl : {}", crawl);
        if (crawl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Crawl result = crawlService.save(crawl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, crawl.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /crawls} : get all the crawls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crawls in body.
     */
    @GetMapping("/crawls")
    public ResponseEntity<List<Crawl>> getAllCrawls(CrawlCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Crawls by criteria: {}", criteria);
        Page<Crawl> page = crawlQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crawls/count} : count all the crawls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crawls/count")
    public ResponseEntity<Long> countCrawls(CrawlCriteria criteria) {
        log.debug("REST request to count Crawls by criteria: {}", criteria);
        return ResponseEntity.ok().body(crawlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crawls/:id} : get the "id" crawl.
     *
     * @param id the id of the crawl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crawl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crawls/{id}")
    public ResponseEntity<Crawl> getCrawl(@PathVariable Long id) {
        log.debug("REST request to get Crawl : {}", id);
        Optional<Crawl> crawl = crawlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crawl);
    }

    /**
     * {@code DELETE  /crawls/:id} : delete the "id" crawl.
     *
     * @param id the id of the crawl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crawls/{id}")
    public ResponseEntity<Void> deleteCrawl(@PathVariable Long id) {
        log.debug("REST request to delete Crawl : {}", id);
        crawlService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
