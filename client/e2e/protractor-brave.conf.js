const config = require('./protractor.conf').config;

config.capabilities = {
  browserName: 'chrome',
  chromeOptions: {
    args: ['--headless'],
    binary: 'C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe'
  }
};

exports.config = config;
