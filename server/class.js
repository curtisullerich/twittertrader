var child_process = require('child_process');

var exec = '../classify/target/TwitterTrader-0.0.1-SNAPSHOT.jar';


exports.setup = function(callback) {
  var process = child_process.spawn(exec);

  process.stdout.on('data', function(data) {
    // Assume we get a whole line back
    var line = data;
    var lineParts = line.split(',');

    var ret = {
      id_str: lineParts[0].trim(),
      company: lineParts[1].trim(),
      companyConfidence: parseFloat(lineParts[2]),
      sentiment: lineParts[3].trim(),
      sentimentConfidence: parseFloat(lineParts[4]),
    };

    callback(ret);
  }

  var ret = {};

  ret.classify = function(tweet) {
    process.stdin.write(JSON.stringify(tweet));
    process.stdin.write('\n');
  }

  return ret;
}
