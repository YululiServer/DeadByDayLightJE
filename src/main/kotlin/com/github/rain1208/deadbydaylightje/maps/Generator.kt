package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class Generator(val armorStand: ArmorStand) {
    var occupancyRate:Double = 0.0
    var isAlive = true

    init {
        armorStand.isVisible = false
    }

    fun baseTick(game: Game) {
        if (!isAlive) return

        sendParticle()
        val nearPlayer = armorStand.getNearbyEntities(1.5,2.0,1.5).filterIsInstance<Player>()
        for (entity in nearPlayer) {
            if (entity.isSneaking) game.getPlayer(entity)?.onUse(this)
        }

        if (occupancyRate >= 100) {
            for (player in nearPlayer) {
                if (game.getSurvivors().contains(player)) {
                    game.survivor[player.name]?.repairComplete()
                }
            }
            isAlive = false
            armorStand.remove()
            armorStand.health = 0.0
            Bukkit.broadcastMessage("発電が完了しました")
            return
        }
    }

    fun sendParticle() {
        armorStand.world.spawnParticle(Particle.PORTAL,armorStand.location.add(0.0,0.5,0.0),1)
    }

    fun onActivate(repairAbility: Double) {
        occupancyRate += repairAbility
    }
}