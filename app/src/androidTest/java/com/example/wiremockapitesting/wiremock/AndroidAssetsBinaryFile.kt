package com.example.wiremockapitesting.wiremock

import android.content.res.AssetManager
import com.github.tomakehurst.wiremock.common.TextFile
import org.apache.commons.io.IOUtils
import java.io.IOException

class AndroidAssetsBinaryFile(
    private val assetManager: AssetManager,
    private val assetPath: String
) :
    TextFile(null) {
    override fun readContents(): ByteArray? {
        try {
            val inputStream = assetManager.open(assetPath)
            return IOUtils.toByteArray(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    //    private void closeStream(InputStream stream) {
    //        if (stream != null) {
    //            try {
    //                stream.close();
    //            } catch (IOException var3) {
    //                throw new RuntimeException(var3);
    //            }
    //        }
    //    }
    override fun name(): String {
        return assetPath
    }

    override fun toString(): String {
        return name()
    }

}