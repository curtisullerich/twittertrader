var tweetsdb = require('store').tweets;
var classs = require('class');

var batchSize = 1000;


var classifier = classs.setup(function (classification) {
	tweetsdb.update(
		{ id_str : classification.id_str },
		{ $set : classification }
		);
});

while(true) {
	var tweets = tweetsdb.find({classification:{$exists:false}}).limit(batchSize);

	// Stream tweets from database
	var tstream = tweets.cursor.stream();
	tstream.on('data', function (tweet) {
	  classifier.classify(tweet);
	});
	tstream.on('close', function () {
	});
}

classifier.end();
