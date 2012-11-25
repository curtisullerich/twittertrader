var tweetsdb = require('store').tweets;
var classifier = require('class');

var batchSize = 1000;


classifier.setup(function (classification) {
	tweetsdb.update(
		{ id_str : classification.id_str },
		{ $set : classification }
		);
});

while(true) {
	var tweets = tweetsdb.find({classification:{$exists:false}}).limit(batchSize);

	
}

var db = require("mongojs").connect(nconf.get('database'), [nconf.get('collection')]);

var nTwitter = require("ntwitter");
var twitter = new nTwitter(nconf.get('twitter_auth'));

var i=0;
twitter.stream(
	'statuses/filter',
	{ track: nconf.get('keywords') },
	function(stream) {
		stream.on('data', function(tweet) {
			if(i > nconf.get('capture:frequency') && tweet.user.lang == 'en')
			{
				i = 0;
				tweet.random = Math.random();
				tweet.created_at = new Date(tweet.created_at);
				db.twits.save(tweet);
				console.log('tweet: '+ tweet.created_at);
			}
			i++;
		});
	}
);

