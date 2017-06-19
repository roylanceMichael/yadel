package org.roylance.yadel.services;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.roylance.yadel.enums.CommonTokens;
import org.roylance.yadel.utilities.ActorIpUtilities;

class ActorSingleton {
    private static ActorSystem actorSystem;
    static ActorSystem getActorSystem() {
        if (actorSystem == null) {
            final Config config = ConfigFactory.parseString(String.format(CommonTokens.TcpPortConifguration, "0"))
                    .withFallback(ConfigFactory.parseString(
                            String.format(CommonTokens.TcpHostConfiguration,
                                    ActorIpUtilities.INSTANCE.getNonLocalhostIPAddress(getManagerIP()))))
                    .withFallback(ConfigFactory.load());
            actorSystem = ActorSystem.create(CommonTokens.ClusterName, config);
        }
        return actorSystem;
    }

    static ActorSelection getManager() {
        if (actorSystem == null) {
            getActorSystem();
        }
        return actorSystem.actorSelection(String.format(CommonTokens.INSTANCE.getFullManagerLocation(), getManagerIP()));
    }

    private static String getManagerIP() {
        final String akkaHost = System.getenv("AkkaHost");
        if (akkaHost == null || akkaHost.isEmpty()) {
            return "192.168.43.35";
        }
        return akkaHost;
    }
}