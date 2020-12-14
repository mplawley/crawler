import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrawl } from 'app/shared/model/crawl.model';

@Component({
  selector: 'jhi-crawl-detail',
  templateUrl: './crawl-detail.component.html',
})
export class CrawlDetailComponent implements OnInit {
  crawl: ICrawl | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crawl }) => (this.crawl = crawl));
  }

  previousState(): void {
    window.history.back();
  }
}
