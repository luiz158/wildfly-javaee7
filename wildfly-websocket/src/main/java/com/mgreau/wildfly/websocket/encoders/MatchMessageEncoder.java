/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package com.mgreau.wildfly.websocket.encoders;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.mgreau.wildfly.websocket.messages.MatchMessage;

/* Encode an MatchMessage as JSON.
 * For example, (new MatchMessage(tennisMatch))
 * is encoded as follows:
 '{ "match" : {  "comments" : "What a point ! Ace !! ",' +
 '"players": [ ' +
 '{ "name" : "R. FEDERER", "games" : "18" , "sets" : "3" , "points" : "15", "set1" : "6", "set2" : "6", "set3" : "6" }, ' +
 '{ "name" : "R. NADAL", "games" : "7" , "sets" : "0" , "points" : "15", "set1" : "1", "set2" : "2", "set3" : "4"  } ' +
 '  ] },  ' + 
 ' "bet" : "nadal winner" '+
 '}';
 */
public class MatchMessageEncoder implements Encoder.Text<MatchMessage> {
	@Override
	public void init(EndpointConfig ec) {
	}

	@Override
	public void destroy() {
	}

	@Override
    public String encode(MatchMessage m) throws EncodeException {
        StringWriter swriter = new StringWriter();
        try (JsonWriter jsonWrite = Json.createWriter(swriter)) {
        	JsonObjectBuilder builder = Json.createObjectBuilder();
        	builder.add("bet", "Player x wins!")
        	.add("match", Json.createObjectBuilder()
            			.add("comments",m.getMatch().getLiveComments())
            			.add("serve", m.getMatch().getServe())
            			.add("players", Json.createArrayBuilder()
        					 .add(Json.createObjectBuilder()
        							.add("name", m.getMatch().getPlayerOneName())
    			                	.add("games", m.getMatch().getP1CurrentGame())
    			                	.add("sets", m.getMatch().getP1Sets())
    			                	.add("points", m.getMatch().getPlayer1Score())
    			                	.add("set1", m.getMatch().getP1Set1())
    			                	.add("set2", m.getMatch().getP1Set2())
    			                	.add("set3", m.getMatch().getP1Set3()))
    					      .add(Json.createObjectBuilder()
					    	     	.add("name", m.getMatch().getPlayerTwoName())
				                	.add("games", m.getMatch().getP2CurrentGame())
				                	.add("sets", m.getMatch().getP2Sets())
				                	.add("points", m.getMatch().getPlayer2Score())
				                	.add("set1", m.getMatch().getP2Set1())
				                	.add("set2", m.getMatch().getP2Set2())
				                	.add("set3", m.getMatch().getP2Set3()))
            					));
            	
        	jsonWrite.writeObject(builder.build());	
        }
        return swriter.toString();
    }
}
