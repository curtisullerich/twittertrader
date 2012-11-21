var nconf = require('nconf');
// First consider commandline arguments and environment variables, respectively.
nconf.argv().env();
// Then load configuration from a designated file.
nconf.file({ file: 'config.json' });


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

