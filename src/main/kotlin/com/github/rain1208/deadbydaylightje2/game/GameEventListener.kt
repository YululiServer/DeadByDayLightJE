package com.github.rain1208.deadbydaylightje2.game

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class GameEventListener(val game: Game): Listener {
    @EventHandler
    fun attack(event: EntityDamageByEntityEvent) {
        val survivor = game.getSurvivor(event.damager.uniqueId)
        val killer = game.getKiller(event.entity.uniqueId)

        if (survivor == null || killer == null) {
            event.isCancelled = true
            return
        }

        survivor.damage(game)
    }
}