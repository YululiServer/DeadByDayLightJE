package com.github.rain1208.deadbydaylightje2.characters

import com.github.rain1208.deadbydaylightje2.maps.resource.*
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

interface IGamePlayer {
    val player: Player

    fun initPlayer(spawn: Location)

    fun onUse(resource: GameResource) {
        if (resource is Generator) useGenerator(resource)
        if (resource is Lever) useLever(resource)
        if (resource is Hook) useHook(resource)
    }

    fun useGenerator(generator: Generator)

    fun useLever(lever: Lever)

    fun useHook(hook: Hook)

    fun repairComplete(usable: Usable) {
        if (usable is Generator) repairGeneratorComplete(usable)
        if (usable is Lever) repairLeverComplete(usable)
    }

    fun repairGeneratorComplete(generator: Generator)

    fun repairLeverComplete(lever: Lever)
}