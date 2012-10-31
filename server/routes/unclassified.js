var tweetsdb = require('../store').tweets;
var writeJSON = require('../io').writeJSON;
var nconf = require('../config').nconf;
var writeCSV = require('../io').writeCSV;

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
 * GET random listing.
 */

exports.random = function(req, res){
  res.writeHead(200, {'Content-Type': 'application/json'});
  res.write('[');
  var tweets = tweetsdb.find({classification:{$exists:false}, random:{$gte:Math.random()}}).limit(parseInt(req.params.count));

  tweets.toArray(function(err, tweetArr) {
    var i=0;
    for(; i<tweetArr.length-1; i++) {
      res.write(JSON.stringify(tweetArr[i]));
      res.write(',');
    }
    res.write(JSON.stringify(tweetArr[i]));
    res.end(']');
  });
};

/*
 * GET random listing.
 */

exports.randomcsv = function(req, res) {
  var tweets = tweetsdb.find({classification:{$exists:false}, random:{$gte:Math.random()}}).limit(parseInt(req.params.count));
  writeCSV(res, tweets);
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
    tweetsdb.find({company:company, classification:{$exists:false}}).count(function(err,count) {
      companyCounts[company] = count;
      done += 1;

      if(companies.length == done)
        res.send(companyCounts);
    });
  });
};

/*
 * GET company/*
 */

exports.company = {
  id: function(req, res) {
    var tweets = tweetsdb.find({classification:{$exists:false}, company:req.params.id.toLowerCase()});
    writeJSON(res, tweets);
  },
/*
 * GET company/:id/:count
 */
  idcount: function(req, res) {
    var tweets = tweetsdb.find({classification:{$exists:false}, company:req.params.id.toLowerCase()}).limit(parseInt(req.params.count));
    writeJSON(res, tweets);
  }
}


/*
 * GET sentiment/:kind
 */
exports.sentiment = {
  kind: function(req, res) {
    res.send("response");
  }
}


