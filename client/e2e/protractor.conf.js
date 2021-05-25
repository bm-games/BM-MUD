// @ts-check
// Protractor configuration file, see link for more information
// https://github.com/angular/protractor/blob/master/lib/config.ts

const {SpecReporter, StacktraceOption} = require('jasmine-spec-reporter');
const HtmlReporter = require('protractor-beautiful-reporter');
/**
 * @type { import("protractor").Config }
 */
exports.config = {
  allScriptsTimeout: 11000,
  specs: [
    './src/**/*.e2e-spec.ts'
  ],
  capabilities: {
    browserName: 'chrome',
    chromeOptions: {
      // args: ['--headless'],
      // binary: 'C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe'
    }
  },
  directConnect: true,
  SELENIUM_PROMISE_MANAGER: false,
  // baseUrl: 'http://localhost:4200/ ',
  baseUrl: 'http://play.bm-games.net/ ',
  framework: 'jasmine',
  jasmineNodeOpts: {
    showColors: true,
    defaultTimeoutInterval: 30000,
    print: function () {
    }
  },
  onPrepare() {
    require('ts-node').register({
      project: require('path').join(__dirname, './tsconfig.json')
    });
    jasmine.getEnv().addReporter(new SpecReporter({
      spec: {
        displayStacktrace: StacktraceOption.PRETTY
      }
    }));
    jasmine.getEnv().addReporter(new HtmlReporter({
      baseDirectory: 'reports/screenshots'
    }).getJasmine2Reporter());
  }
};
