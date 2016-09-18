package ru.aim.anotheryetbashclient.service

import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import ru.aim.anotheryetbashclient.R
import ru.aim.anotheryetbashclient.helper.L
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.view.ShareType
import java.io.File
import java.io.FileOutputStream
import java.util.*

private const val TAG = "sharing"

interface SharingService {
    fun share(type: ShareType, quote: Quote)
}

class SharingServiceImpl(val context: Context) : SharingService {

    private val list = HashMap<ShareType, (Context, Quote) -> Unit>()

    init {
        addShareType(ShareType.TEXT) { context: Context, quote: Quote ->
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, quote.text)
            context.startActivity(Intent.createChooser(sharingIntent, context.resources.getString(R.string.share_desc)))
        }

        addShareType(ShareType.LINK) { context: Context, quote: Quote ->
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val url = "${context.getString(R.string.bash_url)}/${cleanId(quote.publicId!!)}"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
            context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_desc)))
        }

        addShareType(ShareType.BUFFER) { context: Context, quote: Quote ->
            val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            manager.primaryClip = ClipData.newPlainText(quote.publicId, quote.text)
            Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }

        addShareType(ShareType.IMAGE) { context: Context, quote: Quote ->
            TODO()
        }
    }

    private fun addShareType(type: ShareType, f: (Context, Quote) -> Unit) {
        list.put(type, f)
    }

    override fun share(type: ShareType, quote: Quote) {
        val sharingAction = list[type] ?: throw UnsupportedOperationException("Unsupported sharing type ${type.name}")
        sharingAction.invoke(context, quote)
    }
}

internal fun cleanId(id: String): String {
    if (TextUtils.isEmpty(id)) {
        val cur = Date()
        return "" + cur.time
    } else
        return id.replace("#", "")
}

internal fun getFile(context: Context, fileName: String): File {
    val root = Environment.getExternalStorageDirectory().toString()
    val myDir = File(root + File.separator + context.getString(R.string.app_name))
    return File(myDir, fileName)
}

internal fun saveBitmap(context: Context, bitmap: Bitmap, fileName: String): String {
    val root = Environment.getExternalStorageDirectory().toString()
    val myDir = File(root + File.separator + context.getString(R.string.app_name))
    myDir.mkdirs()
    val file = File(myDir, fileName)
    if (file.exists()) {
        file.delete()
    }
    try {
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return file.path
}

fun byteSizeOf(bitmap: Bitmap): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        return bitmap.allocationByteCount
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
        return bitmap.byteCount
    } else {
        return bitmap.rowBytes * bitmap.height
    }
}

internal fun insertImage(context: Context, cr: ContentResolver, source: Bitmap,
                         title: String, description: String): String {

    val path = saveBitmap(context, source, title)

    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, title)
    values.put(MediaStore.Images.Media.DESCRIPTION, description)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.Images.Media.DATE_TAKEN, Date().time)
    values.put(MediaStore.Images.Media.ORIENTATION, 0)
    values.put(MediaStore.Images.Media.DATA, path)
    values.put(MediaStore.Images.Media.SIZE, byteSizeOf(source))

    var url: Uri? = null
    var stringUrl: String? = null    /* value to be returned */

    try {
        url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    } catch (e: Exception) {
        L.e(TAG, "Failed to insert image", e)
        if (url != null) {
            cr.delete(url, null, null)
            url = null
        }
    }

    if (url != null) {
        stringUrl = url.toString()
    }

    return ""
}
