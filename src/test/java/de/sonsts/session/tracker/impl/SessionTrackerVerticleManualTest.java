package de.sonsts.session.tracker.impl;

import io.vertx.core.Vertx;

public class SessionTrackerVerticleManualTest
{
    private static final String ADDRESS = "session-status";
    
    public static void main(String[] args)
    {
        SessionTrackerVerticle uut = new SessionTrackerVerticle(ADDRESS);
        
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(uut);
        
        vertx.eventBus().consumer(ADDRESS, message -> {
            System.out.println("Recieved: address:" + message.address() + ", replyAddress=" + message.replyAddress() + ", headers=" + message.headers() + ", body=" + message.body());
        });
        
        uut.evaluate();
    }
}
