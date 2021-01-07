package com.github.rain1208.deadbydaylightje2.maps.resource

import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.Location

interface GameResource {
    val pos: Location
    var isAlive: Boolean

    fun baseTick(game: Game)
}