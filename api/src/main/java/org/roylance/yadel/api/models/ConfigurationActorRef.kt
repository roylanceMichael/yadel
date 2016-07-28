package org.roylance.yadel.api.models

import akka.actor.ActorRef
import org.roylance.yadel.YadelModel

class ConfigurationActorRef(
        val actorRef: ActorRef,
        val configuration: YadelModel.WorkerConfiguration)
