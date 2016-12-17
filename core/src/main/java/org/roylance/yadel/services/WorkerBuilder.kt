package org.roylance.yadel.services

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory
import org.roylance.common.service.IBuilder
import org.roylance.yadel.YadelModel
import org.roylance.yadel.actors.WorkerBase
import org.roylance.yadel.enums.CommonTokens
import scala.Tuple2

class WorkerBuilder<T:WorkerBase>(
        private val hostName:String,
        private val port:String,
        private val workerClass:Class<T>): IBuilder<Tuple2<ActorSystem, ActorRef>> {

    override fun build(): Tuple2<ActorSystem, ActorRef> {
        val config = ConfigFactory.parseString(String.format(CommonTokens.TcpPortConifguration, port))
                .withFallback(ConfigFactory.parseString(String.format(CommonTokens.TcpHostConfiguration, hostName)))
                .withFallback(ConfigFactory.parseString(CommonTokens.WorkerConfiguration))
                .withFallback(ConfigFactory.load())

        val system = ActorSystem.create(CommonTokens.ClusterName, config)
        val returnTuple = Tuple2(
                system,
                system.actorOf(Props.create(this.workerClass), YadelModel.ActorRole.WORKER.name))


        return returnTuple
    }
}
