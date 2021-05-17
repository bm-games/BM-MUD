import {AppPage} from './app.po';
import {browser, by, element, promise, protractor} from "protractor";


/*
Author: Marius Armbruster
Use-Case: A User wants to Login Into the Game with his Credentials. The Login should be successful and he should be redirected
          to the Dashboard. After that he takes a short look over the Dashboard an then Logs Out.
Info: We also Check if Necessary things like Redirection Links are existend on the Login Page and the Login Form is empty.
 */
let name: string;

describe('Authentication Login Case', () => {
  let page: AppPage;
  beforeEach(() => {
    page = new AppPage();
  });

  it('should display BM-MUD: Title', async () => {
    await page.navigateTo();
    await expect(page.getTitleText()).toContain('BM-MUD: Authentication');
  });
  it('should display Login Header, an Empty Form and a Button which states Log In', async () => {
    await expect(element(by.xpath("//h3[.='Login']")).getText()).toEqual('Login');
    await expect(element(by.name("email")).getText()).toEqual("");
    await expect(element(by.name("password")).getText()).toEqual("");
    await expect(element(by.css(".btn")).getText()).toEqual("Log In");
  });
  it('should check if redirect Links to Register and Forgot Password are shown', async () => {
    await expect(element(by.xpath("//a[.='No Account? Register NOW!']")).getAttribute('href')).toEqual('http://play.bm-games.net/auth/register');
    await expect(element(by.xpath("//a[.='Forgot your Password?']")).getAttribute('href')).toEqual('http://play.bm-games.net/auth/reset-password');
  });
  it('should Login the User with his Credentials and redirects him to the Dashboard', async () => {
    await element(by.name('email')).sendKeys('support@bm-games.net');
    await element(by.name('password')).sendKeys('zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38');
    await element(by.buttonText('Log In')).click();
    await expect(page.getTitleText()).toContain('BM-MUD: Dashboard');
  });
  it('should display the Dashboard Title and the personalized Greeting', async () => {
    await expect(page.getTitleText()).toContain('BM-MUD: Dashboard');
    await expect(element(by.css(".mat-toolbar > span:nth-of-type(1)")).getText()).toEqual('Hallo support@bm-games.net');
  });
  it('should display the search Form, MUD-Create Button and Logout Button', async () => {
    await expect(element(by.css("[placeholder='Suche nach verfügbaren Spielen']")).getText()).toEqual('');
    await expect(element(by.xpath("//button[.='MUD Erstellen']")).getText()).toEqual('MUD Erstellen');
    await expect(element(by.xpath("//span[contains(.,'Logout logout')]")).getText()).toEqual('Logout logout');
  });
  it('should Logout the User and display the Login Page again', async () => {
    await element(by.xpath("//button[contains(.,'Logout')]")).click();
    await expect(page.getTitleText()).toContain('BM-MUD: Authentication');
    await expect(element(by.xpath("//h3[.='Login']")).getText()).toEqual('Login');
  });
});

/*
Author: Marius Armbruster
Use-Case: A User wants to create a MUD with all needed attributes. After the creation the MUD should be shown in the Dashboard successfully.
Info: The first part is just the Login Case because we need to get past the Authentication
 */
describe('MUD-Creation Use Case', () => {
  let page: AppPage;
  let width = 1920;
  let height = 1080;
  browser.driver.manage().window().setSize(width, height);

  beforeEach(() => {
    page = new AppPage();
  });

  it('should Login the User and redirect him to the Dashboard', async () => {
    await page.navigateTo();
    await element(by.name('email')).sendKeys('support@bm-games.net');
    await element(by.name('password')).sendKeys('zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38');
    await element(by.buttonText('Log In')).click();
  });
  it('should show the Dashboard successfully', async () => {
    await expect(page.getTitleText()).toContain('BM-MUD: Dashboard');
    await expect(element(by.css("[placeholder='Suche nach verfügbaren Spielen']")).getText()).toEqual('');
    await expect(element(by.xpath("//button[.='MUD Erstellen']")).getText()).toEqual('MUD Erstellen');
    await expect(element(by.xpath("//span[contains(.,'Logout logout')]")).getText()).toEqual('Logout logout');
  });
  it('should click the MUD Create Button and redirect to the right page', async () => {
    await element(by.xpath("//span[.='MUD Erstellen']")).click();
    await expect(page.getTitleText()).toContain('BM-MUD: Configurator');
  });
  it('should Insert the MUD-Title and then click on Races', async () => {
    name = "E2E-StarWars ___"+ await page.getRandomString();
    await element(by.css("[placeholder='MUD Name']")).sendKeys(name);
    await element(by.xpath("//a[.='Rassen']")).click();
  });
  it('should show the Races', async () => {
    await expect(element(by.css(".labelHeader")).getText()).toEqual('Rassen')
  });
  it('should create some Races', async () => {
    //Tusken-Räuber
    await element(by.css("[placeholder='Name']")).sendKeys('Tusken-Räuber');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
   //Twi’lek
    await element(by.css("[placeholder='Name']")).sendKeys('Twi’lek');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Quarren
    await element(by.css("[placeholder='Name']")).sendKeys('Quarren');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Mensch
    await element(by.css("[placeholder='Name']")).sendKeys('Mensch');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Mon Calamari
    await element(by.css("[placeholder='Name']")).sendKeys('Mon Calamari');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Kaminoaner
    await element(by.css("[placeholder='Name']")).sendKeys('Kaminoaner');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Hutt
    await element(by.css("[placeholder='Name']")).sendKeys('Hutt');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Droide
    await element(by.css("[placeholder='Name']")).sendKeys('Droide');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
  });
  it("should switch to Class Tab and create Classes", async () => {
    await element(by.xpath("//a[.='Klassen']")).click();
    //Kopfgeldjäger
    await element(by.css("[placeholder='Name']")).sendKeys('Kopfgeldjäger');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[3]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Jedi
    await element(by.css("[placeholder='Name']")).sendKeys('Jedi');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Sith
    await element(by.css("[placeholder='Name']")).sendKeys('Sith');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Sturmtruppler
    await element(by.css("[placeholder='Name']")).sendKeys('Sturmtruppler');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Angriffsdroide
    await element(by.css("[placeholder='Name']")).sendKeys('Angriffsdroide');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Anführer
    await element(by.css("[placeholder='Name']")).sendKeys('Anführer');
    await element(by.xpath("//div[@class='inputAreaBody']/div[1]//div[@class='mat-slider-ticks']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[2]//div[@class='mat-slider-ticks']")).click();
    await element(by.css("textarea")).sendKeys('Möge die Macht mit dir sein.')
    await element(by.xpath("//button[.='Hinzufügen']")).click();
  });
  it("should switch to Items Tab an create some Items", async () => {
    await element(by.xpath("//a[.='Items']")).click();
    //Frosch
    await element(by.css("[placeholder='Name']")).sendKeys('Frosch');
    await element(by.css(".mat-select-placeholder")).click();
    await element(by.xpath("//span[contains(.,'Gesundheit abziehen')]")).click();
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Apfel
    await element(by.css("[placeholder='Name']")).sendKeys('Apfel');
    await element(by.css(".inputAreaBody > div:nth-of-type(2) .mat-form-field-wrapper")).click();
    await element(by.xpath("//span[contains(.,'Heilen')]")).click();
    await element(by.xpath("//button[.='Hinzufügen']")).click();
  });
  it("should switch to NPCs Tab and create some NPCs", async () => {
    await element(by.xpath("//a[.='NPCs']")).click();
    //Count Doku
    await element(by.css("[placeholder='Name']")).sendKeys('Count Doku');
    await element(by.xpath("//mat-list-option[1]/div[@class='mat-list-item-content mat-list-item-content-reverse']")).click();
    await element(by.xpath("//mat-list-option[2]/div[@class='mat-list-item-content mat-list-item-content-reverse']")).click();
    await element(by.css("textarea")).sendKeys("Ich mag dich ned!")
    await element(by.xpath("//button[.='Hinzufügen']")).click();
    //Obi Wan
    await element(by.css("[placeholder='Name']")).sendKeys('Obi Wan');
    await element(by.xpath("//mat-list-option[1]/div[@class='mat-list-item-content mat-list-item-content-reverse']")).click();
    await element(by.xpath("//mat-list-option[2]/div[@class='mat-list-item-content mat-list-item-content-reverse']")).click();
    await element(by.css("textarea")).sendKeys("Hello There")
    await element(by.xpath("//button[.='Hinzufügen']")).click();
  });
  it("should switch to NPCs Tab and create some NPCs", async () => {
    await element(by.xpath("//a[.='Räume']")).click();
    //Geonosis
    await element(by.css("[placeholder='Name']")).sendKeys('Geonosis');
    await element(by.css("textarea")).sendKeys("Staubig hier!")
    await element(by.xpath("//div[@class='inputAreaBody']//mat-list-option[@class='mat-list-item mat-list-option mat-focus-indicator mat-accent ng-star-inserted']/div[contains(.,'Obi Wan')]")).click();
    await element(by.xpath("//div[@class='inputAreaBody']//mat-list-option[@class='mat-list-item mat-list-option mat-focus-indicator mat-accent ng-star-inserted']/div[contains(.,'Count Doku')]")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[3]//mat-list-option[1]/div[@class='mat-list-item-content mat-list-item-content-reverse']")).click();
    await element(by.xpath("//div[@class='inputAreaBody']/div[3]//mat-list-option[2]/div[@class='mat-list-item-content mat-list-item-content-reverse']")).click();
    await element(by.xpath("//button[.='Speichern']")).click();
    await element(by.css(".mat-form-field-infix")).click();
    await element(by.css(".mat-option-text")).click();
  });
  it("should switch to Startequipment and select one", async () => {
    await element(by.xpath("//a[.='Startequipment']")).click();
    await element(by.xpath("//mat-list-option[1]/div[@class='mat-list-item-content mat-list-item-content-reverse']")).click();
  });
  it("should save the created Game", async () => {
    await element(by.xpath("//button[.='Endgültig speichern']")).click();
    await browser.sleep(100);
  });
  it("should look if the Config was successfully saved", async () => {
    await expect(element(by.xpath("//mat-card-title[contains(.,'"+name+" info')]")).getText()).toEqual(name+"\ninfo");

  });
  it('should Logout the User and display the Login Page again', async () => {
    await element(by.xpath("//button[contains(.,'Logout')]")).click();
    await expect(page.getTitleText()).toContain('BM-MUD: Authentication');
    await expect(element(by.xpath("//h3[.='Login']")).getText()).toEqual('Login');
  });

});
/*
Author: Marius Armbruster
Use-Case: A User wants to Login Into the Game with his Credentials. The Login should be successful and he should be redirected
          to the Dashboard. After that he takes a short look over the Dashboard, joins one of the Games he created.
Info: The Use Case is for a normal Player not Master. He could also connect as a Master but instead he creates a Avatar and joins with that.
 */
describe('Player Join MUD Case', () => {
  let page: AppPage;
  let sname = name;
  beforeEach(() => {
    page = new AppPage();
  });
  let width = 1920;
  let height = 1080;
  browser.driver.manage().window().setSize(width, height);

  beforeEach(() => {
    page = new AppPage();
  });

  it('should Login the User and redirect him to the Dashboard', async () => {
    await page.navigateTo();
    browser.sleep(50000);
    await element(by.name('email')).sendKeys('support@bm-games.net');
    await element(by.name('password')).sendKeys('zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38zMsD!A@jHXr&PbcSmgF8LFqL6fttx74Rp4EKrY38');
    await element(by.buttonText('Log In')).click();
  });

  it('should display the Dashboard', async () => {
    await expect(page.getTitleText()).toContain('BM-MUD: Dashboard');
  });
  it('should search his created game', async () => {
    await element(by.css("[placeholder='Suche nach verfügbaren Spielen']")).click;
    await element(by.id("mat-input-0")).sendKeys(name+"")
  });
  it('should join the searched game', async () => {
    await element(by.xpath("//span[contains(.,'Beitreten')]")).click();
  });
  it('should show the Avatar Page from the Game', async () => {
    await expect(element(by.xpath("//span[.='Willkommen in "+name+"']")).getText()).toEqual("Willkommen in "+name);
  });
  it('should create a Avatar', async () => {
    await element(by.xpath("//button[@class='mat-focus-indicator mat-raised-button mat-button-base mat-primary']")).click();
    await browser.sleep(400);
    await browser.actions().sendKeys("IlikeTrees").perform();
    await browser.sleep(400);
    await browser.actions().sendKeys(protractor.Key.TAB).sendKeys(protractor.Key.ENTER).sendKeys(protractor.Key.ENTER).perform();
    await browser.sleep(400);
    await browser.actions().sendKeys(protractor.Key.TAB).sendKeys(protractor.Key.ENTER).sendKeys(protractor.Key.ENTER).perform();
    await browser.sleep(400);
    await element(by.xpath("//span[.='Avatar erstellen']")).click();
    await browser.sleep(200);
  });
  it('should join with the created Avatar + check', async () => {
    await element(by.xpath("//mat-card-actions[@class='mat-card-actions']/button[@class='mat-focus-indicator mat-raised-button mat-button-base']")).click();
    await expect(element(by.xpath("//span[.='"+name+"']")).getText()).toEqual(name);
  });
  it('should click into the Console and type look', async () => {
    await element(by.css("textarea")).click();
    await browser.actions().sendKeys('look').sendKeys(protractor.Key.ENTER).perform();
    await browser.sleep(500)
    await expect(element(by.xpath("//span[contains(.,'Du bist in Geonosis: \"Staubig hier!\"')]")).getText()).toEqual('Du bist in Geonosis: "Staubig hier!"');
  });
});

