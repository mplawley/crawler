import { element, by, ElementFinder } from 'protractor';

export class CrawlComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crawl div table .btn-danger'));
  title = element.all(by.css('jhi-crawl div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class CrawlUpdatePage {
  pageTitle = element(by.id('jhi-crawl-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  timeInput = element(by.id('field_time'));
  urlInput = element(by.id('field_url'));
  resultInput = element(by.id('field_result'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setTimeInput(time: string): Promise<void> {
    await this.timeInput.sendKeys(time);
  }

  async getTimeInput(): Promise<string> {
    return await this.timeInput.getAttribute('value');
  }

  async setUrlInput(url: string): Promise<void> {
    await this.urlInput.sendKeys(url);
  }

  async getUrlInput(): Promise<string> {
    return await this.urlInput.getAttribute('value');
  }

  async setResultInput(result: string): Promise<void> {
    await this.resultInput.sendKeys(result);
  }

  async getResultInput(): Promise<string> {
    return await this.resultInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class CrawlDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crawl-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crawl'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
