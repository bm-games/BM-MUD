import {browser} from 'protractor';

export class AppPage {

  async navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl);
  }

  async getTitleText(): Promise<string> {
    return browser.getTitle();
  }
  async getRandomString(): Promise<string> {
    let string = '';
    let length = 8;
    let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789' //Include numbers if you want
    for (let i = 0; i < length; i++) {
      string += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return string;
  }
}
