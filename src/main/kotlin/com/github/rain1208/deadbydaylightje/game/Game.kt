package com.github.rain1208.deadbydaylightje.game

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.EventListener
import com.github.rain1208.deadbydaylightje.characters.IGamePlayer
import com.github.rain1208.deadbydaylightje.characters.Killer
import com.github.rain1208.deadbydaylightje.characters.Survivor
import com.github.rain1208.deadbydaylightje.maps.Generator
import com.github.rain1208.deadbydaylightje.maps.Map
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.scheduler.BukkitRunnable

class Game {
    val survivor: MutableMap<String, Survivor> = mutableMapOf()
    val killers: MutableMap<String, Killer> = mutableMapOf()

    val generators: ArrayList<Generator> = arrayListOf()
    var generatorCount = 0

    lateinit var map:Map

    val gameTask = GameTask(this)

    var isStarted = false

    fun start() {
        map = Map()

        for (loc in map.generatorPoint) {
            generators.add(Generator(loc.world.spawn(loc,ArmorStand::class.java)))
            generatorCount++
        }

        HandlerList.unregisterAll(EventListener())
        Bukkit.getPluginManager().registerEvents(GameEventListener(this), DeadByDayLightJE.instance)
        Bukkit.broadcastMessage("ゲームを開始します")

        var count = 10

        object : BukkitRunnable() {
            override fun run() {
                if (count <= 0) {
                    cancel()
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.sendTitle("スタート!", "", 20, 50, 20)
                    }
                    gameTask.runTaskTimer(DeadByDayLightJE.instance,0,20)
                    gameTask.timeBar.createBar()
                    gameTask.timerStart()
                    startPlayer()
                    map.world.time = 13800
                    map.world.setGameRuleValue("doDaylightCycle","false")
                } else {
                    Bukkit.broadcastMessage("ゲーム開始まで: $count"+"秒")
                    count--
                }
            }
        }.runTaskTimer(DeadByDayLightJE.instance,0,20)
        isStarted = true
    }

    fun startPlayer() {
        for (survivor in survivor.values) {
            survivor.initPlayer(map.getSpawn())
        }

        object :BukkitRunnable(){
            override fun run() {
                println(killers)
                for (killer in killers.values) {
                    killer.initPlayer(map.getKillerSpawn())
                }
            }
        }.runTaskLater(DeadByDayLightJE.instance,100)
    }

    fun stop() {
        if (isStarted) {
            HandlerList.unregisterAll(GameEventListener(this))
            for (generator in generators) {
                generator.armorStand.remove()
            }
            removeTimer()
            gameTask.cancel()
            isStarted = false
        }

        with(DeadByDayLightJE.instance) {
            game = null
            logger.info("ゲームを終了しました")
            if (autoStart()) createGame()
        }
    }

    fun join(player: Player) {
        survivor[player.name] = Survivor(player)
        Bukkit.broadcastMessage("サバイバー: ${player.name} さんがゲームに参加しました")
    }

    fun joinInMid(player: Player) {
        survivor[player.name] = Survivor(player)
        survivor[player.name]?.initPlayer(map.getJail())
        Bukkit.broadcastMessage("${player.name} さんが途中参加しました")
        player.sendMessage("途中参加のため牢屋からスタートしました")
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

    fun getPlayer(player: Player): IGamePlayer? {
        if (survivor.contains(player.name)) return survivor[player.name]
        if (killers.contains(player.name)) return killers[player.name]
        return null
    }

    fun getSurvivors(): ArrayList<Player> {
        val players:ArrayList<Player> = arrayListOf()
        for (survivor in survivor.values) {
            players.add(survivor.player)
        }
        return players
    }

    fun getKillers(): ArrayList<Player> {
        val players:ArrayList<Player> = arrayListOf()
        for (killer in killers.values) {
            players.add(killer.player)
        }
        return players
    }

    fun setKiller(player: Player) {
        if (survivor.contains(player.name)) leave(player)
        killers[player.name] = Killer(player)
        Bukkit.broadcastMessage("${player.name} さんがキラーになりました")
    }

    fun isKiller(player: Player): Boolean = getKillers().contains(player)

    fun isSurvivor(player: Player): Boolean = getSurvivors().contains(player)

    fun removeTimer() = gameTask.timeBar.removeAll()
}