package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class AWSDesiredCapacityBuilder(
        private val groupName: String,
        private val fileLocation: String
): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
aws autoscaling set-desired-capacity --auto-scaling-group-name $groupName  --desired-capacity $1
"""
        File(fileLocation).writeText(template)
        return true
    }
}