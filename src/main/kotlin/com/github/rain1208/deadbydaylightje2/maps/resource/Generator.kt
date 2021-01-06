package com.github.rain1208.deadbydaylightje2.maps.resource

import com.github.rain1208.deadbydaylightje2.game.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound

class Generator(override val pos: Location): Usable {
    companion object {
        var count = 0
        var repair = 0
    }

    init {
        count++
    }

    override var occupancyRate: Double = 99.0
    override var isAlive: Boolean = true
    override val range: Double = 2.0

    override fun repairComplete(game: Game) {
        pos.world.playSound(pos, Sound.BLOCK_ANVIL_USE, 3f, 1f)
        pos.world.spawnParticle(Particle.EXPLOSION_HUGE,pos,1)
        Bukkit.broadcastMessage("発電機の修理が完了しました")
        repair++
    }

    fun onBreak(breakAbility: Double) {
        pos.world.playSound(pos, Sound.ENTITY_GENERIC_EXPLODE,2f,1f)
        pos.world.spawnParticle(Particle.EXPLOSION_HUGE, pos, 1)
        occupancyRate -= breakAbility
        if (occupancyRate < 0) occupancyRate = 0.0
    }

    fun onActivate(repairAbility: Double) {
        occupancyRate += repairAbility
        if (occupancyRate > 100) occupancyRate = 100.0
    }
}