package com.github.rain1208.deadbydaylightje2.maps

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.utils.ConfigManager
import org.bukkit.Bukkit

class MapManager {
    val maps: MutableMap<String, Map> = mutableMapOf()

    fun getMap(name: String): Map? = maps[name]

    fun loadAllMap() {
        val customConfig = DeadByDayLightJE2.instance.configManager.get(ConfigManager.MAPS)
        for (mapName in customConfig.config.getStringList("mapList")) {
            loadMap(mapName)
        }
    }

    fun loadMap(mapName: String) {
        val world = Bukkit.getWorld(mapName)
        if (world != null) {
            maps[mapName] = Map(world, mapName)
            DeadByDayLightJE2.instance.logger.info("ワールド : $mapName を読み込みました")
        } else {
            DeadByDayLightJE2.instance.logger.warning("ワールド : $mapName を読み込みませんでした")
        }
    }
}