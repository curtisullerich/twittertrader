/**
 * Module dependencies.
 */

var nconf = require('nconf')
  , mongo = require('mongoskin');

nconf.argv().env();
nconf.file({ file: 'config.json' });
nconf.defaults({
        'port': '9000'
});

var db = mongo.db('localhost/'+nconf.get('database'));
var twits = db.collection(nconf.get('collection'));

twits.ensureIndex( {"classification":1, "random":1});
twits.ensureIndex( {"id_str":1} );
exports.tweets = twits; // db.collection(nconf.get('collection'));

