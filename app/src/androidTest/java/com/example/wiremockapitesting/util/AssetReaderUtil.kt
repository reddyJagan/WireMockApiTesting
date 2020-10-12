package com.example.wiremockapitesting.util

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object AssetReaderUtil {
    fun asset(context: Context, assetPath: String): String {
        return try {
            val buf = StringBuilder()
            val inputStream =
                context.assets.open("body_files/$assetPath")
            inputStreamToString(inputStream, "UTF-8")
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private const val BUFFER_SIZE = 4 * 1024

    @Throws(IOException::class)
    fun inputStreamToString(
        inputStream: InputStream?,
        charsetName: String?
    ): String {
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, charsetName)
        val buffer = CharArray(BUFFER_SIZE)
        var length: Int
        while (reader.read(buffer).also { length = it } != -1) {
            builder.append(buffer, 0, length)
        }
        return builder.toString()
    }
}