package ie.app.uetstudents.adapter

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.item_anh_write.view.*
import java.io.File

class adapter_anhwrite(var listanh: List<Uri>, var Clickdelete: OnclickItem_deleteanh) :
    RecyclerView.Adapter<adapter_anhwrite.Viewholder>() {
    inner class Viewholder(var itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun onBindata(uri: Uri) {
            val fileName = getSelectedFileName(uri, itemview.context)
            if(fileName?.contains(".pdf") == true) {
                Glide.with(itemView).load(R.drawable.pdf).into(itemview.item_anh)
            } else {
                itemview.item_anh.setImageURI(uri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter_anhwrite.Viewholder {
        return Viewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_anh_write, parent, false)
        )
    }

    override fun onBindViewHolder(holder: adapter_anhwrite.Viewholder, position: Int) {
        val anh: Uri = listanh[position]
        holder.onBindata(anh)
        holder.itemview.delete_anh.setOnClickListener {
            Clickdelete.CLickDelete(anh)
        }
    }

    override fun getItemCount(): Int {
        return listanh.size
    }

    private fun getSelectedFileName(selectFileUri: Uri, context: Context): String? {
        val uriString: String = selectFileUri.toString()
        val myFile = File(uriString)
        var displayName: String? = null
        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(selectFileUri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        return displayName
    }
}

interface OnclickItem_deleteanh {
    fun CLickDelete(anh: Uri)
}
