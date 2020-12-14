import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CrawlerTestModule } from '../../../test.module';
import { CrawlUpdateComponent } from 'app/entities/crawl/crawl-update.component';
import { CrawlService } from 'app/entities/crawl/crawl.service';
import { Crawl } from 'app/shared/model/crawl.model';

describe('Component Tests', () => {
  describe('Crawl Management Update Component', () => {
    let comp: CrawlUpdateComponent;
    let fixture: ComponentFixture<CrawlUpdateComponent>;
    let service: CrawlService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CrawlerTestModule],
        declarations: [CrawlUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CrawlUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CrawlUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CrawlService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Crawl(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Crawl();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
