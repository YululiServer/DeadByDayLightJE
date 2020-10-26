package com.github.rain1208.deadbydaylightje.characters

import org.bukkit.Location
import org.bukkit.entity.Player

class Survivor(val player: Player): IGamePlayer {
    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20

        player.inventory.clear()

        player.teleport(spawn)
    }

}