package org.roylance.yadel.api.utilities

import akka.actor.ActorRef
import org.roylance.yadel.api.enums.CommonTokens
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

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

    fun getCurrentIp():String {
        return InetAddress.getLocalHost().hostAddress
    }

    fun getNonLocalhostIPAddress(seedHostIp:String):String? {
        val ipScoreMap = HashMap<String, Double>()

        try {
            val currentIp = InetAddress.getLocalHost().hostAddress.trim()
            if (!CommonTokens.LocalHost.equals(currentIp) && currentIp.indexOf(":") == -1) {
                val distance = StringUtilities.editDistance(seedHostIp, currentIp)
                ipScoreMap[currentIp] = distance
            }
        }
        catch(e: Exception) {
            System.out.println("had a difficult time getting the local host address, but looking at other addresses now...")
        }

        try
        {
            val items = NetworkInterface.getNetworkInterfaces()
            while (items.hasMoreElements()) {
                val networkInterface = items.nextElement()

                val addresses = networkInterface.inetAddresses
                while(addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    val hostAddress = address.hostAddress.trim()
                    if (!CommonTokens.LocalHost.equals(hostAddress) && hostAddress.indexOf(":") == -1) {
                        val distanceToAddress = StringUtilities.editDistance(seedHostIp, hostAddress)
                        ipScoreMap[hostAddress] = distanceToAddress
                    }
                }
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
            System.out.println(e.message)
        }

        var lowestScore = Double.MAX_VALUE
        var lowestKey:String? = null
        ipScoreMap.keys.forEach {
            if (ipScoreMap[it]!! < lowestScore) {
                lowestKey = it
                lowestScore = ipScoreMap[it]!!
            }
        }

        return lowestKey
    }
}
