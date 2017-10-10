package me.arakmmis.contactsapp.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

object ByteArrayUtil {

    fun fromFile(file: File?): ByteArray? {
        val bytesArray = ByteArray(file?.length()!!.toInt())

        val fis = FileInputStream(file)
        fis.read(bytesArray)
        fis.close()

        return bytesArray
    }

    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}