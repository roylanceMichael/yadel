package org.roylance;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.roylance.yadel.api.enums.CommonTokens;

class ActorSingleton {
    private static ActorSystem actorSystem;
    static ActorSystem getActorSystem() {
        if (actorSystem == null) {
            final Config config = ConfigFactory.parseString(String.format(CommonTokens.TcpPortConifguration, "0"))
                    .withFallback(ConfigFactory.parseString(String.format(CommonTokens.TcpHostConfiguration, "127.0.0.1")))
                    .withFallback(ConfigFactory.load());
            actorSystem = ActorSystem.create(CommonTokens.ClusterName, config);
        }
        return actorSystem;
    }

    static ActorSelection getManager(String ip) {
        if (actorSystem == null) {
            getActorSystem();
        }
        return actorSystem.actorSelection(String.format(CommonTokens.INSTANCE.getFullManagerLocation(), ip));
    }
}
