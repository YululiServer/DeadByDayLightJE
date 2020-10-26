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
    val players: MutableMap<String, IGamePlayer> = mutableMapOf()
    lateinit var killer: Killer

    lateinit var map:Map

    fun start() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player == killer.player) continue
            players[player.name] = Survivor(player)
        }
        map = Map()

        if (!::killer.isInitialized) {
            println("killerがいないため開始できませんでした")
            Bukkit.broadcastMessage("killerがいないため開始できません")
            return
        }
        Bukkit.getPluginManager().registerEvents(GameEventListener(this), DeadByDayLightJE.instance)
        Bukkit.broadcastMessage("ゲームを開始します")

        var count = 5

        object : BukkitRunnable() {
            override fun run() {
                if (count <= 0) {
                    cancel()
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.sendTitle("スタート!","",20,50,20)
                        players[player.name]?.initPlayer(map.getSpawn())

                        killer.initPlayer(map.getKillerSpawn())

                    }
                } else {
                    Bukkit.broadcastMessage("ゲーム開始まで: $count")
                    count--
                }
            }
        }.runTaskTimer(DeadByDayLightJE.instance,0,20)
    }

    fun stop() {
        HandlerList.unregisterAll(GameEventListener(this))
        with(DeadByDayLightJE.instance) {
            game = null
            logger.info("ゲームを終了しました")
            if (autoStart()) createGame()
        }
    }

    /*fun join(player: Player) {
        if (playerExists(player)) {
            player.sendMessage("既にゲームに参加しています")
            return
        }
        players[player.name] = Survivor(player)
        Bukkit.broadcastMessage("${player.name} さんがゲームに参加しました")
    }*/

    fun leave(player: Player) {
        players.remove(player.name)
        Bukkit.broadcastMessage("${player.name} さんがゲームから退出しました")
    }

    fun setKiller(player: Player) {
        if (players.contains(player.name)) leave(player)
        killer = Killer(player)
        Bukkit.broadcastMessage("${player.name} さんがキラーになりました")
    }
}