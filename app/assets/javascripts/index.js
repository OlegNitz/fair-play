$(function() {
  document.ws = new WebSocket($("body").data("ws-url"));

  document.ws.onmessage = function(event) {
    var message = JSON.parse(event.data);
    alert("Got msg " + message);
  };
});
