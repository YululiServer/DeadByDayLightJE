package com.github.rain1208.deadbydaylightje

import com.github.rain1208.deadbydaylightje.commands.EndGame
import com.github.rain1208.deadbydaylightje.commands.SetKiller
import com.github.rain1208.deadbydaylightje.commands.StartGame
import com.github.rain1208.deadbydaylightje.commands.StopGame
import com.github.rain1208.deadbydaylightje.game.Game
import org.bukkit.plugin.java.JavaPlugin

class DeadByDayLightJE : JavaPlugin() {
    companion object {
        lateinit var instance: DeadByDayLightJE
        private set
    }

    private val commands = mapOf(
            "dstart" to StartGame,
            "d_end" to EndGame,
            "dstop" to StopGame,
            "dkiller" to SetKiller
    )

    var game: Game? = null


    override fun onEnable() {
        instance = this
        server.pluginManager.registerEvents(EventListener(),this)

        //Configの追加
        saveDefaultConfig()

        //コマンドの登録
        registerCommands()

        if (autoStart()) createGame()
    }

    fun autoStart(): Boolean = config.getBoolean("auto-create-game")


    fun createGame() {
        if (game == null) {
            game = Game()
            logger.info("新しくゲームが作成されました")
        } else {
            logger.info("ゲームは既に作られています")
        }
    }

    fun forceStopGame() {
        game = null
        server.broadcastMessage("ゲームを強制終了します")
        if (autoStart()) createGame()
    }

    private fun registerCommands()
    {
        commands.forEach{(name, executor) ->
            getCommand(name)?.run {
                setExecutor(executor)
                logger.info("$name を読み込みました")
            } ?: logger.info("$name が読み込めませんでした")
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}