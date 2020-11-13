package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class Lever(val armorStand: ArmorStand) {
    private var occupancyRate:Double = 0.0
    var isAlive = true

    init {
        armorStand.isVisible = false
    }

    fun baseTick(game:Game) {
        if (!isAlive) return
        val nearPlayer = armorStand.getNearbyEntities(1.5,2.0,1.5).filterIsInstance<Player>()
        for (entity in nearPlayer) {
            if (entity.isSneaking) {
                val n = (occupancyRate / 10).toInt()
                val msg = StringBuilder("修理率 :"+"■".repeat(n)+"□".repeat(5 - n)).toString()
                entity.sendTitle("",msg,0,20,0)
                occupancyRate += 5 //TODO("修理 カウント")
                break
            }
        }

        if (occupancyRate >= 50) {
            game.leverActivate()
            armorStand.remove()
            isAlive = false
        }
    }
}