/**
 * Module dependencies.
 */

var nconf = require('nconf')
  , mongojs = require('mongojs');

nconf.argv().env();
nconf.file({ file: '../config.json' });
nconf.defaults({
        'port': '9000'
});

exports.db = mongojs.connect(nconf.get('database'), [nconf.get('collection')]);

