package com.github.rain1208.deadbydaylightje.game

import org.bukkit.scheduler.BukkitRunnable

class GameTask(val game: Game): BukkitRunnable() {
    override fun run() {
        for (generator in game.generators) {
            if (generator.isAlive) generator.baseTick(game)
        }
    }
}