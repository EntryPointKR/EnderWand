package kr.entree.enderwand.bukkit.item

import kr.entree.enderwand.bukkit.message.colorize
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Created by JunHyung Lim on 2020-01-25
 */
inline fun ItemStack.meta(configure: ItemMeta.() -> Unit) = metaOf<ItemMeta>(configure)

inline fun <reified T : ItemMeta> ItemStack.metaOf(configure: T.() -> Unit) {
    val meta = itemMeta
    if (meta is T) {
        meta.configure()
        itemMeta = meta
    } else throw IllegalStateException("$type meta of item isn't ${T::class.java.simpleName}")
}

fun ItemMeta.setName(name: String) = setDisplayName(name.colorize())

fun ItemMeta.setLore(vararg lore: String) = setLore(lore.map { it.colorize() })

inline fun ItemMeta.lore(configure: MutableList<String>.() -> Unit) {
    lore = mutableListOf<String>().apply(configure).map { it.colorize() }
}

val ItemStack.displayName get() = itemMeta?.displayName ?: ""

val ItemMeta.modelData
    get() = if (hasCustomModelData()) {
        customModelData
    } else null