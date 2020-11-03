package com.github.rain1208.deadbydaylightje

import com.github.rain1208.deadbydaylightje.commands.*
import com.github.rain1208.deadbydaylightje.game.Game
import com.github.rain1208.deadbydaylightje.utils.SQLite
import org.bukkit.Bukkit
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
            "dstatus" to GameStatus,
            "oni" to SetKiller,
            "join" to JoinGame,
            "leave" to LeaveGame,
            "config" to Config
    )

    var game: Game? = null

    override fun onEnable() {
        instance = this
        server.pluginManager.registerEvents(EventListener(),this)

        //Configの追加
        saveDefaultConfig()

        //コマンドの登録
        registerCommands()

        //ゲームデータを保存するデータベース
        //SQLite(this)

        if (autoStart()) createGame()
    }

    fun autoStart(): Boolean = config.getBoolean("auto-create-game")


    fun createGame() {
        if (game == null) {
            game = Game()
            logger.info("新しくゲームが作成されました")
            for (player in Bukkit.getOnlinePlayers()) {
                game?.join(player)
            }
        } else {
            logger.info("ゲームは既に作られています")
        }
    }

    fun forceStopGame() {
        game?.stop()
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
            } ?: logger.warning("$name が読み込めませんでした")
        }
    }

    override fun onDisable() {
        logger.info("プラグインが無効化されました")
    }
}