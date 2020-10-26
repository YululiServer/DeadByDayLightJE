package com.github.rain1208.deadbydaylightje.characters

import org.bukkit.Location

interface IGamePlayer {

    //リスポーン時やゲームの開始時など
    //体力　空腹度 アイテムの再配布
    fun initPlayer(spawn: Location)
}