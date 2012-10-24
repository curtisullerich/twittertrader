
exports.writeJSON = function(res, tweets) {
  res.writeHead(200, {'Content-Type': 'application/json'});
  res.write('[');

  tweets.toArray(function(err, tweetArr) {
    var i = 0;
    for(; i<tweetArr.length-1; i++) {
      res.write(JSON.stringify(tweetArr[i]));
      res.write(',');
    }
    if(tweetArr.length > 0)
      res.write(JSON.stringify(tweetArr[i]));
    res.end(']');
  });
}
