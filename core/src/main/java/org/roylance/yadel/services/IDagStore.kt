package org.roylance.yadel.services

import org.roylance.yadel.YadelModel

interface IDagStore {
    fun values(): MutableCollection<YadelModel.Dag.Builder>
    fun get(key: String): YadelModel.Dag.Builder?
    fun set(key: String, value: YadelModel.Dag.Builder)
    fun containsKey(key: String): Boolean
    fun remove(key: String): Boolean
    fun size(): Int
}