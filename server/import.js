

var databaseUrl = "twitter";
var collections = ["twits", "captures"];

var fs = require("fs");
var db = require("mongojs").connect(databaseUrl, collections);

fs.readFile(process.cwd() + '/classifications.csv', 'utf8', function(err,contents) {
  var pattern = /^(\w+),(\w+),([.\w]+),(\w+),([.\w]+)$/
  var lines = contents.split("\n");
  var i = 0;
 
  function process() {
    var line = lines[i];
    i = i+1;
    var match = pattern.exec(line);

    if(match) {
      var id_str = match[1];
      var company = match[2];
      var companyConf = parseFloat(match[3]);
      var sentiment = match[4];
      var sentimentConf = parseFloat(match[5]);
 
      db.twits.find({id_str:id_str}, function(err, one) {   
        if(one && one.length > 0) {
          var tweet = one[0];
          tweet.company = company.toLowerCase();
          tweet.companyConfidence = companyConf;
          tweet.sentiment = sentiment;
          tweet.sentimentConfidence = sentimentConf;
           
          tweet.classification = "classified";
          db.twits.save(tweet);
          console.dir(tweet.id_str);
        } else {
	  console.log("Could not find tweet: " + id_str);
	}
      });

    } else {
      console.dir(match);
    }

    if(i < lines.length)
      setTimeout(process, 50);
  }
  process();
});

db.close();
