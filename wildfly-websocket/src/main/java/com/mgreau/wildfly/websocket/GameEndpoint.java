package com.mgreau.wildfly.websocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.mgreau.wildfly.websocket.decoders.MessageDecoder;
import com.mgreau.wildfly.websocket.encoders.MatchMessageEncoder;
import com.mgreau.wildfly.websocket.messages.BetMessage;
import com.mgreau.wildfly.websocket.messages.MatchMessage;
import com.mgreau.wildfly.websocket.messages.Message;

@ServerEndpoint(
		value = "/games/{game-id}",
		        decoders = { MessageDecoder.class }, 
		        encoders = { MatchMessageEncoder.class }
		)
public class GameEndpoint {
	
	private static final Logger logger = Logger.getLogger("GameEndpoint");
	
    /* Queue for all open WebSocket sessions */
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    
    public static void send(MatchMessage msg, String gameId) {
        try {
            /* Send updates to all open WebSocket sessions for this match */
            for (Session session : queue) {
            	if (Boolean.TRUE.equals(session.getUserProperties().get(gameId))){
            		if (session.isOpen()){
	            		session.getBasicRemote().sendObject(msg);
	                    logger.log(Level.INFO, "Score Sent: {0}", msg);
            		}
            	}
            }
        } catch (IOException | EncodeException e) {
            logger.log(Level.INFO, e.toString());
        }   
    }
    
    @OnMessage
    public void message(final Session session, BetMessage msg) {
        logger.log(Level.INFO, "Received: {0}", msg.toString());
    }

    @OnOpen
    public void openConnection(Session session, @PathParam("game-id") String gameId) {
        /* Register this connection in the queue */
        queue.add(session);
        session.getUserProperties().put(gameId, true);
        logger.log(Level.INFO, "Connection opened for game : " + gameId);
    }
    
    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }
    
    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }
    

}