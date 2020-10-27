package com.github.rain1208.deadbydaylightje.characters

import com.github.rain1208.deadbydaylightje.maps.Generator
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Killer(val player: Player): IGamePlayer {
    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20

        player.inventory.clear()

        val axe = ItemStack(Material.IRON_AXE)
        axe.amount = 10
        player.inventory.addItem(axe)

        player.teleport(spawn)
    }

    override fun power(generator: Generator) {
        println(generator.count)
    }
}