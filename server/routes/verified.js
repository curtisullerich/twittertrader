
var tweetsdb = require('../store').tweets;
var writeJSON = require('../io').writeJSON;

/*
 * GET verified/company
 */

exports.company = {
  id: function(req, res) {
    var tweets = tweetsdb.find({classification:"verified", company:req.params.id});
    writeJSON(res, tweets);
  },
  id_timestamp: function(req, res) {
    res.send("response");
  }
}


/*
 * GET verified/companies
 */

exports.companies = function(req, res){
  res.send("respond with a resource");
};


/*
 * POST verified
 */

exports.post = function(req, res){
  console.dir(req); 
//  res.send("respond with a resource");
};



