package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.Bukkit
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class Generator(val armorStand: ArmorStand) {
    var count = 0
    var isAlive = true

    fun baseTick(game: Game) {
        if (count >= 10) {
            isAlive = false
            armorStand.remove()
            Bukkit.broadcastMessage("発電が完了しました")
        } else {
            for (entity in armorStand.getNearbyEntities(4.0,10.0,4.0)) {
                if (entity !is Player) continue
                if (entity.isSneaking) game.getPlayer(entity)?.power(this)
            }
        }
    }
}