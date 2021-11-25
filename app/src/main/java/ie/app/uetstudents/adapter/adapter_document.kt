package ie.app.uetstudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.item_document.view.*
import java.util.zip.Inflater

class adapter_document(var listdocument : ArrayList<document>) : RecyclerView.Adapter<adapter_document.ViewHoldel>() {
    inner class ViewHoldel(var itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun onBinData(document: document)
        {
            itemview.item_document_name.text = document.name_documnet
            itemview.item_document_image.setImageResource(document.image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoldel {
        return ViewHoldel(
            LayoutInflater.from(parent.context).inflate(R.layout.item_document,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHoldel, position: Int) {
        return holder.onBinData(listdocument.get(position))
    }

    override fun getItemCount(): Int {
        return listdocument.size
    }
}