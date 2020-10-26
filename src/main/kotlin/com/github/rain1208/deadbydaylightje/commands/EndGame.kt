package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EndGame:CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) return true
        with(DeadByDayLightJE.instance) {
            if (game is Game) {
                game?.stop()
            } else {
                sender.sendMessage("ゲームが作成されていないので開始できませんでした")
                logger.info("ゲームが作成されていません")
            }
        }
        return true
    }
}