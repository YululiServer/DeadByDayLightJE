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
            "jail", "oni", "hatch", "itembox", "lobby",
            "seizon", "generator", "fish", "respawnBlock",
            "gateOpen"
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
        if (args.isEmpty()) return true
        if (sender !is Player) return true

        if (args[0].isNotEmpty() && commands.contains(args[0])) {
            val config = DeadByDayLightJE.instance.configManager
            val pos = sender.location
            val text = "${pos.blockX},${pos.blockY},${pos.blockZ}"
            when (args[0]) {
                "jail","lobby","respawnBlock" -> config.set(args[0],text)
                else -> config.add(args[0],text)
            }
            sender.sendMessage(text+"を"+args[0]+"に設定しました")
        }
        return true
    }
}