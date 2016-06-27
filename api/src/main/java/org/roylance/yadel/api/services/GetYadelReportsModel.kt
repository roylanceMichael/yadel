package org.roylance.yadel.api.services

import org.apache.commons.io.IOUtils
import java.io.StringWriter
import java.nio.charset.Charset

class GetYadelReportsModel:IBuilder<String> {
    override fun build(): String {
        val writer = StringWriter()
        val stream = GetYadelReportsModel::class.java.getResourceAsStream("/YadelReports.proto")
        IOUtils.copy(stream, writer, Charset.defaultCharset())
        return writer.toString()
    }
}
