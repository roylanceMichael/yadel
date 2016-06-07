package org.roylance.yadel.api.models

import akka.actor.ActorRef

class ConfigurationActorRef(
        val actorRef: ActorRef,
        val configuration:YadelModels.WorkerConfiguration)
