package com.github.rain1208.deadbydaylightje2.utils

import com.github.rain1208.deadbydaylightje2.game.Game
import com.github.rain1208.deadbydaylightje2.maps.resource.Generator
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.scheduler.BukkitRunnable

class GameTimer(val game: Game): BukkitRunnable() {
    val timeBar: BossBar = Bukkit.createBossBar("", BarColor.PURPLE, BarStyle.SOLID)
    var time = 900

    init {
        timeBar.isVisible = false
    }

    override fun run() {

        when (game.phase) {
            Game.GAME_PHASE, Game.LEVER_PHASE -> {
                val text = TextComponent("発電機修理数: ${Generator.repair}/${Generator.count}")
                for (player in Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,text)
                }

                timeBar.title = "§9生存者§r ${game.survivors.size} | 残り時間 ${getTimeText()} | §c殺人鬼§r ${game.killers.size}"
                timeBar.progress = time / 900.0
                time--

                if (time == 0) {
                    game.stop()
                }
            }
            Game.LAST_PHASE -> {
                timeBar.title = "§9生存者§r ${game.survivors.size} | 残り時間 ${getTimeText()} | §c殺人鬼§r ${game.killers.size}"
                timeBar.progress = time / 60.0
                time--

                if (time == 0) {
                    game.stop()
                }
            }
        }
    }

    private fun getTimeText():String {
        var text = ""
        text += if (time/60 < 10) "0${time/60}" else "${time/60}"
        text += ":"
        text += if (time%60 < 10) "0${time%60}" else "${time%60}"
        return text
    }
}