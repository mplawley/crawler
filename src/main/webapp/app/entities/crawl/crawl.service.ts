import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICrawl } from 'app/shared/model/crawl.model';

type EntityResponseType = HttpResponse<ICrawl>;
type EntityArrayResponseType = HttpResponse<ICrawl[]>;

@Injectable({ providedIn: 'root' })
export class CrawlService {
  public resourceUrl = SERVER_API_URL + 'api/crawls';

  constructor(protected http: HttpClient) {}

  create(crawl: ICrawl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(crawl);
    return this.http
      .post<ICrawl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(crawl: ICrawl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(crawl);
    return this.http
      .put<ICrawl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICrawl>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICrawl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(crawl: ICrawl): ICrawl {
    const copy: ICrawl = Object.assign({}, crawl, {
      time: crawl.time && crawl.time.isValid() ? crawl.time.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.time = res.body.time ? moment(res.body.time) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((crawl: ICrawl) => {
        crawl.time = crawl.time ? moment(crawl.time) : undefined;
      });
    }
    return res;
  }
}
