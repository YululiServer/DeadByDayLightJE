package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

object Config: CommandExecutor, TabExecutor {
    val commands = arrayListOf(
            "jail", "oni", "hacci", "itembox",
            "seizon", "generator", "fish"
    )

    override fun onTabComplete(sender: CommandSender?, command: Command?, alias: String?, args: Array<out String>?): MutableList<String> {
        val completions = arrayListOf<String>()
        if (command?.name != "config") return completions
        StringUtil.copyPartialMatches(args?.get(0),commands,completions)
        completions.sort()
        return completions
    }

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (args == null) return true
        if (sender !is Player) return true

        if (args[0].isNotEmpty() && commands.contains(args[0])) {

        }
        return true
    }

}