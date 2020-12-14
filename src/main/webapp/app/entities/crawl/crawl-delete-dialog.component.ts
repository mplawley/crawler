import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICrawl } from 'app/shared/model/crawl.model';
import { CrawlService } from './crawl.service';

@Component({
  templateUrl: './crawl-delete-dialog.component.html',
})
export class CrawlDeleteDialogComponent {
  crawl?: ICrawl;

  constructor(protected crawlService: CrawlService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crawlService.delete(id).subscribe(() => {
      this.eventManager.broadcast('crawlListModification');
      this.activeModal.close();
    });
  }
}
