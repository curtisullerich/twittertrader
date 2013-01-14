var classs = require('./class');
var fs = require('fs');

var nconf = require('nconf');
// First consider commandline arguments and environment variables, respectively.
nconf.argv().env();
// Then load configuration from a designated file.
nconf.file({ file: 'config.json' });


//var companies = ["none", "apple", "walmart", "Verizon", "DollarTree", "Starbucks", "Costco", "Coke", "eBay", "Google", "Microsoft", "Amazon", "twitter", "Netflix"];


//var db = require("mongojs").connect(nconf.get('database'), [nconf.get('collection')]);

var nTwitter = require("ntwitter");
var twitter = new nTwitter(nconf.get('twitter_auth'));

var activeTweets = {};
var activeTweetCount = 0;
var activeTweetLastCount = 0;
var dups = 0;
//var classifier = getClassifier();


var res = fs.createWriteStream('tweets.csv', {flags:'a'});

var i=0;
twitter.stream(
	'statuses/filter',
	{ track: nconf.get('keywords') },
	function(stream) {
		stream.on('data', function(tweet) {
			// if(i > nconf.get('capture:frequency') && tweet.user.lang == 'en')
			// {
			// 	i = 0;
			// 	tweet.random = Math.random();
			// 	tweet.created_at = new Date(tweet.created_at);
			// 	db.twits.save(tweet);
			// 	console.log('tweet: '+ tweet.created_at);
			// }
			// i++;

	if(tweet.user.lang != 'en') return;


      res.write('"' + tweet.id_str + '",');
      res.write('"' + tweet.retweet_count + '",');
      res.write('"' + tweet.created_at + '",');
      res.write('"' + tweet.user.friends_count + '",');
      res.write('"' + tweet.user.screen_name + '",');
      res.write('"' + tweet.user.lang + '",');
      res.write('"' + tweet.user.verified + '",');
      res.write('"' + tweet.text.replace(/\n/g, "") + '"');
      res.write("\n");


			

			//classifier.classify(tweet);
			//activeTweetCount++;
		});
	}
);

function getCountTimestamp(date) {
	hourdate = new Date(date);
	hourdate.setMinutes(0);
	hourdate.setSeconds(0);
	hourdate.setMilliseconds(0);
	return hourdate;
}

function company(text) {
	var company = "none";
	// Determine company
	for(var i=0; i<companies.length; i++) {
		if(-1 != tweet.text.toLowerCase().indexOf(companies[i].toLowerCase())) {
			company = companies[i].toLowerCase();
		}
	}
	return company;
}


function getClassifier() {
	return classs.setup(function (classification) {
		count += 1;
		if(count%1000 == 0)
			console.log(count);

		if(!activeTweets[classification.id_str])
			return;

		var tweet = activeTweets[classification.id_str];

		// Determine company
		tweet.company = company(tweet.text);
		tweet.companyConfidence = -10;

		tweet.sentment = classification.sentiment;
		tweet.sentimentConfidence = classification.sentimentConfidence;

		var timestamp = getCountTimestamp(tweet.created_at);
	//	console.dir(timestamp);
		var inc = {};
		inc[tweet.sentiment] = 1;
		console.dir({time:timestamp, company:company, $inc:inc});
		countsdb.update({time:timestamp, company:company}, {$inc:inc}, {upsert:true});

		delete activeTweets[classification.id_str];

		activeTweetCount -= 1;

	});
}


function updateClassifier() {

	if(activeTweetCount == activeTweetLastCount)
		dups += 1;
	else
		dups = 0;

	activeTweetLastCount = activeTweetCount;


	if(dups > 100)
		classifier = getClassifier();

	setTimeout(updateClassifier, 5000);
}


//setTimeout(updateClassifier, 5000);
