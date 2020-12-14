package com.lawley.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.lawley.web.rest.TestUtil;

public class CrawlTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crawl.class);
        Crawl crawl1 = new Crawl();
        crawl1.setId(1L);
        Crawl crawl2 = new Crawl();
        crawl2.setId(crawl1.getId());
        assertThat(crawl1).isEqualTo(crawl2);
        crawl2.setId(2L);
        assertThat(crawl1).isNotEqualTo(crawl2);
        crawl1.setId(null);
        assertThat(crawl1).isNotEqualTo(crawl2);
    }
}
