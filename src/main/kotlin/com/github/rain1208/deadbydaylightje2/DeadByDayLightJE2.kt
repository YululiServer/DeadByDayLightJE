package com.github.rain1208.deadbydaylightje2

import com.github.rain1208.deadbydaylightje2.commands.*
import com.github.rain1208.deadbydaylightje2.game.Game
import com.github.rain1208.deadbydaylightje2.maps.MapManager
import com.github.rain1208.deadbydaylightje2.utils.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class DeadByDayLightJE2 : JavaPlugin() {
    companion object {
        lateinit var instance: DeadByDayLightJE2
        private set

        const val MESSAGE_PREFIX = "[DeadByDayLight]"
    }

    lateinit var configManager: ConfigManager
    lateinit var mapManager: MapManager

    var game:Game? = null


    private val commands = mapOf(
        "autocreate" to AutoCreateGame,
        "start" to StartGameCommand,
        "join" to JoinGameCommand,
        "leave" to LeaveGameCommand,
        "map" to ChooseMapCommand
    )

    override fun onEnable() {
        instance = this
        server.pluginManager.registerEvents(EventListener(), this)

        configManager = ConfigManager()
        mapManager = MapManager()

        logger.info("===============ワールド===============")
        mapManager.loadAllMap()

        logger.info("===============コマンド===============")
        registerCommands()

        if (autoStart()) createGame()
    }

    fun autoStart(): Boolean = configManager.get(ConfigManager.SETTINGS).config.getBoolean("auto-create-game")

    fun createGame() {
        if (game == null) {
            game = Game()
            logger.info("新しくゲームが作られました")
            for (player in Bukkit.getOnlinePlayers()) {
                game?.join(player)
            }
        } else {
            logger.info("ゲームは既に作られています")
        }
    }

    private fun registerCommands()
    {
        commands.forEach{(name, executor) ->
            getCommand(name)?.run {
                setExecutor(executor)
                logger.info("$name を読み込みました")
            } ?: logger.warning("$name が読み込めませんでした")
        }
    }

    override fun onDisable() {
        game?.stop()
    }
}