package com.github.rain1208.deadbydaylightje2.commands

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

object ChooseMapCommand: CommandExecutor, TabCompleter {
    override fun onTabComplete(sender: CommandSender?, command: Command?, alias: String?, args: Array<out String>?): MutableList<String> {
        val completions = arrayListOf<String>()
        if (command?.name != "map") return completions
        if (args?.size!! > 1) return completions
        StringUtil.copyPartialMatches(args[0], DeadByDayLightJE2.instance.mapManager.maps.keys, completions)
        completions.sort()
        return completions
    }

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (args?.isEmpty()!!) {
            sender?.sendMessage("引数が入力されていません")
            return true
        }
        if (args.size != 1) {
            sender?.sendMessage("引数が不正です")
            return true
        }

        DeadByDayLightJE2.instance.game?.setMap(args[0])

        return true
    }
}