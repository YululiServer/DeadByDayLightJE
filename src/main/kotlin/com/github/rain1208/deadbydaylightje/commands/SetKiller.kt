package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

object SetKiller: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        if (args.size == 1) {
            if (args[0] == "_RANDOM_") {
                val players = DeadByDayLightJE.instance.game?.getSurvivors()
                if (players != null && players.size > 0) {
                    DeadByDayLightJE.instance.game?.setKiller(players[Random.nextInt(players.size)])
                } else {
                    sender.sendMessage("参加中のプレイヤーがいないため抽選できません")
                }
                return true
            }

            DeadByDayLightJE.instance.game?.setKiller(Bukkit.getPlayer(args[0]))
        } else {
            var count = 0
            val message = TextComponent("===============鬼の決定===============\n")
            message.color = ChatColor.RED
            for (player in Bukkit.getOnlinePlayers()) {
                val text = TextComponent(player.name + "§r, ")
                text.color = ChatColor.GREEN
                text.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/oni ${player.name}")
                message.addExtra(text)

                count++
                if (count % 4 == 0) message.addExtra("\n")
            }
            val randText = TextComponent("\nランダムに抽選する")
            randText.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/oni _RANDOM_")
            val suffix = TextComponent("\n" + "=".repeat(39))
            suffix.color = ChatColor.RED
            message.addExtra(randText)
            message.addExtra(suffix)

            sender.spigot().sendMessage(message)
        }
        return true
    }
}

