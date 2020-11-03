package com.github.rain1208.deadbydaylightje.game

import com.github.rain1208.deadbydaylightje.characters.Survivor
import com.github.rain1208.deadbydaylightje.utils.BossBar
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class GameTask(val game: Game): BukkitRunnable() {
    var time = 900
    val timeBar = BossBar()

    val hookedSurvivor = mutableMapOf<String, Survivor>()

    override fun run() {
        if (time <= 0) {
            game.stop()
        }
        hookPlayerUpdate()
        sendData()
        for (generator in game.generators) {
            if (generator.isAlive) generator.baseTick(game)
        }
        time--
    }

    fun hookPlayerUpdate() {
        for ((name,surv) in hookedSurvivor) {
            val players = surv.player.getNearbyEntities(1.0,2.0,1.0).filterIsInstance<Player>()

            for (player in players) {
                if (hookedSurvivor.contains(player.name) || !game.isSurvivor(player)) continue
                if (game.survivor[player.name]?.rescueCoolDown!!) continue
                if (player.isSneaking) {
                    surv.addHookCount(true)
                    if (surv.rescueCount >= 3) {
                        game.survivor[player.name]?.rescue(surv)
                        break
                    }
                }
            }

            surv.addHookCount(false)
            if (surv.hookCount >= 30) {
                hookedSurvivor.remove(name)
                game.goToJail(surv)
                return
            }
        }
    }

    fun timerStart() {
        for (survivor in game.getSurvivors()) {
            timeBar.addPlayer(survivor.player)
        }
        for (killer in game.getKillers()) {
            timeBar.addPlayer(killer.player)
        }
    }

    private fun sendData() {
        val title = "残り時間 ${time/60}:${if (time%60 == 0) "00" else time%60} | §c殺人鬼§r ${game.killers.size} | §9生存者§r ${game.survivor.size} |"
        timeBar.setTitle(title)
        timeBar.setProgress(time.toDouble()/900.0)


        val message = TextComponent("残り発電機: ${game.generatorCount}")
        for (player in Bukkit.getOnlinePlayers()) {
            message.text += " | スキル: 未実装"
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,message)
        }
    }
}