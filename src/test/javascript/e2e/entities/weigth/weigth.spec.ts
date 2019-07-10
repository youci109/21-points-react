/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import WeigthComponentsPage from './weigth.page-object';
import { WeigthDeleteDialog } from './weigth.page-object';
import WeigthUpdatePage from './weigth-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Weigth e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let weigthUpdatePage: WeigthUpdatePage;
  let weigthComponentsPage: WeigthComponentsPage;
  /*let weigthDeleteDialog: WeigthDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Weigths', async () => {
    await navBarPage.getEntityPage('weigth');
    weigthComponentsPage = new WeigthComponentsPage();
    expect(await weigthComponentsPage.getTitle().getText()).to.match(/Weigths/);
  });

  it('should load create Weigth page', async () => {
    await weigthComponentsPage.clickOnCreateButton();
    weigthUpdatePage = new WeigthUpdatePage();
    expect(await weigthUpdatePage.getPageTitle().getAttribute('id')).to.match(/twentyOnePointsReactApp.weigth.home.createOrEditLabel/);
    await weigthUpdatePage.cancel();
  });

  /* it('should create and save Weigths', async () => {
        async function createWeigth() {
            await weigthComponentsPage.clickOnCreateButton();
            await weigthUpdatePage.setTimestampInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
            expect(await weigthUpdatePage.getTimestampInput()).to.contain('2001-01-01T02:30');
            await weigthUpdatePage.setWeightInput('5');
            expect(await weigthUpdatePage.getWeightInput()).to.eq('5');
            await weigthUpdatePage.userSelectLastOption();
            await waitUntilDisplayed(weigthUpdatePage.getSaveButton());
            await weigthUpdatePage.save();
            await waitUntilHidden(weigthUpdatePage.getSaveButton());
            expect(await weigthUpdatePage.getSaveButton().isPresent()).to.be.false;
        }

        await createWeigth();
        await weigthComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeCreate = await weigthComponentsPage.countDeleteButtons();
        await createWeigth();

        await weigthComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
        expect(await weigthComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

  /* it('should delete last Weigth', async () => {
        await weigthComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeDelete = await weigthComponentsPage.countDeleteButtons();
        await weigthComponentsPage.clickOnLastDeleteButton();

        const deleteModal = element(by.className('modal'));
        await waitUntilDisplayed(deleteModal);

        weigthDeleteDialog = new WeigthDeleteDialog();
        expect(await weigthDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/twentyOnePointsReactApp.weigth.delete.question/);
        await weigthDeleteDialog.clickOnConfirmButton();

        await weigthComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
        expect(await weigthComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
