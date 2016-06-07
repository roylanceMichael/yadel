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
import java.io.File
import java.net.InetAddress
import java.nio.file.Path
import java.nio.file.Paths

abstract class WorkerBase: UntypedActor() {
    protected val cluster = Cluster.get(this.context.system())
    protected val log = Logging.getLogger(this.context().system(), this)

    protected var foundManagerAddress:String? = null

    override fun preStart() {
        this.log.info("handling prestart on base")
        super.preStart()
        this.cluster.subscribe(this.self, ClusterEvent.MemberUp::class.java)
    }

    override fun postStop() {
        this.log.info("handling poststop on base")
        super.postStop()
        this.cluster.unsubscribe(this.self)
    }

    override fun onReceive(p0: Any?) {
        this.log.info("received message $p0")
        if (p0 is ClusterEvent.CurrentClusterState) {
            this.log.info("handling current cluster state")
            this.handleCurrentClusterState(p0)
        }
        else if (p0 is ClusterEvent.MemberUp) {
            this.log.info("handling member up")
            this.handleMemberUp(p0)
        }
        else if (p0 is YadelModels.ManagerToWorkerMessage) {
            if (YadelModels.ManagerToWorkerMessageType.KILL_YOURSELF.equals(p0.type)) {
                this.log.info("killing myself")
                System.exit(0)
            }
            else if (YadelModels.ManagerToWorkerMessageType.SPAWN_NEW_WORKER_AT_YOUR_LOCATION.equals(p0.type)) {
                val currentDir = System.getProperty("user.dir")
                val bashCommand = "/bin/bash"
                val runFile = Paths.get(currentDir, "run.sh").toString()

                val ip = InetAddress.getLocalHost().hostAddress
                this.log.info("spawning another with $bashCommand $runFile $ip ${CommonTokens.Zero} ${p0.spawnMessage.seedHost1} ${p0.spawnMessage.seedPort1} ${p0.spawnMessage.seedHost2} ${p0.spawnMessage.seedPort2}")
                val pb = ProcessBuilder().command(
                        bashCommand,
                        runFile,
                        ip,
                        CommonTokens.Zero,
                        p0.spawnMessage.seedHost1,
                        p0.spawnMessage.seedPort1,
                        p0.spawnMessage.seedHost2,
                        p0.spawnMessage.seedPort2)
                pb.inheritIO()
                pb.start()
            }
        }

        // inherited class will catch work to be done
    }

    protected fun finishWorking(message: YadelModels.WorkerToManagerMessage.Builder) {
        this.getFrontendActorSelection()?.tell(message.setType(YadelModels.WorkerToManagerMessageType.NOW_IDLE).build(), this.self)
    }

    protected fun startWorking(message: YadelModels.WorkerToManagerMessage.Builder) {
        this.getFrontendActorSelection()?.tell(message.setType(YadelModels.WorkerToManagerMessageType.NOW_WORKING).build(), this.self)
    }

    protected fun getFrontendActorSelection():ActorSelection? {
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
            this.getFrontendActorSelection()
                    ?.tell(
                            YadelModels.WorkerToManagerMessage.newBuilder().setType(YadelModels.WorkerToManagerMessageType.REGISTRATION).build(),
                            this.self)
        }
    }
}
