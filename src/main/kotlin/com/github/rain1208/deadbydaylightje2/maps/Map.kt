package com.github.rain1208.deadbydaylightje2.maps

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.maps.resource.Generator
import com.github.rain1208.deadbydaylightje2.maps.resource.Hook
import com.github.rain1208.deadbydaylightje2.maps.resource.Lever
import com.github.rain1208.deadbydaylightje2.utils.ConfigManager
import org.bukkit.Location
import org.bukkit.World
import java.util.*

class Map(val world: World, mapName: String) {
    private val config = DeadByDayLightJE2.instance.configManager.get(ConfigManager.MAPS).config

    val spawn: Location
    val generators: MutableList<Generator> = mutableListOf()
    val levers: MutableList<Lever> = mutableListOf()
    val hooks: MutableList<Hook> = mutableListOf()

    fun randomHook() = hooks[Random().nextInt(hooks.size)]

    init {
        spawn = getLocation("${mapName}.spawn")
        for (generator in getLocationList("${mapName}.generators")) {
            generators.add(Generator(generator))
        }
        for (lever in getLocationList("${mapName}.levers")) {
            levers.add(Lever(lever))
        }
        for (hook in getLocationList("${mapName}.hooks")) {
            hooks.add(Hook(hook))
        }
    }

    private fun getLocation(path: String): Location {
        val data = config.getString(path)
        return if (data != null) {
            val pos = data.split(",").map { it.toDouble() }
            Location(world, pos[0], pos[1], pos[2])
        } else {
            Location(world, 0.0, 0.0, 0.0)
        }
    }

    private fun getLocationList(path: String):List<Location> {
        val data = config.getStringList(path)
        val list = mutableListOf<Location>()

        for (pos in data) {
            val p = pos.split(",").map { it.toDouble() }
            list.add(Location(world, p[0], p[1], p[2]))
        }

        return list
    }
}