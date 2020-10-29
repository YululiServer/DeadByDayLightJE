package com.github.rain1208.deadbydaylightje.game

import com.github.rain1208.deadbydaylightje.utils.BossBar
import org.bukkit.scheduler.BukkitRunnable

class GameTask(val game: Game): BukkitRunnable() {
    var time = 900
    val timeBar = BossBar().createBar()

    override fun run() {
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
        val title = "残り時間 $time | 殺人鬼 ${game.killers.size} | 生存者 ${game.survivor.size} |"
        timeBar.setTitle(title)
        timeBar.setProgress(time.toDouble()/900.0)
    }
}