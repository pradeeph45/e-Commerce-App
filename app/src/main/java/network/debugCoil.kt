package network

import android.content.Context
import coil.ImageLoader

object DebugCoil {

    fun createDebugImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(UnsafeOkHttpClient.getUnsafeOkHttpClient())
            .build()
    }
}