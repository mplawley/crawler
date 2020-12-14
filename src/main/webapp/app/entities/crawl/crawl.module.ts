import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CrawlerSharedModule } from 'app/shared/shared.module';
import { CrawlComponent } from './crawl.component';
import { CrawlDetailComponent } from './crawl-detail.component';
import { CrawlUpdateComponent } from './crawl-update.component';
import { CrawlDeleteDialogComponent } from './crawl-delete-dialog.component';
import { crawlRoute } from './crawl.route';

@NgModule({
  imports: [CrawlerSharedModule, RouterModule.forChild(crawlRoute)],
  declarations: [CrawlComponent, CrawlDetailComponent, CrawlUpdateComponent, CrawlDeleteDialogComponent],
  entryComponents: [CrawlDeleteDialogComponent],
})
export class CrawlerCrawlModule {}
