package com.github.rain1208.deadbydaylightje2.characters

import com.github.rain1208.deadbydaylightje2.characters.killerTypes.KillerType
import com.github.rain1208.deadbydaylightje2.maps.resource.Generator
import com.github.rain1208.deadbydaylightje2.maps.resource.Hook
import com.github.rain1208.deadbydaylightje2.maps.resource.Lever
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player

class Killer(override val player: Player) : IGamePlayer {
    private val baseBreakAbility = 1.0
    private val originalBreakAbility = 0.0
    val footPointVisibleDistance = 30

    private val killerType: KillerType? = null

    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20
        player.gameMode = GameMode.ADVENTURE

        player.inventory.clear()

        if (killerType != null) {
            player.inventory.contents = killerType.inventory.contents
            player.inventory.armorContents = killerType.inventory.armorContents
        }
    }

    override fun useGenerator(generator: Generator) =
        generator.onBreak(baseBreakAbility + originalBreakAbility)


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