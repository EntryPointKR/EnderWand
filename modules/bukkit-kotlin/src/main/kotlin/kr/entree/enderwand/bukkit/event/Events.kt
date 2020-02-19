package kr.entree.enderwand.bukkit.event

import kr.entree.enderwand.bukkit.scheduler.scheduler
import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.Plugin

/**
 * Created by JunHyung Lim on 2020-01-06
 */
inline val Cancellable.isNotCancelled get() = !isCancelled

fun Event.findPlayer() =
    when (this) {
        is PlayerEvent -> {
            player
        }
        is BlockPlaceEvent -> {
            player
        }
        is BlockBreakEvent -> {
            player
        }
        else -> null
    }

inline fun <reified T : Event> Plugin.on(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline receiver: T.(FunctionalEventExecutor) -> Unit
) =
    FunctionalEventExecutor(ignoreCancelled) { _, event ->
        if (event is T) {
            if (priority == EventPriority.MONITOR) {
                scheduler.run { receiver(event, this) }
            } else {
                receiver(event, this)
            }
        }
    }.also { executor ->
        Bukkit.getPluginManager()
            .registerEvent(T::class.java, executor, priority, executor, this)
    }