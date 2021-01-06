package com.github.rain1208.deadbydaylightje2.characters

import com.github.rain1208.deadbydaylightje2.maps.resource.Generator
import com.github.rain1208.deadbydaylightje2.maps.resource.Hook
import com.github.rain1208.deadbydaylightje2.maps.resource.Lever
import com.github.rain1208.deadbydaylightje2.maps.resource.Usable
import org.bukkit.Location
import org.bukkit.entity.Player

class Killer(override val player: Player) : IGamePlayer {
    private val baseBreakAbility = 1.0
    private val originalBreakAbility = 0.0
    val footPointVisibleDistance = 30
    override fun initPlayer(spawn: Location) {
        TODO("Not yet implemented")
    }

    override fun useGenerator(generator: Generator) {
        generator.onBreak(baseBreakAbility + originalBreakAbility)
    }

    override fun useLever(lever: Lever) {
        TODO("Not yet implemented")
    }

    override fun useHook(hook: Hook) {
        TODO("Not yet implemented")
    }

    override fun repairGeneratorComplete(generator: Generator) {
        TODO("Not yet implemented")
    }

    override fun repairLeverComplete(lever: Lever) {
        TODO("Not yet implemented")
    }
}