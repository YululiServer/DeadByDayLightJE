package com.github.rain1208.deadbydaylightje2.maps.resource

import com.github.rain1208.deadbydaylightje2.characters.Survivor
import com.github.rain1208.deadbydaylightje2.game.Game
import com.github.rain1208.deadbydaylightje2.game.GameSetting
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class Hook(override val pos: Location): GameResource {
    override var isAlive: Boolean = true
    var range = 2.0

    private val players: MutableMap<UUID,Survivor> = mutableMapOf()
    private val rescue: MutableList<UUID> = mutableListOf()
    private var rescuea: MutableList<UUID> = mutableListOf()

    override fun baseTick(game: Game) {
        if (players.isEmpty()) return

        val nearPlayer = Bukkit.getOnlinePlayers().filter { pos.distance(it.location) <= range }.filterNot { players.contains(it.uniqueId) }

        for (player in nearPlayer) {
            if (player.isSneaking) {
                game.getSurvivor(player.uniqueId)?.onUse(this)
            }
        }
        rescuea = rescue
        rescue.clear()
    }

    fun rescue(survivor: Survivor) {
        if (rescuea.contains(survivor.player.uniqueId)) {
            survivor.rescueCount = 0
            rescue.add(survivor.player.uniqueId)
        }
        survivor.rescueCount++

        if (survivor.rescueCount >= GameSetting.rescueTime) {
            val player = getRandomHookPlayer() ?: return
            player.initPlayer(survivor.player.location)
            player.health = 1
            survivor.setRescueCoolDown()
        }
    }

    fun getRandomHookPlayer(): Survivor? {
        val key = players.keys.toList()[Random().nextInt(players.size)]
        return players[key]
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