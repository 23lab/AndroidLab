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

    var connectionRsp = {
        cmd: "connection",
        clientId: sock.name
    };

    sock.write(encode(JSON.stringify(connectionRsp)), "UTF-8", function () {
        console.log("Connection Rsp inished!");
    });

    // put socket into array, use socket name as key
    var obj = {};
    sockList[sock.name] = sock;

    console.log("========================================");
    console.log(sock);
    console.log("========================================");
    console.log('CONNECTED: ' + sock.remoteAddress + ':' + sock.remotePort);
    var buf = "";
    var STATE_WAITING_FOR_START = 0;
    var STATE_WAITING_FOR_END = 1;
    var state = STATE_WAITING_FOR_START;
    var HEAD_POS = -1;

    function process(chr, index) {
        for (int i = 0; i < buf.length; i++) {
            if (chr === String.fromCharCode(HEAD)) {
                state = STATE_WAITING_FOR_END;
            }
        }

    }
    sock.on('data', function(data) {
        buf += data;
        var C_A_pos = buf.search(String.fromCharCode(HEAD));
        var C_D_pos = buf.search(String.fromCharCode(TAIL));
        if (C_A_pos !== -1 && C_D_pos !== -1 && C_A_pos < C_D_pos) {
            while (C_A_pos !== -1 && C_D_pos !== -1 && C_A_pos < C_D_pos) {
                console.log(buf);
                var rawData = buf.substr(C_A_pos + 1, C_D_pos - C_A_pos - 1);
                var length = parseInt(rawData.substr(0, 6)) + 0;
                console.log("RawData length: " + length);
                console.log("RawData data: " + rawData);
                if (length !== rawData.length) {
                    console.log("length dismatch");
                } else {
                    // Get a valid msg
                    var msg = rawData.substr(6);
                    console.log(sock.remotePort + ":" + sock.remotePort + ": " + msg);
                    console.log(sockList.length);

                    // broadcast
                    broadcast(msg);

                    // convert origin msg to a well formed msg
                    var jsonMsg = new NBMsg(msg);
                    console.log("====================");
                    console.log(jsonMsg);
                }
                buf = buf.substr(C_D_pos + 1);
                C_A_pos = buf.search(String.fromCharCode(HEAD));
                C_D_pos = buf.search(String.fromCharCode(TAIL));
            }
        } else { // clear buf
            console.log("data protocol error!");
            buf = "";
        }
        console.log('buf: ' + buf);
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
