package org.roylance.yadel.api.utilities

import akka.actor.ActorRef

object ActorIpUtilities {
    const private val ForwardSlash = "/"
    const private val AtMark = "@"
    const private val EmptyString = ""

    fun getIpFromActorAddress(actor:ActorRef):String? {
        val actorAddress = actor.path().address().toString()

        val start = actorAddress.indexOf(ForwardSlash)
        val end = actorAddress.indexOf(AtMark)

        if (start < 0 || end < 0) {
            return null
        }
        return actorAddress.substring(start, end)
    }
}
