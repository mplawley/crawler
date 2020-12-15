import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CrawlComponentsPage, CrawlDeleteDialog, CrawlUpdatePage } from './crawl.page-object';

const expect = chai.expect;

describe('Crawl e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crawlComponentsPage: CrawlComponentsPage;
  let crawlUpdatePage: CrawlUpdatePage;
  let crawlDeleteDialog: CrawlDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Crawls', async () => {
    await navBarPage.clickEntityDirectly('crawl');
    crawlComponentsPage = new CrawlComponentsPage();
    await browser.wait(ec.visibilityOf(crawlComponentsPage.title), 5000);
    expect(await crawlComponentsPage.getTitle()).to.eq('Crawls');
    await browser.wait(ec.or(ec.visibilityOf(crawlComponentsPage.entities), ec.visibilityOf(crawlComponentsPage.noResult)), 1000);
  });

  it('should load create Crawl page', async () => {
    await crawlComponentsPage.clickOnCreateButton();
    crawlUpdatePage = new CrawlUpdatePage();
    expect(await crawlUpdatePage.getPageTitle()).to.eq('Create or edit a Crawl');
    await crawlUpdatePage.cancel();
  });

  it('should create and save Crawls', async () => {
    const nbButtonsBeforeCreate = await crawlComponentsPage.countDeleteButtons();

    await crawlComponentsPage.clickOnCreateButton();

    await promise.all([
      crawlUpdatePage.setTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      crawlUpdatePage.setUrlInput('url'),
      crawlUpdatePage.setResultInput('result'),
    ]);

    expect(await crawlUpdatePage.getTimeInput()).to.contain('2001-01-01T02:30', 'Expected time value to be equals to 2000-12-31');
    expect(await crawlUpdatePage.getUrlInput()).to.eq('url', 'Expected Url value to be equals to url');
    expect(await crawlUpdatePage.getResultInput()).to.eq('result', 'Expected Result value to be equals to result');

    await crawlUpdatePage.save();
    expect(await crawlUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crawlComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Crawl', async () => {
    const nbButtonsBeforeDelete = await crawlComponentsPage.countDeleteButtons();
    await crawlComponentsPage.clickOnLastDeleteButton();

    crawlDeleteDialog = new CrawlDeleteDialog();
    expect(await crawlDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crawl?');
    await crawlDeleteDialog.clickOnConfirmButton();

    expect(await crawlComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
