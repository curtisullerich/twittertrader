
var countsdb = require('../store').counts;
var writeJSON = require('../io').writeJSON;
var nconf = require('../config').nconf;


var limit = 10000;

exports.company = function(req, res) {

  var startDate = new Date(req.params.start);
  var endDate = new Date(req.params.end);

  var tweetCountArrays = {
    positive:[],
    negative:[],
    neutral:[],
    irrelevant:[]
  };

console.dir({company:req.params.company, time:{$gte:startDate, $lte:endDate}});

  var counts = countsdb.find({company:req.params.company, time:{$gte:startDate, $lte:endDate}}).limit(limit);

  if(!counts.cursor)
    res.end();
  else {
    var tstream = counts.cursor.stream();

    tstream.on('data', function(count) {
      if(count['positive']) tweetCountArrays.positive.push([count.time, count.positive]);
      if(count['negative']) tweetCountArrays.negative.push([count.time, count.negative]);
      if(count['neutral'])  tweetCountArrays.neutral.push( [count.time, count.neutral ]);
      if(count['irrelevant']) tweetCountArrays.irrelevant.push([count.time, count.irrelevant]);
    });

    tstream.on('close', function() {
      res.end(JSON.stringify(tweetCountArrays));
    });
  }
}

