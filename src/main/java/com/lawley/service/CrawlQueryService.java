package com.lawley.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.lawley.domain.Crawl;
import com.lawley.domain.*; // for static metamodels
import com.lawley.repository.CrawlRepository;
import com.lawley.service.dto.CrawlCriteria;

/**
 * Service for executing complex queries for {@link Crawl} entities in the database.
 * The main input is a {@link CrawlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Crawl} or a {@link Page} of {@link Crawl} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrawlQueryService extends QueryService<Crawl> {

    private final Logger log = LoggerFactory.getLogger(CrawlQueryService.class);

    private final CrawlRepository crawlRepository;

    public CrawlQueryService(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }

    /**
     * Return a {@link List} of {@link Crawl} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Crawl> findByCriteria(CrawlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Crawl> specification = createSpecification(criteria);
        return crawlRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Crawl} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Crawl> findByCriteria(CrawlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Crawl> specification = createSpecification(criteria);
        return crawlRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrawlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Crawl> specification = createSpecification(criteria);
        return crawlRepository.count(specification);
    }

    /**
     * Function to convert {@link CrawlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Crawl> createSpecification(CrawlCriteria criteria) {
        Specification<Crawl> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Crawl_.id));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), Crawl_.time));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Crawl_.url));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResult(), Crawl_.result));
            }
        }
        return specification;
    }
}
