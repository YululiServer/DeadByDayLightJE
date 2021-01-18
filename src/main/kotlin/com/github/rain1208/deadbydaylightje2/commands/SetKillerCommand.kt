package com.github.rain1208.deadbydaylightje2.commands

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SetKillerCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) return true
        val game = DeadByDayLightJE2.instance.game ?: return true
        if (args == null || args.isEmpty()) {
            val prefix = TextComponent("===============鬼の決定===============")
            prefix.color = ChatColor.RED
            sender.spigot().sendMessage(ChatMessageType.CHAT, prefix)

            val messageBase = TextComponent("")

            for ((index, player) in Bukkit.getOnlinePlayers().withIndex()) {
                val text = TextComponent(player.name)
                text.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/oni ${player.name}")
                text.color = ChatColor.GREEN
                text.addExtra("§r, ")

                if (index % 4 == 0) text.addExtra("\n")

                messageBase.addExtra(text)
            }
            sender.spigot().sendMessage(messageBase)

            val rand = TextComponent("\nランダムに抽選する")
            rand.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/oni __RANDOM__")
            rand.color = ChatColor.DARK_BLUE
            sender.spigot().sendMessage(rand)

            val suffix = TextComponent("\n" + "=".repeat(39))
            suffix.color = ChatColor.RED
            sender.spigot().sendMessage(suffix)

            return true
        }

        if (args.size == 1) {
            if (args[0] == "__RANDOM__") {
                val player = Bukkit.getPlayer(game.survivors.keys.random())
                if (player.isOnline)
                    game.setKiller(player)
                return true
            }
            val player = Bukkit.getPlayer(args[0])
            if (player != null) {
                if (player.isOnline)
                    game.setKiller(player)
                return true
            }
        }
        return true
    }
}
