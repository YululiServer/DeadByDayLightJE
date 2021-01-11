package com.github.rain1208.deadbydaylightje2.game

import com.github.rain1208.deadbydaylightje2.DeadByDayLightJE2
import com.github.rain1208.deadbydaylightje2.utils.ConfigManager

object GameSetting {
    private val custom = DeadByDayLightJE2.instance.configManager.get(ConfigManager.SETTINGS)
    val rescueTime = custom.config.getDouble("rescue-time")
}