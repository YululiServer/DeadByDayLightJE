package com.github.rain1208.deadbydaylightje2.maps.resource

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.characters.Survivor
import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable

class Hook(override val pos: Location): Usable {
    override var occupancyRate: Double = 0.0
    override var isAlive: Boolean = true
    override val range: Double = 2.0

    var player: MutableList<Survivor> = mutableListOf()

    override fun baseTick(game: Game) {
        val nearPlayer = Bukkit.getOnlinePlayers().filter { pos.distance(it.location) <= range }
        for (player in nearPlayer) {
            if (player.isSneaking) {
                game.getSurvivor(player.uniqueId)?.onUse(this)
            }
        }
        for (hookSurvivor in player) {
        }
    }



    fun breakHook() {
        isAlive = false
        object : BukkitRunnable() {
            override fun run() {
                isAlive = true
            }
        }.runTaskLaterAsynchronously(DeadByDayLightJE2.instance, 200)
    }

    override fun repairComplete(game: Game) {
        TODO("Not yet implemented")
    }

    fun setHook(survivor: Survivor) {
        player.add(survivor)
        survivor.isHooked = true
        survivor.player.teleport(pos)
    }
}