package com.github.rain1208.deadbydaylightje.commands

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.game.Game
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

object GameBook: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) return true

        val book = ItemStack(Material.WRITTEN_BOOK)

        book.itemMeta = getContents()
        sender.inventory.addItem(book)

        return true
    }

    fun getContents(): BookMeta {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val meta = book.itemMeta as BookMeta

        meta.displayName = "ゲーム Book"
        meta.title = "ゲーム Book"

        meta.spigot().pages = mutableListOf()

        val start = TextComponent(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + "    ゲームを開始する")
        start.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dstart")
        meta.spigot().addPage(arrayOf(TextComponent("\n\n\n"),start))

        val create = TextComponent("[作成]")
        create.color = ChatColor.GREEN
        create.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dcreate")
        val message = TextComponent("ゲームを作成する : ")
        message.addExtra(create)
        meta.spigot().addPage(arrayOf(message))

        val auto = TextComponent("[切り替え]")
        auto.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/autocreate")
        auto.color = ChatColor.GREEN
        val msg = TextComponent("ゲームの自動更新:")
        msg.addExtra(auto)
        meta.spigot().addPage(arrayOf(msg))

        return meta
    }
}