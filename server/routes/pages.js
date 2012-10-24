
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'Express' });
}


/*
 * GET verify page.
 */

exports.verify = function(req, res){
  res.render('verify', { title: 'Verify Tweets' });
};
