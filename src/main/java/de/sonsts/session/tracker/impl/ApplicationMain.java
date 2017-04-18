/*
 * Copyright (c) 2017, Michael Sonst, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
