/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package com.mgreau.wildfly.websocket.messages;

public class BetMessage {
	
	private String winner;
	
	public BetMessage(String winner){
		this.winner = winner;
	}
	
	
	public String getWinner(){
		return winner;
	}
    
	public String toString(){
		return "[BetMessage] ...";
	}
}
