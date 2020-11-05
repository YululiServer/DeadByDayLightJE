package com.github.rain1208.deadbydaylightje.utils

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import java.io.File
import java.lang.RuntimeException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class SQLite(val plugin: DeadByDayLightJE) {
    private val file = File(plugin.dataFolder.absolutePath + File.separator + "GameData.db")

    lateinit var connection:Connection
    lateinit var statement: Statement

    init {
        if (!file.exists()) {
            file.createNewFile()
            plugin.logger.info("GameData.dbが作成されました")
        }
        connect()
    }

    private fun connect(recursions: Int = 0) {
        if (recursions >= 2) {
            throw RuntimeException("tried to get 3 times. giving up.")
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.absoluteFile)
            statement = connection.createStatement()
        } catch (e: SQLException) {
            e.printStackTrace()
            return connect(recursions+1)
        }
        plugin.logger.info("データベースを読み込みました")
        statement.execute("create table if not exists PlayerData(name Text not null primary key, survivor_count Integer, killer_count Integer, survivor_win Integer, killer_win Integer)")
    }

    fun close() {
        statement.close()
        connection.close()
    }
}