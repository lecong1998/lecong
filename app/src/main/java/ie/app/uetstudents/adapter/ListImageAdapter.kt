package ie.app.uetstudents.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import ie.app.uetstudents.MainActivity
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient.BASE_URL
import ie.app.uetstudents.ui.Entity.Question.get.ImageDto
import ie.app.uetstudents.ui.tailieu.detailPDF

class ListImageAdapter(context: Context) : BaseAdapter<ImageDto>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]

        holder.findViewById<ImageView>(R.id.imageItem).apply {
            if(item.image.contains(".pdf")) {
                Glide.with(this)
                    .load(R.drawable.pdf)
                    .into(this)
            } else {
                val urlImage = "${BASE_URL}image${item.image}"
                Glide.with(this)
                    .load(urlImage)
                    .placeholder(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .into(this)
            }
        }



    }

}
