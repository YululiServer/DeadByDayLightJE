package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.Bukkit
import org.bukkit.Location

class Map {
    val spawnPoint = arrayListOf<Location>()
    val killerSpawn = arrayListOf<Location>()
    val generatorPoint = arrayListOf<Location>()

    init {
        val config = DeadByDayLightJE.instance.config
        val world = Bukkit.getWorld(config.getString("world.name") ?: "world") ?: Bukkit.getWorld("world")

        val spawnList = config.getStringList("world.spawn")
        for (spawn in spawnList) {
            val pos = spawn.split(",").map { it.toDouble() }
            spawnPoint.add(Location(world,pos[0],pos[1],pos[2],0f,0f))
        }

        val pos = config.getString("world.killerSpawn")?.split(",")?.map { it.toDouble() } ?: "0,10,0".split(",").map { it.toDouble() }
        killerSpawn.add(Location(world,pos[0],pos[1],pos[2],0f,0f))
    }

    fun getSpawn():Location = spawnPoint.random()

    fun getKillerSpawn(): Location = killerSpawn[0]
}
