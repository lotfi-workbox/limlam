package com.saeedlotfi.limlam.userInterface._common

import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*

@Suppress("UNCHECKED_CAST")
class DeepRecursiveScope<T, R>(
    block: suspend DeepRecursiveScope<T, R>.(T) -> R,
    value: T
) : Continuation<R> {
    private val function = block as Function3<Any?, Any?, Continuation<R>, Any?>
    private var result: Result<R> = Result.success(null) as Result<R>
    private var value: Any? = value
    private var cont: Continuation<R>? = this

    suspend fun callRecursive(value: T): R {
        return suspendCoroutineUninterceptedOrReturn { cont ->
            this.cont = cont
            this.value = value
            COROUTINE_SUSPENDED
        }
    }

    fun runCallLoop(onComplete : () -> Unit): R {
        while (true) {
            val result = this.result
            val cont = this.cont // null means done
                ?: return result.getOrThrow()
            // ~startCoroutineUninterceptedOrReturn
            val r = try {
                function(this, value, cont)
            } catch (e: Throwable) {
                cont.resumeWithException(e)
                continue
            }
            if (r !== COROUTINE_SUSPENDED) {
                cont.resume(r as R)
                if (this.cont == null){
                    onComplete()
                }
            }
        }
    }

    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<R>) {
        this.cont = null
        this.result = result
    }
}

class DeepRecursiveFunction<T, R>(
    val block: suspend DeepRecursiveScope<T, R>.(T) -> R
)

operator fun <T, R> DeepRecursiveFunction<T, R>.invoke(value: T, onComplete : () -> Unit): R =
    DeepRecursiveScope(block, value).runCallLoop(onComplete)