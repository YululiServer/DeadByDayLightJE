package com.github.rain1208.deadbydaylightje.game

import com.github.rain1208.deadbydaylightje.utils.BossBar
import org.bukkit.scheduler.BukkitRunnable

class GameTask(val game: Game): BukkitRunnable() {
    var time = 900
    val timeBar = BossBar()

    override fun run() {
        if (time <= 0) {
            TODO()
        }
        sendBossBar()
        for (generator in game.generators) {
            if (generator.isAlive) generator.baseTick(game)
        }
        time--
    }

    fun timerStart() {
        for (survivor in game.getSurvivors()) {
            timeBar.addPlayer(survivor.player)
        }
        for (killer in game.getKillers()) {
            timeBar.addPlayer(killer.player)
        }
    }

    private fun sendBossBar() {
        val title = "残り時間 ${time/60}:${if (time%60 == 0) "00" else time%60} | §c殺人鬼§r ${game.killers.size} | §9生存者§r ${game.survivor.size} |"
        timeBar.setTitle(title)
        timeBar.setProgress(time.toDouble()/900.0)
    }
}