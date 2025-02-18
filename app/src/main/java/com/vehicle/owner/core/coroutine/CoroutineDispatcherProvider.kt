package com.vehicle.owner.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

interface CoroutineDispatcherProvider {
    val dispatcherDefault: CoroutineDispatcher
    val dispatcherIO: CoroutineDispatcher
    suspend fun <T> switchToDefault(block: suspend CoroutineScope.() -> T): T
    suspend fun <T> switchToIO(block: suspend CoroutineScope.() -> T): T
}
