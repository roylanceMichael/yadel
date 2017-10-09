package org.roylance.yadel.models

import org.roylance.yadel.YadelModel
import org.roylance.yadel.services.IActor

class ConfigurationActorRef(
        val actorRef: IActor,
        val configuration: YadelModel.WorkerConfiguration)
