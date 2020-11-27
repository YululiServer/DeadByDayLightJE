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
        val meta = book.itemMeta as BookMeta
        meta.displayName = "ゲーム Book"
        meta.title = "ゲーム Book"

        book.itemMeta = meta
        sender.inventory.addItem(book)

        return true
    }

    fun getContents(): BookMeta {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val meta = book.itemMeta as BookMeta

        meta.displayName = "ゲーム Book"
        meta.title = "ゲーム Book"

        meta.spigot().pages = mutableListOf()

        val game = DeadByDayLightJE.instance.game

        if (game !is Game) {
            val create = TextComponent("[作成]")
            create.color = ChatColor.GREEN
            create.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/dcreate")
            val message = TextComponent("ゲームを作成する : ")
            message.addExtra(create)
            meta.spigot().addPage(arrayOf(message))
        } else {
            /*
        ゲームスタート
        キラー サバイバー
        ワールドの情報
        自動でゲームを作るかの設定
         */
        }

        val auto = TextComponent(if (!DeadByDayLightJE.instance.autoStart()) "ON" else "OFF")
        auto.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/autocreate")
        val msg = TextComponent("ゲームの自動更新 : ")
        msg.addExtra(auto)
        meta.spigot().addPage(arrayOf(msg))

        return meta
    }
}