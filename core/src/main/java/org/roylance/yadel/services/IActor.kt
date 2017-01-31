package org.roylance.yadel.services

import akka.actor.ActorRef

interface IActor {
    fun forward(message: Any)
    fun tell(message: Any)
    fun key(): String
    fun host(): String
    fun port(): String
}