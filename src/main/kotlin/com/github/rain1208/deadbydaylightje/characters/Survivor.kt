package com.github.rain1208.deadbydaylightje.characters

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.maps.Generator
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Survivor(override val player: Player): IGamePlayer {
    private val baseRepairAbility = 1.0
    private var originalRepairAbility = 9.0 //TODO("ここの数字はテストのため")

    var hp = 2

    var hookCount = 0
    var rescueCount = 0
    var rescueCoolDown = false

    var isHooked = false

    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20

        hp = 2

        hookCount = 0
        rescueCount = 0

        isHooked = false

        player.inventory.clear()

        player.teleport(spawn)
        player.gameMode = GameMode.ADVENTURE
        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
    }

    fun rescue(survivor: Survivor) {
        survivor.initPlayer(player.location)
        Bukkit.broadcastMessage(survivor.player.name + "がフックから救出された")
        setRescueCoolDown()
    }

    private fun setRescueCoolDown() {
        rescueCoolDown = true
        object : BukkitRunnable() {
            override fun run() {
                rescueCoolDown = false
            }
        }.runTaskLater(DeadByDayLightJE.instance,10)
    }

    fun addHookCount(rescue: Boolean) {
        if (rescue) {
            rescueCount++
        } else {
            rescueCount = 0
            hookCount++
        }
    }

    fun addDamage() = hp--

    override fun onUse(generator: Generator) {
        generator.onActivate(baseRepairAbility + originalRepairAbility)
        val n = (generator.occupancyRate / 10).toInt()
        val msg = StringBuilder("修理率 :"+"■".repeat(n)+"□".repeat(10 - n)).toString()

        player.sendTitle("", msg,0,20,0)
    }

    fun repairComplete() {
        player.resetTitle()
        val msg = StringBuilder("§2修理率 :"+"■".repeat(10)).toString()
        player.sendTitle("§2修理完了!!",msg,0,30,0)
    }
}