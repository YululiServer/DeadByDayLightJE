package com.github.rain1208.deadbydaylightje2.characters.killerTypes

import org.bukkit.inventory.PlayerInventory

interface KillerType {
    val name: String
    val inventory: PlayerInventory
}