package com.github.rain1208.deadbydaylightje2.maps.resource

import com.github.rain1208.deadbydaylightje2.characters.Survivor
import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class Hook(override val pos: Location): GameResource {
    override var isAlive: Boolean = true
    var range = 2.0

    private val players: MutableMap<UUID,Survivor> = mutableMapOf()
    private val rescuePlayer: MutableMap<UUID,UUID> = mutableMapOf()

    override fun baseTick(game: Game) {
        if (players.isEmpty()) return

        val nearPlayer = Bukkit.getOnlinePlayers().filter { pos.distance(it.location) <= range }.filterNot { players.contains(it.uniqueId) }

        for (player in nearPlayer) {
            if (player.isSneaking) {
                game.getSurvivor(player.uniqueId)?.onUse(this)
            }
        }
    }

    fun rescue(player: Player) {

    }

    /*fun breakHook() {
        isAlive = false
        object : BukkitRunnable() {
            override fun run() {
                isAlive = true
            }
        }.runTaskLaterAsynchronously(DeadByDayLightJE2.instance, 200)
    }*/

    fun setHook(survivor: Survivor) {
        players[survivor.player.uniqueId] = survivor
        survivor.isHooked = true
        survivor.player.teleport(pos)
    }
}