package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class RunStopBuilder(private val fileLocation: String,
                     private val portsToStop: List<Int> = listOf(2234, 2344, 2345, 2346)): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
${portsToStop.map { buildKillPortTemplate(it) }.joinToString("\n")}
"""
        File(fileLocation).delete()
        File(fileLocation).writeText(template)
        return true
    }

    private fun buildKillPortTemplate(port: Int): String {
        return "kill -9 $(lsof -i:$port -t)"
    }
}