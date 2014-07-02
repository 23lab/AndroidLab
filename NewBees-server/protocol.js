// 首次连接返回, 对应onConnection
{ 
cmd: "connection",
         clientId: sock.name
}

// 其他消息
{ 
    cmd: "custom_cmd",
    target: "send this msg to whom", 
    source: "who am i", // the target can send a msg back by this(source), if empty, regard it as a server msg
                        // other params, use a param wrapper data
    param: {
        k1: "value1", 
        k2: "value2"
    }
}
