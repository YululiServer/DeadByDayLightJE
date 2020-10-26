package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SetKiller: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        DeadByDayLightJE.instance.game?.apply {
            val player = Bukkit.getPlayer(args[0]) ?: Bukkit.getPlayer(players.keys.random())
            if (player is Player) {
                setKiller(player)
            }
        } ?: sender.sendMessage("ゲームはありません")
        return true
    }
}

