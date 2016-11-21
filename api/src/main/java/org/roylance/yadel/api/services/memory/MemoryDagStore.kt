package org.roylance.yadel.api.services.memory

import org.roylance.yadel.YadelModel
import org.roylance.yadel.api.services.IDagStore
import java.util.*

class MemoryDagStore: IDagStore {
    private val activeDags = HashMap<String, YadelModel.Dag.Builder>()

    override fun get(key: String): YadelModel.Dag.Builder? {
        if (activeDags.containsKey(key)) {
            return activeDags[key]!!
        }
        return null
    }

    override fun set(key: String, value: YadelModel.Dag.Builder) {
        activeDags[key] = value
    }

    override fun containsKey(key: String): Boolean {
        return activeDags.containsKey(key)
    }

    override fun values(): MutableCollection<YadelModel.Dag.Builder> {
        return activeDags.values
    }

    override fun remove(key: String): Boolean {
        if (activeDags.containsKey(key)) {
            activeDags.remove(key)
            return true
        }
        return false
    }

    override fun size(): Int {
        return activeDags.size
    }
}