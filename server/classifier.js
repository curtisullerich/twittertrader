var tweetsdb = require('./store').tweets;
var classs = require('./class');

var batchSize = 2;


var tweets = tweetsdb.find({sentiment:{$exists:false}});

var i =0;

var classifier;

function next() {
/*  tweets.nextObject(function(err, tweet) {
    classifier.classify(tweet);
  });
*/

if(i%100000 == 0) {

classifier = classs.setup(function (classification) {
if(i%1000 == 0)
console.log(i);
i+=1;
//console.dir(classification);
	tweetsdb.update(
		{ id_str : classification.id_str },
		{ $set : {sentiment:classification.sentiment, sentimentConfidence:classification.sentimentConfidence } }
		);

setTimeout(next, 0);

});



}

  tweets.nextObject(function(err, tweet) {
    classifier.classify(tweet);
  });




}

var j = i;
var stopped = false;

function force() {
	if(j == i)
	{
		if(stopped == true) {
console.log("Unstopping!");
			next();
			stopped = false;
		} else {
			stopped = true;
		}
	} else {
		stopped = false;
		j = i;
	}
	setTimeout(force, 30000);
}

setTimeout(force, 60000);

next();


//	var tweets = tweetsdb.find({classification:{$exists:false}},

	// Stream tweets from database


//	function(err, result) {
//		if(err) throw err;
//
///		result.each(function(err, tweet) {
//			classifier.classify(tweet);
//		});
//	});
//
//	var tstream = tweets.cursor.stream();
//	tstream.on('data', function (tweet) {
//	  classifier.classify(tweet);
//	});
//	tstream.on('close', function () {
//		dostuff();
//	});
//}

//dostuff();

