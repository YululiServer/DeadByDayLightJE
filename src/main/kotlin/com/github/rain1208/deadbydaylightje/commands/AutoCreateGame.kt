package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

object AutoCreateGame: CommandExecutor, TabCompleter {
    private val bool = arrayListOf("true", "false")
    override fun onTabComplete(sender: CommandSender?, command: Command?, alias: String?, args: Array<out String>?): MutableList<String> {
        return bool
    }

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        val instance = DeadByDayLightJE.instance
        val config = instance.config

        if (args?.isEmpty()!! || !bool.contains(args[0])) {
            config.set("auto-create-game", !instance.autoStart())
            instance.saveConfig()
        } else {
            config.set("auto-create-game", args[0].toBoolean())
            instance.saveConfig()
        }

        sender?.sendMessage("auto-create-gameを" + (if (instance.autoStart()) "ON" else "OFF") + "にしました")

        return true
    }
}