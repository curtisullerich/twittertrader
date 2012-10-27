
var tweetsdb = require('../store').tweets;
var writeJSON = require('../io').writeJSON;
var nconf = require('../config').nconf;

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
}


/*
 * GET verified/companies
 */

exports.companies = function(req, res) {
  
  var companyCounts = {};
  var done = 0;

  var companies = nconf.get('companies');
  companies.forEach(function(company) {
    company = company.toLowerCase();
    tweetsdb.find({company:company, classification:"verified"}).count(function(err,count) {
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

      if(company)
        tweet.company = company.toLowerCase();

      if(sentiment)
        tweet.sentiment = sentiment;

      tweet.classification = "verified";

      tweetsdb.save(tweet);

      res.send(JSON.stringify({success:"true"}));

    });
  }
};


