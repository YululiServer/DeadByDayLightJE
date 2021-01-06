package com.github.rain1208.deadbydaylightje2.characters

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.game.Game
import com.github.rain1208.deadbydaylightje2.maps.resource.Generator
import com.github.rain1208.deadbydaylightje2.maps.resource.Hook
import com.github.rain1208.deadbydaylightje2.maps.resource.Lever
import com.github.rain1208.deadbydaylightje2.maps.resource.Usable
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Survivor(override val player: Player) : IGamePlayer {
    private val baseRepairAbility = 1.0
    private var originalRepairAbility = 0.0

    private val baseLeverActivateAbility = 1.0
    private var originalLeverActivateAbility = 0.0

    var footPointLength = 50

    var health = 2

    var hook = 0
    var rescue = 0

    var rescuePlayer:Survivor? = null

    var isHooked = false

    var rescueCoolDown = false
    var damageCoolDown = false

    override fun initPlayer(spawn: Location) {
        player.health = 20.0
        player.foodLevel = 20
        player.gameMode = GameMode.ADVENTURE
        player.teleport(spawn)

        player.inventory.clear()

        health = 2

        hook = 0
        rescue = 0

        isHooked = false

        rescueCoolDown = false
        damageCoolDown = false

        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
    }

    fun damage(game: Game) {
        if (damageCoolDown) return

        health--
        setDamageCoolDown()

        if (health == 0) {
            game.map.randomHook().setHook(this)
        }
    }

    private fun setDamageCoolDown() {
        damageCoolDown = true
        object : BukkitRunnable() {
            override fun run() {
                damageCoolDown = false
            }
        }.runTaskLaterAsynchronously(DeadByDayLightJE2.instance,200)
    }

    override fun useGenerator(generator: Generator) =
        generator.onActivate(baseRepairAbility + originalRepairAbility)


    override fun useLever(lever: Lever) =
        lever.onActivate(baseLeverActivateAbility + originalLeverActivateAbility)

    override fun useHook(hook: Hook) {
    }


    override fun repairGeneratorComplete(generator: Generator) {
        val msg = StringBuilder(ChatColor.GREEN.toString() +"修理率 : " + "■".repeat(10)).toString()
        player.sendTitle(ChatColor.GREEN.toString()+"発電完了", msg, 0, 20,0)
    }

    override fun repairLeverComplete(lever: Lever) {
    }
}