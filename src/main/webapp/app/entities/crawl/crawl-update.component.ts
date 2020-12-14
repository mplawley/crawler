import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICrawl, Crawl } from 'app/shared/model/crawl.model';
import { CrawlService } from './crawl.service';

@Component({
  selector: 'jhi-crawl-update',
  templateUrl: './crawl-update.component.html',
})
export class CrawlUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    time: [null, [Validators.required]],
    url: [null, [Validators.required]],
    result: [],
  });

  constructor(protected crawlService: CrawlService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crawl }) => {
      if (!crawl.id) {
        const today = moment();
        crawl.time = today;
      }

      this.updateForm(crawl);
    });
  }

  updateForm(crawl: ICrawl): void {
    this.editForm.patchValue({
      id: crawl.id,
      time: crawl.time ? crawl.time.format(DATE_TIME_FORMAT) : null,
      url: crawl.url,
      result: crawl.result,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crawl = this.createFromForm();
    if (crawl.id !== undefined) {
      this.subscribeToSaveResponse(this.crawlService.update(crawl));
    } else {
      this.subscribeToSaveResponse(this.crawlService.create(crawl));
    }
  }

  private createFromForm(): ICrawl {
    return {
      ...new Crawl(),
      id: this.editForm.get(['id'])!.value,
      time: this.editForm.get(['time'])!.value ? moment(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
      url: this.editForm.get(['url'])!.value,
      result: this.editForm.get(['result'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrawl>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
