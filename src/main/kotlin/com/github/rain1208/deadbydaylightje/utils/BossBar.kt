package com.github.rain1208.deadbydaylightje.utils

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

class BossBar {
    val bar:org.bukkit.boss.BossBar = Bukkit.createBossBar("",BarColor.PURPLE,BarStyle.SEGMENTED_20)

    fun createBar():BossBar {
        bar.isVisible = true
        return this
    }

    fun addPlayer(player: Player) {
        bar.addPlayer(player)
    }

    fun removePlayer(player: Player) {
        bar.removePlayer(player)
    }

    fun removeAll() {
        bar.removeAll()
    }

    fun setTitle(title:String) {
        bar.title = title
    }

    fun setProgress(progress: Double) {
        if (progress < 0 || progress > 1) {
            bar.progress = 0.0
            return
        }
        bar.progress = progress
    }
}