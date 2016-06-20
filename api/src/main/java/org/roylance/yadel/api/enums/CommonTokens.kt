package org.roylance.yadel.api.enums

import org.roylance.yadel.api.models.YadelModels

object CommonTokens {
    const val ClusterName = "YadelCluster"

    const val Zero = "0"
    const val One = "1"
    const val LocalHost = "127.0.0.1"
    const val SeedNodesConfiguration = "akka.cluster.seed-roles.%s=%s"
    const val SeedNodeTemplate = "\"akka.ssl.tcp://$ClusterName@%s:%s\""

    const val TcpHostConfiguration = "akka.remote.netty.ssl.hostname=%s"
    const val TcpPortConifguration = "akka.remote.netty.ssl.port=%s"
    const val TwoThreeFourFour = "2344"
    const val TwoThreeFourFive = "2345"
    const val TwoThreeFourSix = "2346"

    val ManagerConfiguration = "akka.cluster.roles = [${YadelModels.ActorRole.MANAGER.name}]"
    val WorkerConfiguration = "akka.cluster.roles = [${YadelModels.ActorRole.WORKER.name}]"
    val ManagerLocation = "/user/${YadelModels.ActorRole.MANAGER.name}"
    val WorkerLocation = "/user/${YadelModels.ActorRole.WORKER.name}"

    val FullManagerLocation = "akka.ssl.tcp://$ClusterName@%s:$TwoThreeFourFour$ManagerLocation"
    val FullFirstSeedNodeWorkerLocation = "akka.ssl.tcp://$ClusterName@%s:$TwoThreeFourFive"
    val FullSecondSeedNodeWorkerLocation = "akka.ssl.tcp://$ClusterName@%s:$TwoThreeFourSix"
}