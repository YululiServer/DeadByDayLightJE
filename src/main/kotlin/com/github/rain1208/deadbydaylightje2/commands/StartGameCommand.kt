package com.github.rain1208.deadbydaylightje2.commands

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object StartGameCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        DeadByDayLightJE2.instance.apply {
            if (game is Game) {
                game?.startCountStart()
            } else {
                if (sender is Player) sender.sendMessage("ゲームが作成されていません")
                logger.info("ゲームが作成されていません")
            }
        }
        return true
    }
}