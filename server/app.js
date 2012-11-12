
/**
 * Module dependencies.
 */

var express = require('express')
  , pages = require('./routes/pages')
  , classified = require('./routes/classified')
  , verified = require('./routes/verified')
  , unclassified = require('./routes/unclassified')
  , http = require('http')
  , path = require('path')
  , nconf = require('nconf')
  , mongojs = require('mongojs');

nconf.argv().env();
nconf.file({ file: 'config.json' });
nconf.defaults({
	'port': '9000'
});

var db = mongojs.connect(nconf.get('database'), [nconf.get('collection')]);

var app = express();

app.configure(function(){
  app.set('port', nconf.get('port'));
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(require('stylus').middleware(__dirname + '/public'));
  app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function(){
  app.use(express.errorHandler());
});

app.get('/', pages.index);
app.get('/verify', pages.verify);

app.get('/classified/companies', classified.companies);
app.get('/classified/sentiments', classified.sentiments);
app.get('/classified/company/:company/confidence/:confidence/count/:count', classified.company_confidence);
app.get('/classified/random/company/confidence/:confidence/count/:count', classified.company_random);
app.post('/classified', classified.post);
app.post('/classified/unclassify', classified.post_unclassify);

app.get('/verified/sentiment.csv', verified.sentimentcsv);
app.get('/verified/sentiment', verified.sentiment.all);
app.get('/verified/sentiments', verified.sentiments);
app.get('/verified/companies.csv', verified.companycsv);
app.get('/verified/companies', verified.companies);
app.get('/verified/company/:id', verified.company.id);
app.get('/verified/sentiment/:kind', verified.sentiment.kind);
app.get('/verified/company/:id/since/:timestamp', verified.company.id_timestamp);
app.post('/verified', verified.post);

app.get('/unclassified/random/:count', unclassified.random);
app.get('/unclassified/random/:count/tweets.csv', unclassified.randomcsv);
app.get('/unclassified/companies', unclassified.companies);
app.get('/unclassified/company/:id/:count', unclassified.company.idcount);
app.get('/unclassified/sentiment/:kind', unclassified.sentiment.kind);

http.createServer(app).listen(app.get('port'), function(){
  console.log("Express server listening on port " + app.get('port'));
});
