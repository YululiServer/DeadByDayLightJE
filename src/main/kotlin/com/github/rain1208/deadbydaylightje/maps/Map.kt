package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.Bukkit
import org.bukkit.Location

class Map {
    private val spawnPoint = arrayListOf<Location>()
    private val killerSpawn = arrayListOf<Location>()
    val generatorPoint = arrayListOf<Location>()

    init {
        val config = DeadByDayLightJE.instance.config
        val world = Bukkit.getWorld(config.getString("world.name") ?: "world") ?: Bukkit.getWorld("world")

        for (spawn in config.getStringList("world.spawn")) {
            val pos = spawn.split(",").map { it.toDouble() }
            spawnPoint.add(Location(world,pos[0],pos[1],pos[2],0f,0f))
        }
        for (spawn in config.getStringList("world.killerSpawn")) {
            val pos = spawn.split(",").map { it.toDouble() }
            killerSpawn.add(Location(world,pos[0],pos[1],pos[2],0f,0f))
        }
    }

    fun getSpawn():Location = spawnPoint.random()

    fun getKillerSpawn(): Location = killerSpawn.random()
}
