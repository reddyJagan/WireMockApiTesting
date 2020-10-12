package com.example.wiremockapitesting.wiremock

import android.content.res.AssetManager
import com.example.wiremockapitesting.wiremock.AndroidAssetsReadOnlyFileSource
import com.github.tomakehurst.wiremock.common.BinaryFile
import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.common.TextFile
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.*

/**
 * WARNING: This is currently be developed and not working yet.
 */
class AndroidAssetsReadOnlyFileSource @JvmOverloads constructor(
    private val assetManager: AssetManager,
    private val assetPath: String = ""
) : FileSource {
    var logger =
        LoggerFactory.getLogger(AndroidAssetsReadOnlyFileSource::class.java)

    override fun getBinaryFileNamed(name: String): BinaryFile {
        return AndroidAssetsBinaryFile(assetManager, "$assetPath/$name")
    }

    override fun getTextFileNamed(s: String): TextFile? {
        return null
    }

    override fun createIfNecessary() {
        return
    }

    override fun child(name: String): FileSource {
        return AndroidAssetsReadOnlyFileSource(assetManager, name)
    }

    override fun getPath(): String {
        return assetPath
    }

    override fun getUri(): URI? {
        return null
    }

    override fun listFilesRecursively(): List<TextFile>? {
        logger.debug("list files recursively")
        try {
            val nameList = assetManager.list(assetPath)
            val textFiles: MutableList<TextFile> =
                ArrayList()
            for (name in nameList!!) {
                val textFile: TextFile =
                    AndroidAssetsBinaryFile(assetManager, "$assetPath/$name")
                textFiles.add(textFile)
            }
            return textFiles
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun writeTextFile(name: String, contents: String) {
        return
    }

    override fun writeBinaryFile(
        name: String,
        contents: ByteArray
    ) {
        return
    }

    override fun exists(): Boolean {
        var exists = false
        var inputStream: InputStream? = null
        try {
            inputStream = assetManager.open(assetPath)
            exists = true
        } catch (e: IOException) {
            e.printStackTrace()
            try {
                val strings = assetManager.list(assetPath)
                logger.debug(strings.toString())
                exists = true
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return exists
    }

    override fun deleteFile(s: String) {}

}