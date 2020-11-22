package com.github.rain1208.deadbydaylightje.game

import com.github.rain1208.deadbydaylightje.DeadByDayLightJE
import com.github.rain1208.deadbydaylightje.animation.ThrowAxe
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class GameEventListener(val game: Game): Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) = game.join(event.player)

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) = game.leave(event.player)

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.type == Material.IRON_AXE) {
            val stand = event.player.world.spawn(event.player.location.add(event.player.location.direction), ArmorStand::class.java)
            stand.isVisible = false
            stand.setGravity(false)
            stand.setArms(true)
            stand.equipment?.itemInMainHand = ItemStack(Material.IRON_AXE)
            stand.rightArmPose.z = 330.0

            event.itemDrop.remove()
            val axe = event.player.inventory.itemInMainHand
            event.player.inventory.remove(axe)

            val instance = DeadByDayLightJE.instance

            val animate = ThrowAxe(stand,event.player)
            animate.runTaskTimer(instance,0,1)

            object : BukkitRunnable() {
                override fun run() {
                    if (!animate.isCancelled) Bukkit.getScheduler().cancelTask(animate.taskId)
                    if(!stand.isDead) stand.remove()
                    event.player.inventory.addItem(axe)
                }
            }.runTaskLater(instance,200)
        }
    }

    @EventHandler
    fun clickBlock(event: BlockDamageEvent) {
        if (!game.deadSurvivor.contains(event.player.name)) return
        if (event.block.location == game.map.respawnBlock) {
            game.respawn(event.player)
        }
    }

    @EventHandler
    fun damageByEntity(event: EntityDamageByEntityEvent) {
        event.damage = 0.0
        val atk = event.damager
        val dmg = event.entity
        if (atk !is Player) return
        if (dmg !is Player) return

        if (game.isSurvivor(dmg) && game.isSurvivor(atk)) {
            event.isCancelled = true
            return
        }
        if (game.isSurvivor(atk) && game.isKiller(dmg)) {
            event.isCancelled = true
            return
        }

        if(game.isKiller(atk) && game.isSurvivor(dmg)) {
            val surv = game.survivor[dmg.name]
            if (surv?.isHooked!!) return
            surv.addDamage()
            if (surv.hp <= 0) {
                surv.isHooked = true
                game.setHook(surv)
                game.killLog(dmg, atk)
            } else {
                dmg.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,10*20,1))
                dmg.addPotionEffect(PotionEffect(PotionEffectType.SPEED,7*20,2))
            }
        }
    }
}