package com.github.rain1208.deadbydaylightje.animate

import org.bukkit.entity.ArmorStand
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class ThrowAxe(private val stand: ArmorStand): BukkitRunnable() { //お遊び(ハントレスの投げる斧)
    override fun run() {
        val oldRot = stand.rightArmPose
        val newRot = oldRot.add(0.2,0.0,0.0)
        stand.rightArmPose = newRot

        val loc = Vector(stand.location.direction.x,stand.location.direction.y,stand.location.direction.z)
        val pos = stand.location.add(loc)
        stand.teleport(pos)

        if (stand.location.add(0.0,0.8,0.0).block.type.isSolid) {
            cancel()
            stand.remove()
        }

        for (player in stand.location.world?.players!!) {
            val hitP = player.location.add(stand.width/2,0.0,stand.width/2)
            if (stand.location.distance(hitP) <= 0.8) {
                player.damage(5.0)
                cancel()
                stand.remove()
            } else if (stand.location.add(0.0,-1.0,0.0).distance(hitP) <= 0.8) {
                player.damage(5.0)
                cancel()
                stand.remove()
            }
        }
    }
}