package com.lawley.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.lawley.domain.Crawl} entity. This class is used
 * in {@link com.lawley.web.rest.CrawlResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crawls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrawlCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter time;

    private StringFilter url;

    private StringFilter result;

    public CrawlCriteria() {
    }

    public CrawlCriteria(CrawlCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.result = other.result == null ? null : other.result.copy();
    }

    @Override
    public CrawlCriteria copy() {
        return new CrawlCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getTime() {
        return time;
    }

    public void setTime(InstantFilter time) {
        this.time = time;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getResult() {
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CrawlCriteria that = (CrawlCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(time, that.time) &&
            Objects.equals(url, that.url) &&
            Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        time,
        url,
        result
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrawlCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (result != null ? "result=" + result + ", " : "") +
            "}";
    }

}
