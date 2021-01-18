package com.github.rain1208.deadbydaylightje2.game

import com.github.rain1208.deadbydaylightje2.maps.resource.Generator
import org.bukkit.scheduler.BukkitRunnable

class GameTask(val game: Game): BukkitRunnable() {
    override fun run() {
        when (game.phase) {
            Game.GAME_PHASE -> {
                if (Generator.count - Generator.repair == 0) {
                    game.generatorRepairAllComplete()
                    return
                }
                for (generator in game.map.generators) {
                    generator.baseTick(game)
                }
            }
            Game.LEVER_PHASE -> {
                for (lever in game.map.levers) {
                    lever.baseTick(game)
                }
            }
            Game.LAST_PHASE -> {

            }
        }
        for (hook in game.map.hooks) {
            hook.baseTick(game)
        }
    }
}