/**
* Execution of this script requires installation of the NodeJs binary (preferrably version 8+). 
* A convenient way to install is by using [nvm](https://nodejs.org/en/download/package-manager/#nvm).
*/
const baseAbsPath = __dirname + '/';

const argv = process.argv.slice(2);

if (1 > argv.length) {
  console.log('Usage: ' + process.argv[0] + ' ' + process.argv[1] + ' <token>');
  return;
}

const WebSocket = require(baseAbsPath + 'node_modules/ws');
const util = require('util');
 
const httpProto = "http";
const wsProto = "ws";
const hostname = "localhost";
const port = "8080";
const host = hostname + ":" + port;
const path = "/ws-servlet-sample/echo";
const token=argv[0];

const ws = new WebSocket(wsProto + '://' + host + path + '?token=' + token);
 
ws.on('open', function open() {
  console.log('connected');
  ws.send(Date.now());
});
 
ws.on('close', function close() {
  console.log('disconnected');
});
 
ws.on('message', function incoming(data) {
  console.log(`Roundtrip time: ${Date.now() - data} ms`);
});
