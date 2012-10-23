
var db = require('../store').db;

/*
 * GET random listing.
 */

exports.random = function(req, res){
  res.writeHead(200, {'Content-Type': 'application/json'});
  res.write('[');
  var tweets = db.twits.find().limit(10);

  for(; tweets.hasNext(); ) {
    res.write(JSON.stringify(tweets.next()));
    res.write(',');
  }
//{}, {}, 10, 0, function (err, tweets) {
//    tweets.forEach(function(tweet) {
//      res.write(JSON.stringify(tweet));
//      res.write(',');
//    });
    res.end(']');
 // });
};

/*
 * GET companies listing.
 */
exports.companies = function(req, res) {
  res.send("response");
}


/*
 * GET company/
 */
exports.company = {
  id: function(req, res) {
    res.send("response");
  }
}


/*
 * GET sentiment/
 */
exports.sentiment = {
  kind: function(req, res) {
    res.send("response");
  }
}


