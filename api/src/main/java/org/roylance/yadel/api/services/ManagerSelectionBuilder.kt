package org.roylance.yadel.api.services

import akka.actor.ActorSelection
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.roylance.yadel.api.enums.CommonTokens
import scala.Tuple2

class ManagerSelectionBuilder<T: ActorSelection>(
        private val hostName:String,
        private val port:String):IBuilder<Tuple2<ActorSystem, ActorSelection>> {
    override fun build(): Tuple2<ActorSystem, ActorSelection> {
        val config = ConfigFactory.parseString(String.format(CommonTokens.TcpPortConifguration, this.port))
                .withFallback(ConfigFactory.parseString(String.format(CommonTokens.TcpHostConfiguration, this.hostName)))
                .withFallback(ConfigFactory.parseString(CommonTokens.ManagerConfiguration))
                .withFallback(ConfigFactory.load())

        val managerSystem = ActorSystem.create(CommonTokens.ClusterName, config)
        val fullManagerLocation = String.format(CommonTokens.FullManagerLocation, hostName) + CommonTokens.ManagerLocation

        val returnTuple = Tuple2(
                managerSystem,
                managerSystem.actorSelection(fullManagerLocation))
        return returnTuple
    }
}
