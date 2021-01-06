package com.github.rain1208.deadbydaylightje2

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class EventListener: Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val game = DeadByDayLightJE2.instance.game
        game?.join(event.player)
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val game = DeadByDayLightJE2.instance.game
        game?.leave(event.player)
    }
}