package com.github.rain1208.deadbydaylightje2.utils

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

object InventorySerialization {
    fun playerInventoryToBase64(inventory: PlayerInventory): Array<String> {
        val content = toBase64(inventory)
        val armor = itemStackArrayToBase64(inventory.armorContents)

        return arrayOf(content, armor)
    }

    fun playerInventoryFromBase64(data: Array<String>): PlayerInventory {
        val (content, armor) = data
        val inventory = Bukkit.createInventory(null, InventoryType.PLAYER) as PlayerInventory

        inventory.contents = fromBase64(content)
        inventory.armorContents = itemStackArrayFromBase64(armor)

        return inventory
    }

    private fun itemStackArrayToBase64(items: Array<ItemStack?>): String {
        return try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)

            dataOutput.writeInt(items.size)


            for (i in items.indices) {
                dataOutput.writeObject(items[i])
            }

            dataOutput.close()
            Base64Coder.encodeLines(outputStream.toByteArray())
        } catch (e: java.lang.Exception) {
            throw IllegalStateException("アイテムを保存できませんでした.", e)
        }
    }

    private fun toBase64(inventory: Inventory): String {
        return try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)

            dataOutput.writeInt(inventory.size)

            for (i in 0 until inventory.size) {
                dataOutput.writeObject(inventory.getItem(i))
            }

            dataOutput.close()
            Base64Coder.encodeLines(outputStream.toByteArray())
        } catch (e: Exception) {
            throw IllegalStateException("アイテムを保存できませんでした",e)
        }
    }

    private fun fromBase64(data: String): Array<ItemStack> {
        try {
            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
            val dataInput = BukkitObjectInputStream(inputStream)
            val inventory = mutableListOf<ItemStack>()

            for (i in 0 until inventory.size) {
                inventory.add(i, dataInput.readObject() as ItemStack)
            }

            dataInput.close()
            return inventory.toTypedArray()
        } catch (e: ClassNotFoundException) {
            throw IOException("デコード出来ませんでした", e)
        }
    }

    private fun itemStackArrayFromBase64(data: String?): Array<ItemStack> {
        try {
            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
            val dataInput = BukkitObjectInputStream(inputStream)
            val items = mutableListOf<ItemStack>()

            for (i in items.indices) {
                items[i] = dataInput.readObject() as ItemStack
            }
            dataInput.close()
            return items.toTypedArray()
        } catch (e: ClassNotFoundException) {
            throw IOException("デコード出来ませんでした", e)
        }
    }
}