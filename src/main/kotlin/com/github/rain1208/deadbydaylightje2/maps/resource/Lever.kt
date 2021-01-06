package com.github.rain1208.deadbydaylightje2.maps.resource

import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.Location

class Lever(override val pos: Location): Usable {
    override var occupancyRate: Double = 0.0
    override var isAlive: Boolean = true
    override val range: Double = 2.0

    override fun repairComplete(game: Game) {
        game.leverActivate()
    }

    fun onActivate(leverActivateAbility: Double) {
        occupancyRate += leverActivateAbility
        if (occupancyRate > 100) occupancyRate = 100.0
    }
}