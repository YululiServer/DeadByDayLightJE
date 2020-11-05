package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
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
            if (entity.isSneaking) {
                game.getPlayer(entity)?.onUse(this)
            } else {
                val n = (occupancyRate / 10).toInt()
                val msg = StringBuilder("修理率 :"+"■".repeat(n)+"□".repeat(10 - n)).toString()
                entity.sendTitle("",msg,0,25,0)
            }
        }

        if (occupancyRate >= 100) {
            for (player in nearPlayer) {
                if (game.getSurvivors().contains(player)) {
                    game.survivor[player.name]?.repairComplete()
                    game.generatorCount--
                }
            }
            isAlive = false
            armorStand.remove()
            armorStand.health = 0.0
            armorStand.world.spawnParticle(Particle.FIREWORKS_SPARK,armorStand.location,1)
            Bukkit.broadcastMessage("発電が完了しました")
            return
        }
    }

    private fun sendParticle() {
        armorStand.world.spawnParticle(Particle.PORTAL,armorStand.location.add(0.0,0.5,0.0),5)
    }

    fun onBreak(breakAbility: Double) {
        armorStand.world.playSound(armorStand.location, Sound.ENTITY_GENERIC_EXPLODE,2f,1f)
        armorStand.world.spawnParticle(Particle.EXPLOSION_HUGE,armorStand.location,1)
        if (occupancyRate - breakAbility <= 0) {
            occupancyRate = 0.0
        } else {
            occupancyRate -= breakAbility
        }
    }

    fun onActivate(repairAbility: Double) {
        occupancyRate += repairAbility
    }

}