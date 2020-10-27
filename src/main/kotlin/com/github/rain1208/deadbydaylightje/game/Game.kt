package com.github.rain1208.deadbydaylightje.game

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.characters.IGamePlayer
import com.github.rain1208.deadbydaylightje.characters.Killer
import com.github.rain1208.deadbydaylightje.characters.Survivor
import com.github.rain1208.deadbydaylightje.maps.Map
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.scheduler.BukkitRunnable

class Game {
    val survivor: MutableMap<String, Survivor> = mutableMapOf()
    val killers: MutableMap<String, Killer> = mutableMapOf()

    lateinit var map:Map

    fun start() {
        map = Map()

        Bukkit.getPluginManager().registerEvents(GameEventListener(this), DeadByDayLightJE.instance)
        Bukkit.broadcastMessage("ゲームを開始します")

        var count = 10

        object : BukkitRunnable() {
            override fun run() {
                if (count <= 0) {
                    cancel()
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.sendTitle("スタート!", "", 20, 50, 20)
                        startPlayer()
                    }
                } else {
                    Bukkit.broadcastMessage("ゲーム開始まで: $count")
                    count--
                }
            }
        }.runTaskTimer(DeadByDayLightJE.instance,0,20)
    }

    fun startPlayer() {
        for (survivor in survivor.values) {
            survivor.initPlayer(map.getSpawn())
        }
        object :BukkitRunnable(){
            override fun run() {
                for (killer in killers.values) {
                    killer.initPlayer(map.getKillerSpawn())
                }
            }
        }.runTaskLater(DeadByDayLightJE.instance,100)
    }

    fun stop() {
        HandlerList.unregisterAll(GameEventListener(this))
        with(DeadByDayLightJE.instance) {
            game = null
            logger.info("ゲームを終了しました")
            if (autoStart()) createGame()
        }
    }

    fun join(player: Player) {
        survivor[player.name] = Survivor(player)
        Bukkit.broadcastMessage("${player.name} さんがゲームに参加しました")
    }

    fun leave(player: Player) {
        if (survivor.contains(player.name)) {
            survivor.remove(player.name)
            Bukkit.broadcastMessage("サバイバー: ${player.name} さんがゲームから退出しました")
        }
        if (killers.contains(player.name)) {
            killers.remove(player.name)
            Bukkit.broadcastMessage("キラー: ${player.name} さんがゲームから退出しました")
        }
    }

    fun setKiller(player: Player) {
        if (survivor.contains(player.name)) leave(player)
        Bukkit.broadcastMessage("${player.name} さんがキラーになりました")
    }
}