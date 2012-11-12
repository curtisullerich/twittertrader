
var tweetsdb = require('../store').tweets;
var writeJSON = require('../io').writeJSON;
var nconf = require('../config').nconf;
var writeCSV = require('../io').writeCSV;



exports.sentiment = {
  kind: function(req, res) {
  },
  all : function(req, res) {
    var tweets = tweetsdb.find({sentimentClassification:{$gt:1}});
    writeJSON(res, tweets);
  }
};

exports.sentimentcsv = function(req, res) {
  var tweets = tweetsdb.find({sentimentClassification:{$gt:1}}).limit(1000);
  writeCSV(res, tweets, "sentiment");
};

exports.companycsv = function(req, res) {
  var tweets = tweetsdb.find({classification:"verified", company:{$exists:true}}).limit(10000);
  writeCSV(res, tweets, "company");
}

exports.sentiments = function(req, res) {

};


/*
 * GET verified/company
 */

exports.company = {
  id: function(req, res) {
    var tweets = tweetsdb.find({classification:"verified", company:req.params.id.toLowerCase()});
    writeJSON(res, tweets);
  },
  id_timestamp: function(req, res) {
    res.send("response");
  }
};


/*
 * GET verified/companies
 */

exports.companies = function(req, res) {
  
  var companyCounts = {};
  var done = 0;

  var companies = nconf.get('companies');
  companies.forEach(function(company) {
    company = company.toLowerCase();
    tweetsdb.find({company:company, companyConfidence:{$gte:2}}).count(function(err,count) {
      companyCounts[company] = count;
      done += 1;

      if(companies.length == done)
        res.send(companyCounts);
    });
  });
};


/*
 * POST verified
 */

exports.post = function(req, res){

  if(req.body.type == 'single') {
    var id = req.body.id_str;
    var sentiment = req.body['sentiment'];
    var company = req.body['company'];

    tweetsdb.findOne({'id_str' : id}, function(err, tweet) {

      if(company) {
        tweet.company = company.toLowerCase();
        tweet.companyConfidence = 2;
      }

      if(sentiment) {
        tweet.sentiment = sentiment;
        tweet.sentimentConfidence = 2;
      }

      tweet.classification = "verified";

      tweetsdb.save(tweet);

      res.send(JSON.stringify({success:"true"}));

    });
  }
};


