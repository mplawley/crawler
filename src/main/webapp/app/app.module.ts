import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CrawlerSharedModule } from 'app/shared/shared.module';
import { CrawlerCoreModule } from 'app/core/core.module';
import { CrawlerAppRoutingModule } from './app-routing.module';
import { CrawlerHomeModule } from './home/home.module';
import { CrawlerEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    CrawlerSharedModule,
    CrawlerCoreModule,
    CrawlerHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CrawlerEntityModule,
    CrawlerAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class CrawlerAppModule {}
