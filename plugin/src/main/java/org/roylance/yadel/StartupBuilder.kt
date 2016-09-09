package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class StartupBuilder(private val autoscaleTarFullUrl: String,
                     private val fileLocation: String): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
curl -k $autoscaleTarFullUrl -o ${CommonStringsHelper.ServerTarName}
tar -xvf ${CommonStringsHelper.ServerTarName}
bash ${CommonStringsHelper.AutoScalingName}
"""
        File(fileLocation).writeText(template)
        return true
    }
}