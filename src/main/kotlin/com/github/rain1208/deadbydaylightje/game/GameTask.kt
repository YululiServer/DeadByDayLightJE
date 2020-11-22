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
            cancel()
            game.result()
        }

        if (game.generatorCount == 0) {
            if (!game.isRepairAllComplete) {
                game.repairAllComplete()
            }
        }

        if (game.killers.isEmpty()) {
            game.stop()
            Bukkit.broadcastMessage("キラーがいなくなったのでゲームを終了します")
        }

        if (game.survivor.isEmpty()) {
            cancel()
            game.result()
        }

        hookPlayerUpdate()
        sendData()

        if (game.isRepairAllComplete) {
            for (lever in game.levers) {
                if (lever.isAlive) lever.baseTick(game)
            }
        }

        for (generator in game.generators) {
            if (generator.isAlive) generator.baseTick(game)
        }
        time--
    }

    fun hookPlayerUpdate() {
        for ((name,surv) in hookedSurvivor) {
            val players = surv.player.getNearbyEntities(1.0,2.0,1.0).filterIsInstance<Player>()
            var flag = false
            for (player in players) {
                if (hookedSurvivor.contains(player.name) || !game.isSurvivor(player)) continue
                if (game.survivor[player.name]?.rescueCoolDown!!) continue
                if (player.isSneaking) {
                    player.sendTitle("救助中","" ,0,20,0)
                    flag = true
                    if (surv.rescueCount >= 2 && surv.isHooked) {
                        surv.isHooked = false
                        hookedSurvivor.remove(name)
                        game.survivor[player.name]?.rescue(surv)
                        break
                    }
                }
            }
            surv.addHookCount(flag)
            if (surv.hookCount >= 30) {
                hookedSurvivor.remove(name)
                game.goToJail(surv)
                return
            }
        }
    }

    private fun sendData() {
        //val title = "残り時間 ${time/60}:${if (time%60 == 0) "00" else time%60} | §c殺人鬼§r ${game.killers.size} | §9生存者§r ${game.survivor.size} |"
        val title = "§9生存者§r ${game.survivor.size} | 残り時間 ${time/60}:${if (time%60 == 0) "00" else time%60} | §c殺人鬼§r ${game.killers.size}"
        timeBar.setTitle(title)
        timeBar.setProgress(time.toDouble()/900.0)

        val message = TextComponent("残り発電機: ${game.generatorCount} | スキル: 未実装")
        for (player in Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,message)
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
}