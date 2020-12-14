package com.lawley.repository;

import com.lawley.domain.Crawl;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Crawl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CrawlRepository extends JpaRepository<Crawl, Long>, JpaSpecificationExecutor<Crawl> {
}
