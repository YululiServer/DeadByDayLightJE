package com.github.rain1208.deadbydaylightje.game

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.EventListener
import com.github.rain1208.deadbydaylightje.characters.IGamePlayer
import com.github.rain1208.deadbydaylightje.characters.Killer
import com.github.rain1208.deadbydaylightje.characters.Survivor
import com.github.rain1208.deadbydaylightje.maps.Generator
import com.github.rain1208.deadbydaylightje.maps.Lever
import com.github.rain1208.deadbydaylightje.maps.Map
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.scheduler.BukkitRunnable

class Game {
    val survivor: MutableMap<String, Survivor> = mutableMapOf()
    val killers: MutableMap<String, Killer> = mutableMapOf()

    val deadSurvivor: MutableMap<String, Survivor> = mutableMapOf()
    val escapeSurvivor = arrayListOf<Survivor>()

    val generators: ArrayList<Generator> = arrayListOf()
    var generatorCount = 0

    val levers: ArrayList<Lever> = arrayListOf()

    lateinit var map:Map

    val gameTask = GameTask(this)

    var isStarted = false

    var isRepairAllComplete = false

    fun start() {
        if (killers.isEmpty()) {
            Bukkit.broadcastMessage("キラーがいないためゲームを開始できません")
            return
        }
        map = Map()

        for (loc in map.generatorPoint) {
            generators.add(Generator(loc.world.spawn(loc,ArmorStand::class.java)))
            generatorCount++
        }

        for (lever in map.leverPoint) {
            levers.add(Lever(lever.world.spawn(lever,ArmorStand::class.java)))
        }

        HandlerList.unregisterAll(DeadByDayLightJE.instance)

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

                    isStarted = true
                    gameTask.runTaskTimer(DeadByDayLightJE.instance,0,20)
                    gameTask.timeBar.createBar()
                    gameTask.timerStart()

                    startPlayer()

                    //夜に固定
                    map.world.time = 13800
                    map.world.setGameRuleValue("doDaylightCycle","false")

                } else {
                    Bukkit.broadcastMessage("ゲーム開始まで: $count"+"秒")
                    count--
                }
            }
        }.runTaskTimer(DeadByDayLightJE.instance,0,20)
    }

    fun startPlayer() {
        for (survivor in survivor.values) {
            survivor.initPlayer(map.getSpawn())
        }
        for (killer in killers.values) {
            killer.initPlayer(map.getKillerSpawn())
        }
    }

    fun stop() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.teleport(map.getLobby())
        }
        if (isStarted) {
            HandlerList.unregisterAll(DeadByDayLightJE.instance)
            DeadByDayLightJE.instance.server.pluginManager.registerEvents(EventListener(),DeadByDayLightJE.instance)
            for (generator in generators) {
                generator.armorStand.remove()
            }
            for (lever in levers) {
                lever.armorStand.remove()
            }
            removeTimer()
            gameTask.cancel()
            isStarted = false
        }

        with(DeadByDayLightJE.instance) {
            game = null
            if (autoStart()) createGame()
        }
    }

    fun result() {
        Bukkit.broadcastMessage("=".repeat(30))
        Bukkit.broadcastMessage(" ".repeat(9) + "ゲーム終了")
        Bukkit.broadcastMessage("=".repeat(30))
        for (player in Bukkit.getOnlinePlayers()) {
            if (escapeSurvivor.count() <= 0) {
                player.sendTitle("ゲーム終了!!!","キラー側の勝利",0,40,0)
            } else {
                player.sendTitle("ゲーム終了!!!","サバイバーが逃げ切った",0,40,0)
                var message = ""
                for (surv in escapeSurvivor) {
                    message += surv.player.name+ ", "
                }
                Bukkit.broadcastMessage(message)
            }
        }
        stop()
    }

    fun leverActivate() {
        Bukkit.broadcastMessage("ゲートが開きました")
        Location(map.world,-10.0,12.0,-62.0).block.type = Material.REDSTONE_BLOCK
    }

    fun setHook(survivor: Survivor) {
        survivor.player.teleport(map.getFish())
        gameTask.hookedSurvivor[survivor.player.name] = survivor
        Bukkit.broadcastMessage("${survivor.player.name}がフックにつられました")
    }

    fun goToJail(surv: Survivor) {
        survivor.remove(surv.player.name)
        deadSurvivor[surv.player.name] = surv
        Bukkit.broadcastMessage(surv.player.name +"が牢屋に送られました")
        surv.player.teleport(map.getJail())
    }

    fun respawn(player: Player) {
        if (generatorCount >= 3) {
            val surv = deadSurvivor[player.name]
            if (surv is Survivor) {
                survivor[player.name] = surv
                surv.initPlayer(map.getSpawn())
            }
        } else {
            setKiller(player)
        }
        deadSurvivor.remove(player.name)
    }

    fun repairAllComplete() {
        isRepairAllComplete = true
        Bukkit.broadcastMessage("発電機の修理がすべて完了しました")
    }

    fun join(player: Player) {
        if (isStarted) {
            gameTask.timeBar.addPlayer(player)
            deadSurvivor[player.name] = Survivor(player)
            deadSurvivor[player.name]?.initPlayer(map.getJail())
            Bukkit.broadcastMessage("${player.name} さんが途中参加しました")
            player.sendMessage("途中参加のため牢屋からスタートしました")
        } else {
            Bukkit.broadcastMessage("サバイバー: ${player.name} さんがゲームに参加しました")
            survivor[player.name] = Survivor(player)
        }
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