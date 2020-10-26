package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object StopGame:CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        DeadByDayLightJE.instance.forceStopGame()
        return true
    }
}