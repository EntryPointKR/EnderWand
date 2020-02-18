package kr.entree.enderwand.bukkit

import kr.entree.enderwand.bukkit.inventory.slots
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Created by JunHyung Lim on 2020-02-18
 */
class SlotPatternTest {
    @Test
    fun `slots pattern`() {
        assertEquals(
            (1 until 9 * 2 - 1).toList(),
            slots(
                "012345678",
                "9abcdefg0"
            )
        )
    }
}