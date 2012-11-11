var tweetsdb = require('../store').tweets;
var writeJSON = require('../io').writeJSON;
var nconf = require('../config').nconf;
var writeCSV = require('../io').writeCSV;


/*
 * POST classified listing.
 */

exports.post = function(req, res){
  res.send("respond with a resource");
};


/*
 * GET companies listing.
 */

exports.companies = function(req, res) {
  var companyCounts = {};
  var done = 0;

  var companies = nconf.get('companies');
  companies.forEach(function(company) {
    company = company.toLowerCase();
    tweetsdb.find({company:company, classification:"classified"}).count(function(err,count) {
      companyCounts[company] = count;
      done += 1;

      if(companies.length == done)
        res.send(companyCounts);
    });
  });
};

exports.sentiments = function(req, res) {
  var sentimentCounts = {};
  var done = 0;

  var companies = nconf.get('sentiments');
  companies.forEach(function(sentiment) {
    tweetsdb.find({sentiment:sentiment, classification:"classified"}).count(function(err,count) {
      sentimentCounts[sentiment] = count;
      done += 1;

      if(companies.length == done)
        res.send(sentimentCounts);
    });
  });
};

