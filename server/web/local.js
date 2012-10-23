

var databaseUrl = "twitter";
var collections = ["twits", "captures"];
var db = require("mongojs").connect(databaseUrl, collections);

var fs = require('fs');
var stream = fs.createWriteStream("/storage/tweets.json");

stream.once('open', function(fd) {
	stream.write('[');
	db.twits.find({}).forEach(function(err, tweet) {
			stream.write(JSON.stringify(tweet));
			stream.write(',');
		});

//	stream.write(']');
//	});
});
/*
res.write('[');
	db.twits.find({}). // , function(err, tweets) {
		forEach(function(err, tweet) {
			res.write(JSON.stringify(tweet));
			res.write(',');
		});
	//	res.end(']');
//	});

	//db.twits.find({}).forEach( function(tweet) {
		//res.send(JSON.stringify(tweet));
	//	res.send('"""');
	//});
});
*/
