package kr.entree.enderwand.bukkit.view

import kr.entree.enderwand.bukkit.enderWand
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryEvent
import java.util.*

/**
 * Created by JunHyung Lim on 2020-01-04
 */
fun HumanEntity.openView(view: View) =
    enderWand.viewManager.open(this, view)

infix fun View.openTo(player: HumanEntity) = player.openView(this)

class ViewManager : Listener {
    val handlerMap = mutableMapOf<UUID, View>()

    fun open(player: HumanEntity, view: View) =
        view.create().apply {
            player.openInventory(this)
            handlerMap[player.uniqueId] = view
        }

    fun notify(e: InventoryEvent) = handlerMap[e.view.player.uniqueId]?.onEvent(e)

    @EventHandler
    fun onClick(e: InventoryClickEvent) = notify(e)

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        notify(e)
        handlerMap.remove(e.player.uniqueId)
    }

    @EventHandler
    fun onDrag(e: InventoryDragEvent) = notify(e)
}