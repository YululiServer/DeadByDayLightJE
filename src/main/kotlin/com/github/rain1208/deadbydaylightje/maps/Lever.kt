package com.github.rain1208.deadbydaylightje.maps

import org.bukkit.Bukkit
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class Lever(val armorStand: ArmorStand) {
    var occupancyRate:Double = 0.0
    var isAlive = true

    init {
        armorStand.isVisible = false
    }

    fun baseTick() {
        if (!isAlive) return

        val nearPlayer = armorStand.getNearbyEntities(1.5,2.0,1.5).filterIsInstance<Player>()
        for (entity in nearPlayer) {
            if (entity.isSneaking) {
                occupancyRate++
            }
        }
        if (occupancyRate >= 50) {
            Bukkit.broadcastMessage("ゲートが通電しました")
            armorStand.remove()
            isAlive = false
        }
    }
}