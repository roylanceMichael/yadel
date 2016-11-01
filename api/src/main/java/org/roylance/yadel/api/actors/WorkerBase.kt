package org.roylance.yadel.api.actors

import akka.actor.ActorSelection
import akka.actor.UntypedActor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import akka.cluster.Member
import akka.cluster.MemberStatus
import akka.event.Logging
import akka.event.LoggingAdapter
import org.roylance.yadel.YadelModel
import org.roylance.yadel.api.enums.CommonTokens
import org.roylance.yadel.api.services.ITaskLogger
import java.lang.management.ManagementFactory
import java.util.*

abstract class WorkerBase: UntypedActor() {
    protected val cluster: Cluster = Cluster.get(this.context.system())
    protected val log: LoggingAdapter = Logging.getLogger(this.context().system(), this)

    protected var foundManagerAddress:String? = null

    protected val logger = object: ITaskLogger {
        override val logs = ArrayList<YadelModel.Log>()

        override fun clearLogs() {
            logs.clear()
        }

        override fun error(message: String) {
            log.error(message)
            logs.add(YadelModel.Log.newBuilder().setId(UUID.randomUUID().toString()).setMessage(message).build())
        }

        override fun info(message: String) {
            log.info(message)
            logs.add(YadelModel.Log.newBuilder().setId(UUID.randomUUID().toString()).setMessage(message).build())
        }
    }

    override fun preStart() {
        log.info("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
        super.preStart()
        this.cluster.subscribe(this.self, ClusterEvent.MemberUp::class.java)
    }

    override fun postStop() {
        logger.info("handling postStop on base")
        super.postStop()
        this.cluster.unsubscribe(this.self)
    }

    override fun onReceive(p0: Any?) {
        logger.clearLogs()
        logger.info("received message")
        if (p0 is ClusterEvent.CurrentClusterState) {
            logger.info("handling current cluster state")
            this.handleCurrentClusterState(p0)
        }
        else if (p0 is ClusterEvent.MemberUp) {
            logger.info("handling member up")
            this.handleMemberUp(p0)
        }
        // inherited class will catch work to be done
    }

    protected fun completeTask(completeTask: YadelModel.CompleteTask) {
        getManagerSelection()?.tell(completeTask, self)
    }

    protected fun getManagerSelection():ActorSelection? {
        if (foundManagerAddress == null) {
            return null
        }
        return context
                .actorSelection(foundManagerAddress + CommonTokens.ManagerLocation)
    }

    private fun handleCurrentClusterState(state:ClusterEvent.CurrentClusterState) {
        state.members.forEach {
            if (it.status() == MemberStatus.up()) {
                this.register(it)
            }
        }
    }

    private fun handleMemberUp(memberUp:ClusterEvent.MemberUp) {
        this.register(memberUp.member())
    }

    private fun register(member: Member) {
        if (member.hasRole(YadelModel.ActorRole.MANAGER.name)) {
            this.foundManagerAddress = member.address().toString()
            this.getManagerSelection()
                    ?.tell(
                            YadelModel.WorkerToManagerMessageType.REGISTRATION,
                            this.self)
        }
    }
}
