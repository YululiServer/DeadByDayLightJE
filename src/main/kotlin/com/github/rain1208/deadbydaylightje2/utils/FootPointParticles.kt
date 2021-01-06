package com.github.rain1208.deadbydaylightje2.utils

import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class FootPointParticles(private val game: Game): BukkitRunnable() {
    private val pointList = mutableMapOf<Player, Queue<Location>>()
    override fun run() {
        update()
        for ((player, locations) in pointList) {
            if (game.getSurvivor(player.uniqueId)?.isHooked!!) continue
            for (pos in locations) {
                for (killer in game.killers.values) {
                    if (pos.distance(killer.player.location) <= killer.footPointVisibleDistance) {
                        killer.player.spawnParticle(Particle.REDSTONE, pos, 2, 0.3, 0.2, 0.3)
                    }
                }
            }
        }
    }

    private fun update() {
        for ((player, locations) in pointList) {
            if (locations.size > game.getSurvivor(player.uniqueId)?.footPointLength!!)
                locations.poll()

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