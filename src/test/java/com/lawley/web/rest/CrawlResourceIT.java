package com.lawley.web.rest;

import com.lawley.CrawlerApp;
import com.lawley.domain.Crawl;
import com.lawley.repository.CrawlRepository;
import com.lawley.service.CrawlService;
import com.lawley.service.dto.CrawlCriteria;
import com.lawley.service.CrawlQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CrawlResource} REST controller.
 */
@SpringBootTest(classes = CrawlerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CrawlResourceIT {

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    @Autowired
    private CrawlRepository crawlRepository;

    @Autowired
    private CrawlService crawlService;

    @Autowired
    private CrawlQueryService crawlQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrawlMockMvc;

    private Crawl crawl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crawl createEntity(EntityManager em) {
        Crawl crawl = new Crawl()
            .time(DEFAULT_TIME)
            .url(DEFAULT_URL)
            .result(DEFAULT_RESULT);
        return crawl;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crawl createUpdatedEntity(EntityManager em) {
        Crawl crawl = new Crawl()
            .time(UPDATED_TIME)
            .url(UPDATED_URL)
            .result(UPDATED_RESULT);
        return crawl;
    }

    @BeforeEach
    public void initTest() {
        crawl = createEntity(em);
    }

    @Test
    @Transactional
    public void createCrawl() throws Exception {
        int databaseSizeBeforeCreate = crawlRepository.findAll().size();
        // Create the Crawl
        restCrawlMockMvc.perform(post("/api/crawls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isCreated());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeCreate + 1);
        Crawl testCrawl = crawlList.get(crawlList.size() - 1);
        assertThat(testCrawl.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testCrawl.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testCrawl.getResult()).isEqualTo(DEFAULT_RESULT);
    }

    @Test
    @Transactional
    public void createCrawlWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = crawlRepository.findAll().size();

        // Create the Crawl with an existing ID
        crawl.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrawlMockMvc.perform(post("/api/crawls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isBadRequest());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crawlRepository.findAll().size();
        // set the field null
        crawl.setTime(null);

        // Create the Crawl, which fails.


        restCrawlMockMvc.perform(post("/api/crawls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isBadRequest());

        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = crawlRepository.findAll().size();
        // set the field null
        crawl.setUrl(null);

        // Create the Crawl, which fails.


        restCrawlMockMvc.perform(post("/api/crawls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isBadRequest());

        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCrawls() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList
        restCrawlMockMvc.perform(get("/api/crawls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawl.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)));
    }
    
    @Test
    @Transactional
    public void getCrawl() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get the crawl
        restCrawlMockMvc.perform(get("/api/crawls/{id}", crawl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crawl.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT));
    }


    @Test
    @Transactional
    public void getCrawlsByIdFiltering() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        Long id = crawl.getId();

        defaultCrawlShouldBeFound("id.equals=" + id);
        defaultCrawlShouldNotBeFound("id.notEquals=" + id);

        defaultCrawlShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrawlShouldNotBeFound("id.greaterThan=" + id);

        defaultCrawlShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrawlShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCrawlsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where time equals to DEFAULT_TIME
        defaultCrawlShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the crawlList where time equals to UPDATED_TIME
        defaultCrawlShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllCrawlsByTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where time not equals to DEFAULT_TIME
        defaultCrawlShouldNotBeFound("time.notEquals=" + DEFAULT_TIME);

        // Get all the crawlList where time not equals to UPDATED_TIME
        defaultCrawlShouldBeFound("time.notEquals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllCrawlsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where time in DEFAULT_TIME or UPDATED_TIME
        defaultCrawlShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the crawlList where time equals to UPDATED_TIME
        defaultCrawlShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllCrawlsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where time is not null
        defaultCrawlShouldBeFound("time.specified=true");

        // Get all the crawlList where time is null
        defaultCrawlShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllCrawlsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where url equals to DEFAULT_URL
        defaultCrawlShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the crawlList where url equals to UPDATED_URL
        defaultCrawlShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCrawlsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where url not equals to DEFAULT_URL
        defaultCrawlShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the crawlList where url not equals to UPDATED_URL
        defaultCrawlShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCrawlsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where url in DEFAULT_URL or UPDATED_URL
        defaultCrawlShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the crawlList where url equals to UPDATED_URL
        defaultCrawlShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCrawlsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where url is not null
        defaultCrawlShouldBeFound("url.specified=true");

        // Get all the crawlList where url is null
        defaultCrawlShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllCrawlsByUrlContainsSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where url contains DEFAULT_URL
        defaultCrawlShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the crawlList where url contains UPDATED_URL
        defaultCrawlShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllCrawlsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where url does not contain DEFAULT_URL
        defaultCrawlShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the crawlList where url does not contain UPDATED_URL
        defaultCrawlShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllCrawlsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where result equals to DEFAULT_RESULT
        defaultCrawlShouldBeFound("result.equals=" + DEFAULT_RESULT);

        // Get all the crawlList where result equals to UPDATED_RESULT
        defaultCrawlShouldNotBeFound("result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllCrawlsByResultIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where result not equals to DEFAULT_RESULT
        defaultCrawlShouldNotBeFound("result.notEquals=" + DEFAULT_RESULT);

        // Get all the crawlList where result not equals to UPDATED_RESULT
        defaultCrawlShouldBeFound("result.notEquals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllCrawlsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where result in DEFAULT_RESULT or UPDATED_RESULT
        defaultCrawlShouldBeFound("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT);

        // Get all the crawlList where result equals to UPDATED_RESULT
        defaultCrawlShouldNotBeFound("result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllCrawlsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where result is not null
        defaultCrawlShouldBeFound("result.specified=true");

        // Get all the crawlList where result is null
        defaultCrawlShouldNotBeFound("result.specified=false");
    }
                @Test
    @Transactional
    public void getAllCrawlsByResultContainsSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where result contains DEFAULT_RESULT
        defaultCrawlShouldBeFound("result.contains=" + DEFAULT_RESULT);

        // Get all the crawlList where result contains UPDATED_RESULT
        defaultCrawlShouldNotBeFound("result.contains=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllCrawlsByResultNotContainsSomething() throws Exception {
        // Initialize the database
        crawlRepository.saveAndFlush(crawl);

        // Get all the crawlList where result does not contain DEFAULT_RESULT
        defaultCrawlShouldNotBeFound("result.doesNotContain=" + DEFAULT_RESULT);

        // Get all the crawlList where result does not contain UPDATED_RESULT
        defaultCrawlShouldBeFound("result.doesNotContain=" + UPDATED_RESULT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrawlShouldBeFound(String filter) throws Exception {
        restCrawlMockMvc.perform(get("/api/crawls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawl.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)));

        // Check, that the count call also returns 1
        restCrawlMockMvc.perform(get("/api/crawls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrawlShouldNotBeFound(String filter) throws Exception {
        restCrawlMockMvc.perform(get("/api/crawls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrawlMockMvc.perform(get("/api/crawls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCrawl() throws Exception {
        // Get the crawl
        restCrawlMockMvc.perform(get("/api/crawls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCrawl() throws Exception {
        // Initialize the database
        crawlService.save(crawl);

        int databaseSizeBeforeUpdate = crawlRepository.findAll().size();

        // Update the crawl
        Crawl updatedCrawl = crawlRepository.findById(crawl.getId()).get();
        // Disconnect from session so that the updates on updatedCrawl are not directly saved in db
        em.detach(updatedCrawl);
        updatedCrawl
            .time(UPDATED_TIME)
            .url(UPDATED_URL)
            .result(UPDATED_RESULT);

        restCrawlMockMvc.perform(put("/api/crawls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCrawl)))
            .andExpect(status().isOk());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeUpdate);
        Crawl testCrawl = crawlList.get(crawlList.size() - 1);
        assertThat(testCrawl.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testCrawl.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testCrawl.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void updateNonExistingCrawl() throws Exception {
        int databaseSizeBeforeUpdate = crawlRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrawlMockMvc.perform(put("/api/crawls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isBadRequest());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCrawl() throws Exception {
        // Initialize the database
        crawlService.save(crawl);

        int databaseSizeBeforeDelete = crawlRepository.findAll().size();

        // Delete the crawl
        restCrawlMockMvc.perform(delete("/api/crawls/{id}", crawl.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
