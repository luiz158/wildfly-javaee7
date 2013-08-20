  var host = "ws://localhost:8080/matchpoint/games/game1";
      var socket; //websocket
      var g1comments; //Game1 comments
      //Player1 elements
      var g1p1, g1p1games, g1p1sets, g1p1points, g1p1set1, g1p1set2, g1p1set3; 
      //Player2 elements
      var g1p2, g1p2games, g1p2sets, g1p2points, g1p2set1, g1p2set2, g1p2set3; 
      
      function connect() {
    	  iniHtmlElements();
          createWebSocket(host);
      }

      function iniHtmlElements(){
    	  g1p1 =  document.getElementById("g1-p1");
    	  g1p1games =  document.getElementById("g1-p1-games")
          g1p1sets =  document.getElementById("g1-p1-sets");
    	  g1p1points =  document.getElementById("g1-p1-points");
    	  g1p1set1 =  document.getElementById("g1-p1-set1");
    	  g1p1set2 =  document.getElementById("g1-p1-set2");
    	  g1p1set3 =  document.getElementById("g1-p1-set3");
    	  g1p1service = document.getElementById("g1-p1-service");
          //player2
          g1p2 =  document.getElementById("g1-p2");
          g1p2games =  document.getElementById("g1-p2-games");
          g1p2sets =  document.getElementById("g1-p2-sets");
          g1p2points =  document.getElementById("g1-p2-points");
          g1p2set1 =  document.getElementById("g1-p2-set1");
          g1p2set2 =  document.getElementById("g1-p2-set2");
          g1p2set3 =  document.getElementById("g1-p2-set3");
          g1p2service = document.getElementById("g1-p2-service");
          //comments
          g1comments =  document.getElementById("g1-comments");
      }
     
      function createWebSocket(host) {
             if(!window.WebSocket) {
            	 var spanError = document.createElement('span');
            	 spanError.setAttribute('class', 'alert alert-danger');
            	 spanError.innerHTML = "Votre navigateur ne supporte pas les WebSockets!";
             	 document.body.appendChild(spanError);
                  return false;
             } else {
                     socket = new WebSocket(host);
                     socket.onopen = function() { document.getElementById("g1-status").innerHTML = 'LIVE'; }
                     socket.onclose = function() { document.getElementById("g1-status").innerHTML = 'FINISHED';  }
                     socket.onerror = function() { document.getElementById("g1-status").innerHTML = 'ERROR';  }
                     socket.onmessage = function(msg){
                             try { //tente de parser data
        					       console.log(data);
        	                       var obj = JSON.parse(msg.data);
        	                     	//comments
        	                       g1comments.value = obj.match.comments;
        	                       g1comments.scrollTop = 999999;
        	                       //service
        	                       //if (obj.match.service === "player1"){
        	                    	   g1p1service.innerHTML = "◉";
            	                  // } else {
            	                	//   g1p2service.innerHTML = "◉";
            	                   //}
        	                       //player1
        	                       g1p1.innerHTML = obj.match.players[0].name;
        	                       g1p1games.innerHTML = obj.match.players[0].games;
        	                       g1p1sets.innerHTML = obj.match.players[0].sets;
        	                       g1p1points.innerHTML = obj.match.players[0].points;
        	                       g1p1set1.innerHTML = obj.match.players[0].set1;
        	                       g1p1set2.innerHTML = obj.match.players[0].set2;
        	                       g1p1set3.innerHTML = obj.match.players[0].set3;
        	                       //player2
        	                       g1p2.innerHTML = obj.match.players[1].name;
        	                       g1p2games.innerHTML = obj.match.players[1].games;
        	                       g1p2sets.innerHTML = obj.match.players[1].sets;
        	                       g1p2points.innerHTML = obj.match.players[1].points;
        	                       g1p2set1.innerHTML = obj.match.players[1].set1;
        	                       g1p2set2.innerHTML = obj.match.players[1].set2;
        	                       g1p2set3.innerHTML = obj.match.players[1].set3;
        	                       
                             } catch(exception) {
                                     data = msg.data
                                     console.log(data);
                             }      
                             
                       }
             }
      }
      function sendMessage(player) {
    	  var msg = "{ \"type\" : \"bet\", \"name\" : \""+ player +"\" }"
    	  console.log(msg);
             if(typeof data == 'object') { // si data est un objet on le change en chaine
                     data = JSON.stringify(data);
             }
             socket.send(msg);
      }

      window.addEventListener("load", connect, false);