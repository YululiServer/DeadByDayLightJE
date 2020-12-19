package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound

class Generator(private val pos: Location) {
    var occupancyRate: Double = 0.0
    var isAlive = true

    fun baseTick(game: Game) {
        if (!isAlive) return

        sendParticle()
        val nearPlayer = Bukkit.getOnlinePlayers().filter { pos.distance(it.location) <= 1.5 }
        for (entity in nearPlayer) {
            if (entity.isSneaking) {
                game.getPlayer(entity)?.onUse(this)
            }
            var n = (occupancyRate / 10).toInt()
            if (n >= 10) n = 10
            if (n <= 0) n = 0
            val msg = StringBuilder("修理率 :" + "■".repeat(n) + "□".repeat(10 - n)).toString()
            entity.sendTitle("", msg, 0, 20, 0)
        }

        if (occupancyRate >= 100) {
            for (player in nearPlayer) {
                if (game.getSurvivors().contains(player)) {
                    game.survivor[player.name]?.repairComplete()
                }
            }
            isAlive = false
            game.generatorCount--
            pos.world.playSound(pos, Sound.BLOCK_ANVIL_USE, 3f, 1f)
            pos.world.spawnParticle(Particle.CRIT_MAGIC, pos, 1)
            Bukkit.broadcastMessage("発電が完了しました")
            return
        }
    }

    private fun sendParticle() =
        pos.world.spawnParticle(Particle.PORTAL, pos,5,0.1,0.0,0.1)

    fun onBreak(breakAbility: Double) {
        pos.world.playSound(pos, Sound.ENTITY_GENERIC_EXPLODE, 2f, 1f)
        pos.world.spawnParticle(Particle.EXPLOSION_HUGE, pos, 1)
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