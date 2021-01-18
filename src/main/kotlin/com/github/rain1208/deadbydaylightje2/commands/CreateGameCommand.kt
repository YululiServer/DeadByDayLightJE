package com.github.rain1208.deadbydaylightje2.commands

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object CreateGameCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (DeadByDayLightJE2.instance.game is Game) {
            sender?.sendMessage("既にゲームは作られています")
            return true
        }
        DeadByDayLightJE2.instance.createGame()
        return true
    }
}