package com.github.rain1208.deadbydaylightje.characters

import com.github.rain1208.deadbydaylightje.maps.Generator
import org.bukkit.Location
import org.bukkit.entity.Player

class Survivor(override val player: Player): IGamePlayer {

    val baseRepairAbility = 1.0
    var originalRepairAbility = 9.0 //TODO("ここの数字はテストのため")

    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20

        player.inventory.clear()

        player.teleport(spawn)
    }

    override fun onUse(generator: Generator) {
        generator.onActivate(baseRepairAbility + originalRepairAbility)
        val n = (generator.occupancyRate / 10).toInt()
        val msg = StringBuilder("修理率 :"+"■".repeat(n)+"□".repeat(10 - n)).toString()
        //val text = TextComponent(msg.toString())
        //player.spigot().sendMessage(ChatMessageType.ACTION_BAR,text)

        player.sendTitle("", msg,0,20,0)
    }

    fun repairComplete() {
        player.resetTitle()
        val msg = StringBuilder("§2修理率 :"+"■".repeat(10)).toString()
        player.sendTitle("§2修理完了!!",msg,0,30,0)
    }
}