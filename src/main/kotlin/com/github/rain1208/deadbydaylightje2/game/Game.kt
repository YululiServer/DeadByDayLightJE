package com.github.rain1208.deadbydaylightje2.game

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.characters.IGamePlayer
import com.github.rain1208.deadbydaylightje2.characters.Killer
import com.github.rain1208.deadbydaylightje2.characters.Survivor
import com.github.rain1208.deadbydaylightje2.maps.Map
import com.github.rain1208.deadbydaylightje2.utils.FootPointParticles
import com.github.rain1208.deadbydaylightje2.utils.GameTimer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class Game {
    companion object {
        const val PRE_PHASE = -1
        const val GAME_PHASE = 0
        const val LEVER_PHASE = 1
        const val LAST_PHASE = 2
    }

    val survivors: MutableMap<UUID, Survivor> = mutableMapOf()
    val killers: MutableMap<UUID, Killer> = mutableMapOf()

    val deadSurvivor: MutableMap<UUID, Survivor> = mutableMapOf()
    val escapeSurvivor: MutableMap<UUID, Survivor> = mutableMapOf()

    private val gameTask = GameTask(this)
    private val gameTimer = GameTimer(this)
    private val footPointParticle = FootPointParticles(this)

    lateinit var map: Map

    var phase = PRE_PHASE

    fun startCountStart() {
        if (!::map.isInitialized) {
            Bukkit.broadcastMessage("mapが指定されていません")
            return
        }
        object : BukkitRunnable() {
            var count = 10
            override fun run() {
                if (count == 0) {
                    start()
                    cancel()
                    return
                }
                broadcastMessage("ゲーム開始まで... $count")
                count--
            }
        }.runTaskTimer(DeadByDayLightJE2.instance,0,20)
    }

    fun start() {
        startTask()
        worldLoad()

        gameTimer.timeBar.isVisible = true

        for (player in Bukkit.getOnlinePlayers()) {
            getPlayer(player.uniqueId)?.initPlayer(map.spawn)
            player.sendTitle("ゲームスタート!!","",3, 20, 3)
        }
        phase = GAME_PHASE
    }

    private fun startTask() {
        val instance = DeadByDayLightJE2.instance
        gameTask.runTaskTimerAsynchronously(instance, 0,5)      //0.25秒
        gameTimer.runTaskTimerAsynchronously(instance, 0, 20)   // 1秒
        footPointParticle.runTaskTimer(instance, 0, 10)         // 0.5秒
    }

    private fun worldLoad() {

    }

    fun setMap(name: String) {
        val world = DeadByDayLightJE2.instance.mapManager.getMap(name)
        if (world != null) {
            map = world
            broadcastMessage("ワールド: $name が選択されました")
        } else {
            DeadByDayLightJE2.instance.logger.info("ワールドが見つかりませんでした")
        }
    }

    fun generatorRepairAllComplete() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.resetTitle()
            player.sendTitle("",ChatColor.GREEN.toString()+"すべての発電機の修理が完了した", 0, 40, 0)
        }
        phase = LEVER_PHASE
    }

    fun leverActivate() {
        broadcastMessage("脱出ゲートが開いた")

        if (phase == LAST_PHASE) return
        phase = LAST_PHASE
        gameTimer.time = 60
    }

    fun result() {

    }

    fun stop() {
        gameTimer.timeBar.removeAll()
        stopTask()
    }

    private fun stopTask() {
        if (phase == PRE_PHASE) return
        if (!gameTask.isCancelled) gameTask.cancel()
        if (!gameTimer.isCancelled) gameTimer.cancel()
        if (!footPointParticle.isCancelled) footPointParticle.cancel()
    }

    fun join(player: Player) {
        if (killers.contains(player.uniqueId)) leave(player)
        survivors[player.uniqueId] = Survivor(player)

        gameTimer.timeBar.addPlayer(player)

        broadcastMessage("サバイバー: ${player.name}が参加した")
    }

    fun leave(player: Player) {
        if (survivors.contains(player.uniqueId)) {
            survivors.remove(player.uniqueId)
            broadcastMessage("サバイバー: ${player.name}が退出した")
        }
        if (killers.contains(player.uniqueId)) {
            killers.remove(player.uniqueId)
            broadcastMessage("キラー: ${player.name}が退出した")
        }
    }

    fun getPlayer(uuid: UUID): IGamePlayer? {
        if (survivors.contains(uuid)) return survivors[uuid]
        if (killers.contains(uuid)) return killers[uuid]
        return null
    }

    fun getSurvivor(uuid: UUID): Survivor? {
        if (survivors.contains(uuid)) return survivors[uuid]
        return null
    }

    fun setKiller(player: Player) {
        leave(player)
        killers[player.uniqueId] = Killer(player)
    }

    fun getKiller(uuid: UUID): Killer? {
        if (killers.contains(uuid)) return killers[uuid]
        return null
    }

    fun broadcastMessage(message: String) =
        Bukkit.broadcastMessage(DeadByDayLightJE2.MESSAGE_PREFIX + message)
}