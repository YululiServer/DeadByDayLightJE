package com.github.rain1208.deadbydaylightje.maps

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import kotlin.random.Random

class Map {
    private val spawnPoint = arrayListOf<Location>()
    private val killerSpawn = arrayListOf<Location>()
    val generatorPoint = arrayListOf<Location>()
    private val fishPoint = arrayListOf<Location>()
    private val jailPoint = arrayListOf<Location>()
    private val lobbyPoint = arrayListOf<Location>()
    val leverPoint = arrayListOf<Location>()
    val gateOpen = arrayListOf<Location>()
    val respawnBlock: Location

    var world: World

    init {
        val config = DeadByDayLightJE.instance.config
        world = Bukkit.getWorld(config.getString("world.name") ?: "world") ?: Bukkit.getWorld("world")

        for (spawn in config.getStringList("world.spawn")) {
            val pos = spawn.split(",").map { it.toDouble() }
            spawnPoint.add(Location(world,pos[0],pos[1],pos[2],0f,0f))
        }
        for (spawn in config.getStringList("world.killerSpawn")) {
            val pos = spawn.split(",").map { it.toDouble() }
            killerSpawn.add(Location(world,pos[0],pos[1],pos[2],0f,0f))
        }
        for (generator in config.getStringList("world.generator")) {
            val pos = generator.split(",").map { it.toDouble() }
            generatorPoint.add(Location(world,pos[0],pos[1],pos[2]))
        }
        for (fish in config.getStringList("world.fish")) {
            val pos = fish.split(",").map { it.toDouble() }
            fishPoint.add(Location(world,pos[0],pos[1],pos[2]))
        }
        for (lever in config.getStringList("world.gateLever")) {
            val pos = lever.split(",").map { it.toDouble() }
            leverPoint.add(Location(world,pos[0],pos[1],pos[2]))
        }
        for (gate in config.getStringList("world.gateOpen")) {
            val pos = gate.split(",").map { it.toDouble() }
            gateOpen.add(Location(world,pos[0],pos[1],pos[2]))
        }

        val jail = config.getString("world.jail").split(",").map { it.toDouble() }
        jailPoint.add(Location(world,jail[0],jail[1],jail[2]))

        val lobby = config.getString("world.lobby").split(",").map { it.toDouble() }
        lobbyPoint.add(Location(world,lobby[0],lobby[1],lobby[2]))

        val respawn = config.getString("world.respawnBlock").split(",").map { it.toDouble() }
        respawnBlock = Location(world,respawn[0],respawn[1],respawn[2])
    }

    fun getSpawn():Location = spawnPoint[Random.nextInt(spawnPoint.size)]
    fun getKillerSpawn(): Location = killerSpawn[Random.nextInt(killerSpawn.size)]
    fun getFish(): Location = fishPoint[Random.nextInt(fishPoint.size)]
    fun getJail(): Location = jailPoint[0]
    fun getLobby(): Location = lobbyPoint[0]

    fun getGate(pos: Location): Location = gateOpen[leverPoint.indexOf(pos)]
}