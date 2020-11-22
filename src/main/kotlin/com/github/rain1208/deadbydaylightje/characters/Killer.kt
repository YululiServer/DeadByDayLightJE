package com.github.rain1208.deadbydaylightje.characters

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.maps.Generator
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Killer(override val player: Player): IGamePlayer {
    private val baseBreakAbility = 20.0
    private var originalBreakAbility = 0.0

    var breakCoolDown = false

    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20

        player.inventory.clear()

        player.teleport(spawn)
        player.gameMode = GameMode.ADVENTURE

        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
    }

    override fun onUse(generator: Generator) {
        if (generator.occupancyRate <= 0.0) return
        if (breakCoolDown) return
        generator.onBreak(baseBreakAbility + originalBreakAbility)
        val n = (generator.occupancyRate / 10).toInt()
        val msg = StringBuilder("修理率 :"+"■".repeat(n)+"□".repeat(10 - n)).toString()
        player.sendTitle("",msg,0,10,0)
        setCoolDown()
    }

    private fun setCoolDown() {
        breakCoolDown = true
        object : BukkitRunnable() {
            override fun run() {
                breakCoolDown = false
            }
        }.runTaskLater(DeadByDayLightJE.instance,20*20)
    }
}