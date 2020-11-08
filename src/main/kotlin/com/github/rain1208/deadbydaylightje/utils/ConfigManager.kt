package com.github.rain1208.deadbydaylightje.utils

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import org.bukkit.configuration.Configuration

class ConfigManager(private val config: Configuration) {
    fun set(key: String, value: String) {
        config.set("world.$key", value)
        DeadByDayLightJE.instance.saveConfig()
    }

    fun add(key: String, value: String) {
        val list = config.getStringList("world.$key")
        list.add(value)
        config.set("world.$key",list)
        DeadByDayLightJE.instance.saveConfig()
    }
}