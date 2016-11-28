package org.roylance.yadel

import org.roylance.yadel.api.services.IDagStore
import org.roylance.yaorm.YaormModel
import org.roylance.yaorm.services.proto.IEntityMessageService
import org.roylance.yaorm.utilities.YaormUtils

class EntityDagStore(private val entityMessageService: IEntityMessageService) : IDagStore {
    override fun containsKey(key: String): Boolean {
        return getDags(key).isNotEmpty()
    }

    override fun get(key: String): YadelModel.Dag.Builder? {
        val foundDags = getDags(key)

        if (foundDags.isEmpty()) {
            return null
        }
        return foundDags.first().toBuilder()
    }

    override fun remove(key: String): Boolean {
        val tempModel = YadelModel.Dag.newBuilder().setId(key).build()
        return entityMessageService.delete(tempModel)
    }

    override fun set(key: String, value: YadelModel.Dag.Builder) {
        value.id = key

        entityMessageService.merge(value.build())
    }

    override fun size(): Int {
        return entityMessageService.getCount(YadelModel.Dag.getDefaultInstance()).toInt()
    }

    override fun values(): MutableCollection<YadelModel.Dag.Builder> {
        return entityMessageService.getMany(YadelModel.Dag.getDefaultInstance()).map { it.toBuilder() }.toMutableList()
    }

    private fun getDags(key: String): List<YadelModel.Dag> {
        val columnDefinition = YaormUtils.buildIdColumn(key)
        val whereClause = YaormModel.WhereClause.newBuilder()
                .setNameAndProperty(columnDefinition)
                .build()

        return entityMessageService.where(YadelModel.Dag.getDefaultInstance(), whereClause)
    }
}