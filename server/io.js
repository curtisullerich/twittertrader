
exports.writeJSON = function(res, tweets) {
  res.writeHead(200, {'Content-Type': 'application/json'});
  res.write('[');

  var stream = tweets.cursor.stream();

  stream.on('data', function (tweet) {
      res.write(JSON.stringify(tweet));
      res.write(',');
    });

  stream.on('close', function () {
    res.end('{}]');
  });
}

exports.writeCSV = function(res, tweets, field) {
  res.writeHead(200, {'Content-Type': 'text/csv'});

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

