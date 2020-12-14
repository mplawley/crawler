import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrawlerTestModule } from '../../../test.module';
import { CrawlDetailComponent } from 'app/entities/crawl/crawl-detail.component';
import { Crawl } from 'app/shared/model/crawl.model';

describe('Component Tests', () => {
  describe('Crawl Management Detail Component', () => {
    let comp: CrawlDetailComponent;
    let fixture: ComponentFixture<CrawlDetailComponent>;
    const route = ({ data: of({ crawl: new Crawl(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CrawlerTestModule],
        declarations: [CrawlDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CrawlDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CrawlDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load crawl on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.crawl).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
