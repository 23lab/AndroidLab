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
var sockList = {};
net.createServer(options, function(sock) {
    sock.name = sock.remoteAddress + ":" + sock.remotePort 
    // this will be return to client when the client connected
    var connectionRsp = {
        cmd: "connection",
        clientId: sock.name
    };


    // register two callback, when a msg resieved(not some raw data)
    sock.onData = function (nbMsg){
        console.log("onData: ");
        console.log(nbMsg);
    };

    sock.write(encode(JSON.stringify(connectionRsp)), "UTF-8", function () {
        console.log("Connection Rsp inished!");
    });

    // put socket into array, use socket name as key
    var obj = {};
    sockList[sock.name] = sock;

    console.log('CONNECTED: ' + sock.remoteAddress + ':' + sock.remotePort);
    var buf = "";
    var STATE_WAITING_FOR_START = 0;
    var STATE_WAITING_FOR_END = 1;
    var state = STATE_WAITING_FOR_START;

    function process(data) {
        var oldBufLenth = buf.length;
        buf += data;

        for (var i = oldBufLenth; i < data.length; i++) {
            if (buf.charAt(i) === String.fromCharCode(HEAD)) {
                // change status if only it is waiting for start Flag
                if (state == STATE_WAITING_FOR_START) {
                    // delete the chars before C_A
                    buf = buf.substr(i + 1, buf.length);

                    // restore index
                    i = 0;
                    
                    // change state, waiting for C_D
                    state = STATE_WAITING_FOR_END;
                    continue;
                }
            } else if (buf.charAt(i) === String.fromCharCode(TAIL)) {
                if (state == STATE_WAITING_FOR_END) {
                    // cut the decoded msg
                    var rawData = buf.substr(0, i);
                    var msg = rawData.substr(6);
                    console.log("rawData: " + rawData);
                    console.log("msg: " + msg);

                    // convert origin msg to a well formed msg
                    var jsonMsg = new NBMsg(msg);
                    sock.onData(jsonMsg);

                    // delete the chars before C_D
                    buf = buf.substr(i + 1, buf.length);

                    // restore state
                    state = STATE_WAITING_FOR_START;
                    continue;
                }
            }
        }

    }
    sock.on('data', function(data) {
        process(data);
    });

    sock.on('close', function(data) {
        console.log('CLOSED' + sock.name);
        delete sockList[sock.name]; 
        console.log(data);
    });

    sock.on('error', function(data) {
        console.log('error');
    });
}).listen(PORT, HOST);

console.log('Server listening on ' + HOST +':'+ PORT);

function encode(msg) {
    var length = 6 + msg.length;
    var alignLength = ("000000" + length).slice(-6);
    var result = String.fromCharCode(HEAD) + alignLength + msg + String.fromCharCode(TAIL);
    console.log(result);
    return result;
}

function broadcast(msg) {
    console.log(sockList);
    for (var index in sockList) {
        sockList[index].write(encode("rsp: " + msg), "UTF-8", function () {
            console.log("send to client " + index + " finished!");
        });
    }
}

function NBMsg(msg){
    // encode Msg
    try{
        var msgJson = JSON.parse(msg);
        console.log(msgJson);
        this.cmd = msgJson.cmd;
        this.target = msgJson.target;
        this.params = msgJson.params;
    }catch(e){
        console.log("myJson not a json");
    }
}

NBMsg.prototype.dispatch = function () {
    if (this.target == undefined || this.target == "") {
        console.log("No Target !!");
    } else {

    }
}
