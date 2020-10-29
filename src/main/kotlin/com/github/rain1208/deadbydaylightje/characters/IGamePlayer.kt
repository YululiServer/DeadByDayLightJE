package com.github.rain1208.deadbydaylightje.characters

import com.github.rain1208.deadbydaylightje.maps.Generator
import org.bukkit.Location
import org.bukkit.entity.Player

interface IGamePlayer {

    val player:Player

    //リスポーン時やゲームの開始時など
    //体力　空腹度 アイテムの再配布
    fun initPlayer(spawn: Location)

    fun onUse(generator: Generator)
}