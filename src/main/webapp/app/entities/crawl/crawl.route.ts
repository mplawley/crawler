import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICrawl, Crawl } from 'app/shared/model/crawl.model';
import { CrawlService } from './crawl.service';
import { CrawlComponent } from './crawl.component';
import { CrawlDetailComponent } from './crawl-detail.component';
import { CrawlUpdateComponent } from './crawl-update.component';

@Injectable({ providedIn: 'root' })
export class CrawlResolve implements Resolve<ICrawl> {
  constructor(private service: CrawlService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrawl> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((crawl: HttpResponse<Crawl>) => {
          if (crawl.body) {
            return of(crawl.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Crawl());
  }
}

export const crawlRoute: Routes = [
  {
    path: '',
    component: CrawlComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Crawls',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrawlDetailComponent,
    resolve: {
      crawl: CrawlResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Crawls',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrawlUpdateComponent,
    resolve: {
      crawl: CrawlResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Crawls',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrawlUpdateComponent,
    resolve: {
      crawl: CrawlResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Crawls',
    },
    canActivate: [UserRouteAccessService],
  },
];
