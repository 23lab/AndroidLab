var net = require('net'); 
var HOST = '198.52.100.190';
var PORT = 8111;

var options = { 
    fd: null,
    allowHalfOpen: false,
    readable: true,
    writable: true
};
var HEAD = 1;
var TAIL = 4;
var sockList = [];
net.createServer(options, function(sock) {
    // Store all sock when connected
    sockList.push(sock);

    // push all data into buf first, and then decode from it
    var buf = "";
    sock.on('data', function(data) {
        buf += data;

        // find the msg head & tail
        var C_A_pos = buf.search(String.fromCharCode(HEAD));
        var C_D_pos = buf.search(String.fromCharCode(TAIL));

        // if head and tail r found
        if (C_A_pos !== -1 && C_D_pos !== -1 && C_A_pos < C_D_pos) {
            
            // maybe multiple msg in buf
            while (C_A_pos !== -1 && C_D_pos !== -1 && C_A_pos < C_D_pos) {
                console.log(buf);
                var rawData = buf.substr(C_A_pos + 1, C_D_pos - C_A_pos - 1);
                var length = parseInt(rawData.substr(0, 6)) + 0;
                console.log("RawData length: " + length);
                console.log("RawData data: " + rawData);
                if (length !== rawData.length) {
                    console.log("length dismatch");
                } else {
                    var msg = rawData.substr(6);
                    console.log(sock.remotePort + ":" + sock.remotePort + ": " + msg);
                    console.log(sockList.length);
                    for (var index in sockList) {
                        sockList[index].write(encode("rsp: " + msg), "UTF-8", function () {
                            console.log("send to client finished!");
                        });
                    }
                }
                
                // discard msg from buf if it is decoded succ
                buf = buf.substr(C_D_pos + 1);
                C_A_pos = buf.search(String.fromCharCode(HEAD));
                C_D_pos = buf.search(String.fromCharCode(TAIL));
            }
        } else { 
            // clear buf if protocol error
            console.log("data protocol error!");
            buf = "";
        }
        console.log('buf: ' + buf);
    });

    sock.on('close', function(data) {
        console.log('CLOSED' + sockList.indexOf(sock));
        sockList.splice(sockList.indexOf(sock));
        console.log(data);
    });

    sock.on('error', function(data) {
        console.log('error');
    });
}).listen(PORT, HOST);

console.log('Server listening on ' + HOST +':'+ PORT);

/**
 * encoding msg, C_A + length(not include C_A & C_D, 6 bytes) + C_D
 */
function encode(msg) {
    var length = 6 + msg.length;
    var alignLength = ("000000" + length).slice(-6);
    var result = String.fromCharCode(HEAD) + alignLength + msg + String.fromCharCode(TAIL);
    console.log(result);
    return result;
}
