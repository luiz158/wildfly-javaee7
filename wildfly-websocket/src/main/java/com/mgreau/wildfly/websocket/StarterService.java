package com.mgreau.wildfly.websocket;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;

import com.mgreau.wildfly.websocket.messages.MatchMessage;

@Startup
@Singleton
public class StarterService {
    /* Use the container's timer service */
    @Resource TimerService tservice;
    private Random random;
    private Set<TennisMatch> games;
    
    private static final Logger logger = Logger.getLogger("StarterService");
    
    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "Initializing EJB.");
        random = new Random();
        games = new LinkedHashSet<>();
        games.add(new TennisMatch("game1", "Federer", "Nadal"));
        games.add(new TennisMatch("game2", "Tsonga", "Djoko"));
    }
    
    @Schedule(second="*/3", minute="*",hour="*", persistent=false)
    public void play() {
    	logger.log(Level.INFO, "------- Play 1 point -----------");
    	for (TennisMatch g : games){
    		if (random.nextInt(2) == 1){
        		g.playerOneScores();
        	} else {
        		g.playerTwoScores();
        	}
        	GameEndpoint.send(new MatchMessage(g), g.getKey());
        	//if there is a winner, send result and reset the game
        	if (g.hasMatchWinner()){
        		g.reset();
        		logger.log(Level.INFO, "------- RESET Game -----------");
        		try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					logger.log(Level.INFO, "------- Starting a new Game -----------");
				}
        	}
    	}
    }
    
    @Timeout
    public void timeout(TimerService tseTimerService){
    	logger.log(Level.INFO, "------- TIMEOUT -----------"+tseTimerService.getTimers().size());
    }
}
