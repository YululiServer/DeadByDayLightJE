package com.github.rain1208.deadbydaylightje

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable

class EventListener: Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val game = DeadByDayLightJE.instance.game
        game?.join(event.player)

        if(!game?.isStarted!!) {
            object : BukkitRunnable() {
                override fun run() {
                    //ルールの表示
                }
            }.runTaskLater(DeadByDayLightJE.instance,20*60)
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val game = DeadByDayLightJE.instance.game
        game?.leave(event.player)
    }
}