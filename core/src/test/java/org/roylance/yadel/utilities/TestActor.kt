package org.roylance.yadel.utilities

import org.roylance.common.service.ILogger
import org.roylance.yadel.services.IActor

class TestActor(private val name: String,
                private val logger: ILogger): IActor {
    var forwardCalled = 0
    var tellCalled = 0

    override fun forward(message: Any) {
        logger.info("forwarding $message")
        forwardCalled++
    }

    override fun tell(message: Any) {
        logger.info("telling $message")
        tellCalled++
    }

    override fun key(): String {
        return name
    }

    override fun host(): String {
        return name
    }

    override fun port(): String {
        return name
    }
}