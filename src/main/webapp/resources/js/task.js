// Clients connect to 3 websocket endpoints here

var uri;
var port;
var websocketSession;

function init() {
    
 var path = '/taskservice/update';
 var scheme = window.location.protocol === 'https:' ? 'wss://' : 'ws://';
 var host = window.location.host;
 
 uri = scheme + host + path;
 console.log('URI: ' + uri)

 if (!websocketSession) {
  websocketSession = new WebSocket(uri);
 }

 websocketSession.onopen = function () {
  websocketSession.send("Opened");
  //alert('1. Connected to ' + uri);
 };

 websocketSession.onerror = function (e) {
  //alert('1. onerror to ' + uri);
 };

 websocketSession.onmessage = function (e) {
  var data = e.data; 
  //May use conditions to update UI based on value of data
  var trigger = document.getElementById('trigger');
  trigger.click();
 };
}

document.addEventListener("DOMContentLoaded", init, false);