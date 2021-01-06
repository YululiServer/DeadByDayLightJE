package com.github.rain1208.deadbydaylightje2.utils

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2

class ConfigManager {
    companion object {
        const val MAPS = 0
        const val SETTINGS = 1
    }

    private val configs:MutableList<CustomConfig> = mutableListOf()

    init {
        configs.add(MAPS, CustomConfig(DeadByDayLightJE2.instance, "maps.yml"))
        configs.add(SETTINGS, CustomConfig(DeadByDayLightJE2.instance, "settings.yml"))
    }

    fun get(id: Int) = configs[id]
}