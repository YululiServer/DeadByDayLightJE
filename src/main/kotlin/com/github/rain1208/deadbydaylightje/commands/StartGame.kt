package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object StartGame: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        with(DeadByDayLightJE.instance) {
            if (game != null) {
                game?.start()
            } else {
                sender.sendMessage("ゲームが作成されていないので開始できませんでした")
                logger.info("ゲームが作成されていません")
            }
        }
        return true
    }
}