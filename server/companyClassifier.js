var tweetsdb = require('./store').tweets;
var countsdb = require('./store').counts;


var companies = ["none", "walmart", "Verizon", "DollarTree", "Starbucks", "Costco", "Coke", "eBay", "Google", "Microsoft", "Amazon", "twitter", "Netflix"];

//countsdb.remove({});

var tweets = tweetsdb.find({sentiment:{$exists:true}, companyConfidence:{$ne:-1}});


function getCountTimestamp(date) {
	hourdate = new Date(date);
	hourdate.setHours(0);
	hourdate.setMinutes(0);
	hourdate.setSeconds(0);
	hourdate.setMilliseconds(0);
	return hourdate;
}



tweets.each(function(err, tweet) {

	if(!tweet)
		return;

	if(!tweet['sentiment'])
		return;

	var company = "none";
	// Determine company
	for(var i=0; i<companies.length; i++) {
		if(-1 != tweet.text.toLowerCase().indexOf(companies[i].toLowerCase())) {
			company = companies[i].toLowerCase();
		}
	}

	tweetsdb.update({_id:tweet._id}, {$set:{company:company, companyConfidence:-1}});

	var timestamp = getCountTimestamp(tweet.created_at);
//	console.dir(timestamp);
	var inc = {};
	inc[tweet.sentiment] = 1;
	//console.dir({time:timestamp, company:company, $inc:inc});
	countsdb.update({time:timestamp, company:company}, {$inc:inc}, {upsert:true});
});

