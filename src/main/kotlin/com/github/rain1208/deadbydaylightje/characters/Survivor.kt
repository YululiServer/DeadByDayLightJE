package com.github.rain1208.deadbydaylightje.characters

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.maps.Generator
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Survivor(override val player: Player): IGamePlayer {
    val baseRepairAbility = 1.0
    var originalRepairAbility = 9.0 //TODO("ここの数字はテストのため")

    var isHanged = false

    var hp = 2

    var tpJail: BukkitRunnable? = null

    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20
        hp = 2

        player.inventory.clear()

        player.teleport(spawn)
        player.gameMode = GameMode.ADVENTURE
        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
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

    fun setFish(hook: Location, jail: Location) {
        isHanged = true
        player.teleport(hook)

        tpJail = object : BukkitRunnable() {
            override fun run() {
                player.teleport(jail)
            }
        }
        tpJail?.runTaskLater(DeadByDayLightJE.instance,20*30)
    }

    fun rescue(rescuer: Player) {
        if (tpJail?.isCancelled!!) return
        hp = 1
        isHanged = false
        tpJail?.cancel()
        player.teleport(rescuer)
    }
}