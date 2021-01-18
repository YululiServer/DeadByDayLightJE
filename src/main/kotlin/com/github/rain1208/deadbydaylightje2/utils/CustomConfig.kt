package com.github.rain1208.deadbydaylightje2.utils

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.logging.Level

class CustomConfig(private val plugin: DeadByDayLightJE2, private val name: String) {
    private val file = File(plugin.dataFolder,name)
    val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)

    init {
        saveDefaultConfig()
    }

    fun save() {
        try {
            config.save(file)
        } catch (e: IOException) {
            plugin.logger.log(Level.SEVERE, "Failed to save configuration: $name", e)
        }
    }

    fun saveDefaultConfig() {
        if (!file.exists()) {
            plugin.saveResource(name, false)
        }
    }
}