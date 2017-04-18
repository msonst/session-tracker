package de.sonsts.session.tracker.impl;

import org.apache.log4j.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ApplicationMain
{
    private static final Logger LOGGER = Logger.getLogger(ApplicationMain.class);
    
    public static void main(String[] args)
    {
        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded())
            {
                Vertx vertx = res.result();
                vertx.deployVerticle(new SessionTrackerVerticle("session-status"));
                LOGGER.info("Verticle deployed.");
            }
            else
            {
                LOGGER.error("Failed to deploy verticle. " + res.cause());
            }
        });
    }
}
