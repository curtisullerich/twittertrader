
var tweetsdb = require('../store').tweets;
var writeJSON = require('../io').writeJSON;

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
 * GET companies listing.
 */
exports.companies = function(req, res) {
  res.send("response");
}


/*
 * GET company/:id/:count
 */
exports.company = {
  idcount: function(req, res) {
    var tweets = tweetsdb.find({classification:{$exists:false}, company:req.params.id}).limit(parseInt(req.params.count));
    writeJSON(res, tweets);
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


