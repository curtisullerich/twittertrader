var JSONStream = require("JSONStream");

exports.writeJSON = function(res, tweets) {
  res.writeHead(200, {'Content-Type': 'application/json'});

  // Check we have tweets to display
  if(tweets.cursor == null) {
    res.end('[]');
    return;
  }
  
  // Setup JSON stream to write to response
  var stream = JSONStream.stringify();
  stream.on('data', function(data) {
    res.write(data);
  });
  stream.on('end', function() {
    res.end();
  });

  // Stream tweets from database to json
  var tstream = tweets.cursor.stream();
  tstream.on('data', function (tweet) {
      stream.write(tweet);
  });
  tstream.on('close', function () {
    stream.end();
  });
}

exports.writeCSV = function(res, tweets, field) {
  res.writeHead(200, {'Content-Type': 'text/csv'});

  // Check we have tweets to display
  if(tweets.cursor == null) {
    res.end();
    return;
  }

  var stream = tweets.cursor.stream();

  stream.on('data', function(tweet) {
     if(field)
        res.write('"' + tweet[field] + '",');
      res.write('"' + tweet.id_str + '",');
      res.write('"' + tweet.retweet_count + '",');
      res.write('"' + tweet.created_at + '",');
      res.write('"' + tweet.user.friends_count + '",');
      res.write('"' + tweet.user.screen_name + '",');
      res.write('"' + tweet.user.lang + '",');
      res.write('"' + tweet.user.verified + '",');
      res.write('"' + tweet.text.replace(/\n/g, "") + '"');
      res.write("\n");
  });


  stream.on('close', function() {
    res.end();
  });
}

