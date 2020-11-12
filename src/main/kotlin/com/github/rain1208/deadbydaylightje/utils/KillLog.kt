package com.github.rain1208.deadbydaylightje.utils

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar

class KillLog {
    val bar: BossBar = Bukkit.createBossBar("",BarColor.RED,BarStyle.SOLID)

    fun setTitle(string: String) {
        bar.title = string
    }

    fun show() {
        for (player in Bukkit.getOnlinePlayers()) {
            bar.addPlayer(player)
        }
        bar.isVisible = true
    }

    fun remove() {
        bar.isVisible = false
        bar.removeAll()
    }
}