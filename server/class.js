var child_process = require('child_process');
var readline = require('readline');
var carrier = require('carrier');

var exec = 'java';
var options= [ '-cp', '../classify/target/TwitterTrader-0.0.1-SNAPSHOT.jar', 'classification.Classify'];


exports.setup = function(callback) {
  var process = child_process.spawn(exec, options);

  process.stderr.on('data', function(data) {
    console.log('stderr: ' + data);
  });

  var online =  function(line) {

//console.log(line);

    var lineParts = line.split(',');

//console.dir(lineParts);

if(lineParts.length < 3)
return;

try {
var id_str = lineParts[0].trim();

if(id_str.length > 20) return;

    var ret = {
      id_str: lineParts[0].trim(),
//      company: lineParts[1].trim(),
//      companyConfidence: parseFloat(lineParts[2]),
      sentiment: lineParts[1].trim(),
      sentimentConfidence: parseFloat(lineParts[2]),
    };

    callback(ret);

} catch (err) { }
  };

  carrier.carry(process.stdout, online);

  var ret = {};

  ret.classify = function(tweet) {

//    rl.question(JSON.stringify
//    console.log(JSON.stringify(tweet));

//console.log(tweet.text);

    process.stdin.write(JSON.stringify( [ tweet ] ).replace("\u0085", "\\n"));
    process.stdin.write("\n");
  };

  return ret;
}
