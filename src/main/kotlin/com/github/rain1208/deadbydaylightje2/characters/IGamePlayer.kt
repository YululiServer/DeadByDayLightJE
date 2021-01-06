package com.github.rain1208.deadbydaylightje2.characters

import com.github.rain1208.deadbydaylightje2.maps.resource.Generator
import com.github.rain1208.deadbydaylightje2.maps.resource.Hook
import com.github.rain1208.deadbydaylightje2.maps.resource.Lever
import com.github.rain1208.deadbydaylightje2.maps.resource.Usable
import org.bukkit.Location
import org.bukkit.entity.Player

interface IGamePlayer {
    val player: Player

    fun initPlayer(spawn: Location)

    fun onUse(usable: Usable) {
        if (usable is Generator) useGenerator(usable)
        if (usable is Lever) useLever(usable)
        if (usable is Hook) useHook(usable)
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