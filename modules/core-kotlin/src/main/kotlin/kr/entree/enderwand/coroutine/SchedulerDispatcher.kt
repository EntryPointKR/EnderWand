package kr.entree.enderwand.coroutine

import kotlinx.coroutines.*
import kr.entree.enderwand.scheduler.Scheduler
import kr.entree.enderwand.time.milliseconds
import kotlin.coroutines.CoroutineContext

/**
 * Created by JunHyung Lim on 2020-01-09
 */
@UseExperimental(InternalCoroutinesApi::class)
class SchedulerDispatcher(private val scheduler: Scheduler) : MainCoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        scheduler.run { block.run() }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = scheduler.runLater(timeMillis.milliseconds) {
            continuation.apply {
                resumeUndispatched(Unit)
            }
        }
        continuation.invokeOnCancellation { task.cancel() }
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !scheduler.isPrimaryThread()
    }

    override val immediate: MainCoroutineDispatcher
        get() = this
}