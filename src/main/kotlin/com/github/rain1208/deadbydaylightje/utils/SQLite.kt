package com.github.rain1208.deadbydaylightje.utils

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import java.io.File
import java.lang.RuntimeException
import java.sql.*

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
        statement.execute(
                "CREATE TABLE if not exists PlayerData(name Text not null primary key,"+
                " survivor_count Integer, killer_count Integer, survivor_win Integer, killer_win Integer)"
        )
    }

    fun dataExists(name: String): Boolean {
        val rs = statement.executeQuery("SELECT * FROM PlayerData WHERE name LIKE $name")
        println(rs)
        return true
    }

    fun addPlayer(name: String) {
        statement.execute("INSERT INTO PlayerData(name, survivor_count, killer_count, survivor_win, killer_win) values ($name, 0, 0, 0,0)")
    }

    fun close() {
        statement.close()
        connection.close()
    }
}