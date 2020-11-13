package com.github.rain1208.deadbydaylightje.game

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class FootPointParticles(val game: Game): BukkitRunnable() {
    private val pointList = mutableMapOf<Player,Queue<Location>>()
    override fun run() {
        update()
        for ((_, locations) in pointList) {
            for (pos in locations) {
                for (killer in game.getKillers()) {
                    killer.spawnParticle(Particle.DRIP_LAVA,pos,10,0.3,0.2,0.3)
                }
            }
        }
    }

    fun update() {
        for ((player,locations) in pointList) {
            if (locations.size > 50) {
                locations.poll()
            }
            locations.add(player.location)
        }
    }

    fun addPlayer(player: Player) {
        pointList[player] = ArrayDeque()
    }

    fun removePlayer(player: Player) {
        pointList.remove(player)
    }
}