package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.game.Game
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object GameStatus:CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender == null) return true
        val instance = DeadByDayLightJE.instance
        val game = instance.game

        if (game !is Game) {
            sender.sendMessage("ゲームは作られていません")
            return true
        }
        val message = TextComponent("========ゲームの状態========")
        message.addExtra(TextComponent("\n殺人鬼: "+ game.killers.keys))
        message.addExtra(TextComponent("\n生存者: "+ game.survivor.keys))
        sender.spigot().sendMessage(message)
        return true
    }
}