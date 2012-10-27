/**
 * Module dependencies.
 */

var nconf = require('nconf');

nconf.argv().env();
nconf.file({ file: 'config.json' });
nconf.defaults({
        'port': '9000'
});

exports.nconf = nconf;

