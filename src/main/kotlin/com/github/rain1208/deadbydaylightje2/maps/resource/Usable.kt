package com.github.rain1208.deadbydaylightje2.maps.resource

import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.*

interface Usable: GameResource {
    override val pos: Location
    override var isAlive: Boolean
    var occupancyRate: Double
    val range: Double

    override fun baseTick(game: Game) {
        val nearPlayer = Bukkit.getOnlinePlayers().filter { pos.distance(it.location) <= range }
        if (!isAlive) {
            for (player in nearPlayer) {
                val msg = StringBuilder(ChatColor.GREEN.toString() +"修理率 : " + "■".repeat(10)).toString()
                player.sendTitle("", msg, 0, 20,0)
            }
            return
        }
        for (player in nearPlayer) {
            if (player.isSneaking) {
                game.getPlayer(player.uniqueId)?.onUse(this)
            }
            var n = (occupancyRate / 10).toInt()
            if (n > 10) n = 10
            if (n < 0) n = 0
            val msg = StringBuilder("修理率 : " + "■".repeat(n) + "□".repeat(10 - n)).toString()
            player.sendTitle("", msg,0,20,0)
        }

        if (occupancyRate >= 100) {
            repairComplete(game)
            for (player in nearPlayer) {
                game.getPlayer(player.uniqueId)?.repairComplete(this)
            }
            isAlive = false
        }
    }

    fun repairComplete(game: Game)
}