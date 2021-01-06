package com.github.rain1208.deadbydaylightje2.commands

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object LeaveGameCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender is Player) DeadByDayLightJE2.instance.game?.leave(sender)
        return true
    }
}