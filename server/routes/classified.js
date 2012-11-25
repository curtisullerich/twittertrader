var tweetsdb = require('../store').tweets;
var writeJSON = require('../io').writeJSON;
var nconf = require('../config').nconf;
var writeCSV = require('../io').writeCSV;
var JSONStream = require('JSONStream');

/*
 * POST classified listing.
 */

 exports.post = function(req, res) {
  var parser = JSONStream.parse([true]);
  parser.on('data', function(data) {
    var id_str = data.id_str;
    var company = data.company;
    var sentiment = data.sentiment;

    tweetsdb.findOne({'id_str':id_str}, function(err, tweet) {

      tweet.cstamp = data.cstamp;

      if(company) {
        tweet.company = company;
        tweet.companyConfidence = 2;
      }

      if(sentiment) {
        tweet.sentiment = sentiment;
        tweet.sentimentConfidence = 2;
      }

      tweet.classification = "classified";
    });
//    console.dir(data);
});
  parser.on('root', function(root, count) {
    res.end();
  });

  req.pipe(parser);
};




/*
 * POST unclassify
 */

 exports.post_unclassify = function(req, res){

  if(req.body.type == 'single') {
    var id = req.body.id_str;
    var field = req.body.field;

    tweetsdb.findOne({'id_str' : id}, function(err, tweet) {

      if(field == 'company' && tweet['company']) {
        delete tweet.company;
        delete tweet.companyConfidence;
      }
      else if(field == 'sentiment' && tweet['sentiment']) {
        delete tweet.sentiment;
        delete tweet.sentimentConfidence;
      }

      if(!tweet['company'] && !tweet['sentiment'])
        delete tweet.classification;

      tweetsdb.save(tweet);

      res.send(JSON.stringify({success:"true"}));

    });
  }
};

exports.company_random = function(req, res) {
	var confidence = req.params.confidence;
	var count = parseInt(req.params.count);

	var lowerConf = 0.0;
  var upperConf = 1.0;

  if(confidence[0] == '<') {
    upperConf = parseFloat(confidence);
  } else if(confidence[0] == '>') {
    lowerConf = parseFloat(confidence);
  } else {

    try {
     var confSplit = confidence.split(":");
     lowerConf = parseFloat(confSplit[0]);
     upperConf = parseFloat(confSplit[1]);
   } catch(e) {
     res.end("{\"error\":\"Invalid confidence param\"");
     return;
   }

 }

 var tweets = tweetsdb.find({random:{$gte:Math.random()}, classification:"classified", companyConfidence:{$gte:lowerConf, $lte:upperConf}}).limit(count);
 writeJSON(res, tweets);
};


exports.company_confidence_search = function(req, res) {
	var confidence = req.params.confidence;
	var count = parseInt(req.params.count);

	var lowerConf = 0.0;
  var upperConf = 1.0;

  if(confidence[0] == '<') {
    upperConf = parseFloat(confidence);
  } else if(confidence[0] == '>') {
    lowerConf = parseFloat(confidence);
  } else {

    try {
     var confSplit = confidence.split(":");
     lowerConf = parseFloat(confSplit[0]);
     upperConf = parseFloat(confSplit[1]);
   } catch(e) {
     res.end("{\"error\":\"Invalid confidence param\"");
     return;
   }

 }

 var tweets = tweetsdb.find({company:req.params.company, text:{$regex:req.params.company}, classification:"classified", companyConfidence:{$gte:lowerConf, $lte:upperConf}}).limit(count);
 writeJSON(res, tweets);
};



exports.company_confidence = function(req, res) {
	var confidence = req.params.confidence;
	var count = parseInt(req.params.count);

	var lowerConf = 0.0;
  var upperConf = 1.0;

  if(confidence[0] == '<') {
    upperConf = parseFloat(confidence);
  } else if(confidence[0] == '>') {
    lowerConf = parseFloat(confidence);
  } else {

    try {
     var confSplit = confidence.split(":");
     lowerConf = parseFloat(confSplit[0]);
     upperConf = parseFloat(confSplit[1]);
   } catch(e) {
     res.end("{\"error\":\"Invalid confidence param\"");
     return;
   }

 }

 var tweets = tweetsdb.find({company:req.params.company, classification:"classified", companyConfidence:{$gte:lowerConf, $lte:upperConf}}).limit(count);
 writeJSON(res, tweets);
};

exports.company_timeslice = function(req, res) {
	var confidence = req.params.confidence;
	var count = parseInt(req.params.count);
  var startDate = new Date(req.params.start);
  var endDate = new Date(req.params.end);
  var company = req.params.company;

  var lowerConf = 0.0;
  var upperConf = 1.0;

  if(confidence[0] == '<') {
    upperConf = parseFloat(confidence);
  } else if(confidence[0] == '>') {
    lowerConf = parseFloat(confidence);
  } else {

    try {
     var confSplit = confidence.split(":");
     lowerConf = parseFloat(confSplit[0]);
     upperConf = parseFloat(confSplit[1]);
   } catch(e) {
     res.end("{\"error\":\"Invalid confidence param\"");
     return;
   }

 }

 var tweets = tweetsdb.find({company:req.params.company, classification:"classified", companyConfidence:{$gte:lowerConf, $lte:upperConf}}).limit(count);
 writeJSON(res, tweets);
};

 exports.company_frequencies = function(req, res) {
  var confidence = req.params.confidence;
  var startDate = new Date(req.params.start);
  var endDate = new Date(req.params.end);

  var lowerConf = 0.0;
  var upperConf = 1.0;

  if(confidence[0] == '<') {
    upperConf = parseFloat(confidence);
  } else if(confidence[0] == '>') {
    lowerConf = parseFloat(confidence);
  } else {

    try {
      var confSplit = confidence.split(":");
      lowerConf = parseFloat(confSplit[0]);
      upperConf = parseFloat(confSplit[1]);
    } catch(e) {
      res.end("{\"error\":\"Invalid confidence param\"");
      return;
    }

  }

  var timeslice = req.params.timeslice

  var keyfuncs = function(timeslice) {
    return function(tweet) {
      var time;
      var t=tweet.created_at;
      if(timeslice == "hour")
        time = new Date(t.getYear(), t.getMonth(), t.getDay(), t.getHour(), 0, 0);
      else if(timeslice == "hour")
        time = new Date(t.getYear(), t.getMonth(), t.getDay(), 0, 0, 0);

      return {
        time:time,
        sentiment:tweet.sentiment
      };
    };
  };

  var keyf = keyfuncs(timeslice);

  var tweetCountBags = tweetsdb.group({
    keyf:keyf,
    filter:{company:req.params.company, companyConfidence:{$gte:lowerConf, $lte:upperConf}}
  });

  var tweetCountArrays = {};
  tweetCountBags.forEach(function(tweetCount) {
    var sentiment = tweetCount.sentiment;
    if(!tweetCountArrays[sentiment])
      tweetCountArrays[sentiment] = [];

    tweetCountArrays[sentiment].append([tweetCount.time, tweetCount.count]);
  });

  res.end(JSON.stringify(tweetCountArrays));
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

