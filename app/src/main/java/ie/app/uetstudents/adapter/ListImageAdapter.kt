package ie.app.uetstudents.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.API.ApiClient.BASE_URL
import ie.app.uetstudents.Entity.Question.get.ImageDto

class ListImageAdapter(context: Context,
callback: BaseAdapter.OnclickPdf<ImageDto>)
    : BaseAdapter<ImageDto>(context) {

    val callbackmoi : BaseAdapter.OnclickPdf<ImageDto> = callback
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
        callback = callbackmoi
        holder.findViewById<ImageView>(R.id.imageItem).setOnClickListener {
            if(item.image.contains(".pdf")) {
                callback!!.onItemClick(position, item)
            }
        }



    }

}
