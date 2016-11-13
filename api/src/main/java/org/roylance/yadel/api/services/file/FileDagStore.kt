package org.roylance.yadel.api.services.file

import org.roylance.yadel.YadelModel
import org.roylance.yadel.api.services.IDagStore
import java.io.File
import java.util.*

class FileDagStore(private val fileLocation: String) : IDagStore {
    override fun get(key: String): YadelModel.Dag.Builder? {
        val dagFile = File(fileLocation)
        if (!dagFile.exists()) {
            return null
        }

        val allBytes = dagFile.readBytes()
        val allDags = YadelModel.AllDags.parseFrom(allBytes)
        return allDags.dagsList.firstOrNull { dag -> dag.id == key }?.toBuilder()
    }

    override fun remove(key: String): Boolean {
        val dagFile = File(fileLocation)
        if (!dagFile.exists()) {
            return false
        }
        else {
            val existingDags = YadelModel.AllDags.parseFrom(dagFile.readBytes()).toBuilder()

            val foundExisting = existingDags.dagsList.firstOrNull { dag -> dag.id == key }
            if (foundExisting == null) {
                return false
            }
            else {
                val indexToRemove = existingDags.dagsList.indexOf(foundExisting)
                existingDags.removeDags(indexToRemove)
            }

            dagFile.writeBytes(existingDags.build().toByteArray())
            return true
        }
    }

    override fun values(): MutableCollection<YadelModel.Dag.Builder> {
        val dagFile = File(fileLocation)
        if (!dagFile.exists()) {
            return ArrayList()
        }

        val allBytes = dagFile.readBytes()
        val allDags = YadelModel.AllDags.parseFrom(allBytes)
        val returnList = ArrayList<YadelModel.Dag.Builder>()

        allDags.dagsList.forEach { dag ->
            returnList.add(dag.toBuilder())
        }

        return returnList
    }

    override fun set(key: String, value: YadelModel.Dag.Builder) {
        val dagFile = File(fileLocation)
        if (!dagFile.exists()) {
            val newDag = YadelModel.AllDags.newBuilder().addDags(value).build()
            dagFile.writeBytes(newDag.toByteArray())
        }
        else {
            val existingDags = YadelModel.AllDags.parseFrom(dagFile.readBytes()).toBuilder()

            val foundExisting = existingDags.dagsList.firstOrNull { dag -> dag.id == key }
            if (foundExisting == null) {
                existingDags.addDags(value)
            }
            else {
                val indexToRemove = existingDags.dagsList.indexOf(foundExisting)
                existingDags.removeDags(indexToRemove)
                existingDags.addDags(value)
            }

            dagFile.writeBytes(existingDags.build().toByteArray())
        }
    }

    override fun containsKey(key: String): Boolean {
        val dagFile = File(fileLocation)
        if (!dagFile.exists()) {
            return false
        }
        val existingDags = YadelModel.AllDags.parseFrom(dagFile.readBytes()).toBuilder()
        val foundExisting = existingDags.dagsList.firstOrNull { dag -> dag.id == key }
        return foundExisting != null
    }
}