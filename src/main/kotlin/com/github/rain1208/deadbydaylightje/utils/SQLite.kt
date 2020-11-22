package com.github.rain1208.deadbydaylightje.utils

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import java.io.File
import java.lang.RuntimeException
import java.sql.*

class SQLite(private val plugin: DeadByDayLightJE) {
    private val file = File(plugin.dataFolder.absolutePath + File.separator + "GameData.db")

    private lateinit var connection:Connection

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
        } catch (e: SQLException) {
            e.printStackTrace()
            return connect(recursions+1)
        }
        plugin.logger.info("データベースを読み込みました")

        val statement = connection.createStatement()
        statement.execute(
                "CREATE TABLE if not exists player_data(name Text not null primary key, survivor_count Integer, killer_count Integer, escape_count Integer, killer_win Integer)"
        )
        statement.close()
    }

    fun dataExists(name: String): Boolean {
        val ps = connection.prepareStatement("SELECT * from player_data WHERE name = ?")
        ps.setString(1, name)

        val rs = ps.executeQuery()

        if (rs.next()) {
            ps.close()
            return true
        }
        ps.close()
        return false
    }

    fun addPlayer(name: String) {
        val ps = connection.prepareStatement("INSERT into player_data values(?, 0, 0, 0, 0)")
        ps.setString(1, name)
        ps.execute()
        ps.close()

        println("$name のデータが作成されました")
    }

    fun addSurvivorCount(name: String) {
        val cps = connection.prepareStatement("SELECT survivor_count from player_data WHERE name = ?")
        cps.setString(1, name)
        val rs = cps.executeQuery()
        val count = rs.getInt(1)

        val ps = connection.prepareStatement("UPDATE player_data set survivor_count = ? WHERE name = ?")
        ps.setInt(1, count + 1)
        ps.setString(2, name)
        ps.execute()

        cps.close()
        ps.close()
    }

    fun addKillerCount(name: String) {
        val cps = connection.prepareStatement("SELECT killer_count from player_data WHERE name = ?")
        cps.setString(1, name)
        val rs = cps.executeQuery()
        val count = rs.getInt(1)

        val ps = connection.prepareStatement("UPDATE player_data set killer_count = ? WHERE name = ?")
        ps.setInt(1, count + 1)
        ps.setString(2, name)
        ps.execute()

        cps.close()
        ps.close()
    }

    fun addEscapeCount(name: String) {
        val cps = connection.prepareStatement("SELECT escape_count from player_data WHERE name = ?")
        cps.setString(1, name)
        val rs = cps.executeQuery()
        val count = rs.getInt(1)

        val ps = connection.prepareStatement("UPDATE player_data set escape_count = ? WHERE name = ?")
        ps.setInt(1, count + 1)
        ps.setString(2, name)
        ps.execute()

        cps.close()
        ps.close()
    }

    fun addKillerWin(name: String) {
        val cps = connection.prepareStatement("SELECT killer_win from player_data WHERE name = ?")
        cps.setString(1, name)
        val rs = cps.executeQuery()
        val count = rs.getInt(1)

        val ps = connection.prepareStatement("UPDATE player_data set killer_win = ? WHERE name = ?")
        ps.setInt(1, count + 1)
        ps.setString(2, name)
        ps.execute()

        cps.close()
        ps.close()
    }

    fun close() = connection.close()
}