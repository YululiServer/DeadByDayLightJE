package com.github.rain1208.deadbydaylightje2.commands

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object StopGameCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        DeadByDayLightJE2.instance.game?.stop()
        DeadByDayLightJE2.instance.game = null
        return true
    }
}