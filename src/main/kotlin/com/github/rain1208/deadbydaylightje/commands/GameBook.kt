package com.github.rain1208.deadbydaylightje.commands

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
        val meta = book.itemMeta as BookMeta
        meta.displayName = "ゲーム Book"
        meta.title = "ゲーム Book"

        val page = mutableListOf<TextComponent>()

        page.add(TextComponent("ゲームを作成する"))

        val text = TextComponent("[ 作成 ]")
        text.color = ChatColor.GREEN
        text.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dcreate")
        page.add(text)

        page.add(TextComponent(""))

        meta.spigot().addPage(page.toTypedArray())

        book.itemMeta = meta
        sender.inventory.addItem(book)

        return true
    }
}