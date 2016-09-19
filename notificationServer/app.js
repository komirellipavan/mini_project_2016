var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

server.listen(3000);

var usernames = {};

console.log('listening on *:3000');
io.on('connection', function (socket) {
 // when the client emits 'adduser', this listens and executes
	socket.on('adduser', function(username){
		// we store the username in the socket session for this client
		socket.username = socket.id;
		// add the client's username to the global list
		usernames[socket.id] = username;
			console.log(usernames);
		
	});
 // when the user disconnects.. perform this
	socket.on('disconnect', function(){
		// remove the username from global usernames list
		delete usernames[socket.username];
		console.log(usernames);
		
	});
	
	
	socket.on('check', function(user){
		console.log("checking.....");
		var serviceProviderStatus;
		for(var key in usernames) {
			if(usernames[key] === user) {
				// do stuff with key
				//console.log(key + ": " +user);
				serviceProviderStatus = "online"
				
				}
		}
		if( serviceProviderStatus == "online"){
			socket.emit('status', "online");
		}
		else{
			socket.emit('status', "offline");
		}
		
		
	});
	// Handle any errors that occur.
socket.onerror = function(error) {
  console.log('WebSocket Error: ' + error);
};
 
 socket.on('book', function(provider,client){
	 for(var key in usernames) {
			if(usernames[key] === provider) {
				// do stuff with key
				socket.broadcast.to(key).emit('req',client);
				console.log("sentBooked");
				}
		}
		
		
		
	});
 
  });
