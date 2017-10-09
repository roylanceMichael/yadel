package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class ServiceBuilder(private val projectName: String,
                     private val fileLocation: String): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
# /etc/init.d/$projectName

touch /var/lock/$projectName

case "$1" in
  start)
    bash /usr/sbin/${CommonStringsHelper.buildStartServerName(projectName)}
    ;;
  stop)
    bash /usr/sbin/${CommonStringsHelper.buildStopName(projectName)}
    ;;
  *)
    echo "Usage: /etc/init.d/$projectName {start|stop}"
    exit 1
    ;;
esac

exit 0
"""
        File(fileLocation).delete()
        File(fileLocation).writeText(template)
        return true
    }
}