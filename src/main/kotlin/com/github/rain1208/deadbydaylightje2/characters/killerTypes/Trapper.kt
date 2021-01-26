package com.github.rain1208.deadbydaylightje2.characters.killerTypes

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.utils.ConfigManager
import com.github.rain1208.deadbydaylightje2.utils.InventorySerialization
import org.bukkit.inventory.PlayerInventory

class Trapper : KillerType {
    override val name = "trapper"
    override val inventory: PlayerInventory

    init {
        val configManager = DeadByDayLightJE2.instance.configManager
        val data = configManager.get(ConfigManager.KILLER_DATA).config.getStringList(name)
        inventory = InventorySerialization.playerInventoryFromBase64(data.toTypedArray())
    }
}