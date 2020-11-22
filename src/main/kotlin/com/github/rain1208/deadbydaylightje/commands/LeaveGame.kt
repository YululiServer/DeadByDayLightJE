package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object LeaveGame: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) return true
        val game = DeadByDayLightJE.instance.game
        if (game !is Game) return true

        if (game.isStarted) {
            sender.sendMessage("ゲームが始まっているので退出できません")
            return true
        }

        game.leave(sender)
        return true
    }
}