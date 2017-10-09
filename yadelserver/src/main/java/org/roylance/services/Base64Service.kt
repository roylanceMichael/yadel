// This file was auto-generated, but can be altered. It will not be overwritten.
package org.roylance.services

import org.roylance.common.service.IBase64Service
import java.util.*

class Base64Service: IBase64Service {
    override fun deserialize(string64: String): ByteArray {
        return Base64.getDecoder().decode(string64)
    }

    override fun serialize(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }
}
