package org.roylance.yadel.api.actors

import akka.actor.ActorSelection
import akka.actor.UntypedActor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import akka.cluster.Member
import akka.cluster.MemberStatus
import akka.event.Logging
import org.roylance.yadel.api.enums.CommonTokens
import org.roylance.yadel.api.models.YadelModels
import java.lang.management.ManagementFactory

abstract class WorkerBase: UntypedActor() {
    protected val cluster = Cluster.get(this.context.system())
    protected val log = Logging.getLogger(this.context().system(), this)

    protected var foundManagerAddress:String? = null

    override fun preStart() {
        System.out.println("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
        super.preStart()
        this.cluster.subscribe(this.self, ClusterEvent.MemberUp::class.java)
    }

    override fun postStop() {
        this.log.info("handling postStop on base")
        super.postStop()
        this.cluster.unsubscribe(this.self)
    }

    override fun onReceive(p0: Any?) {
        this.log.info("received message")
        if (p0 is ClusterEvent.CurrentClusterState) {
            this.log.info("handling current cluster state")
            this.handleCurrentClusterState(p0)
        }
        else if (p0 is ClusterEvent.MemberUp) {
            this.log.info("handling member up")
            this.handleMemberUp(p0)
        }
        // inherited class will catch work to be done
    }

    protected fun completeTask(completeTask: YadelModels.CompleteTask) {
        this.getManagerSelection()?.tell(completeTask, this.self)
    }

    protected fun getManagerSelection():ActorSelection? {
        if (this.foundManagerAddress == null) {
            return null
        }
        return this.context
                .actorSelection(this.foundManagerAddress + CommonTokens.ManagerLocation)
    }

    private fun handleCurrentClusterState(state:ClusterEvent.CurrentClusterState) {
        state.members.forEach {
            if (it.status().equals(MemberStatus.up())) {
                this.register(it)
            }
        }
    }

    private fun handleMemberUp(memberUp:ClusterEvent.MemberUp) {
        this.register(memberUp.member())
    }

    private fun register(member: Member) {
        if (member.hasRole(YadelModels.ActorRole.MANAGER.name)) {
            this.foundManagerAddress = member.address().toString()
            this.getManagerSelection()
                    ?.tell(
                            YadelModels.WorkerToManagerMessageType.REGISTRATION,
                            this.self)
        }
    }
}
