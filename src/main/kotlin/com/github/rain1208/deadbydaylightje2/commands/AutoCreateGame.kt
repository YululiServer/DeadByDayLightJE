package com.github.rain1208.deadbydaylightje2.commands

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.utils.ConfigManager
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
        val instance = DeadByDayLightJE2.instance
        val customConfig = instance.configManager.get(ConfigManager.SETTINGS)

        if (args?.isEmpty()!! || !bool.contains(args[0])) {
            customConfig.config.set("auto-create-game", !instance.autoStart())
        } else {
            customConfig.config.set("auto-create-game", args[0].toBoolean())
        }
        customConfig.save()
        return true
    }
}