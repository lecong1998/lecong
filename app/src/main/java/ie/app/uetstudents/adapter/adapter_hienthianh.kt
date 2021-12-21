package ie.app.uetstudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import kotlinx.android.synthetic.main.anh_detail.view.*

class adapter_hienthianh(var listanh: ArrayList<String>) :
    RecyclerView.Adapter<adapter_hienthianh.Viewholder>() {
    class Viewholder(var itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun onBindata(link: String) {
            if(link.contains(".pdf")) {
                Glide.with(itemView.context)
                    .load(R.drawable.pdf)
                    .into(itemview.imageItem)
            } else {
                val urlImage = "${ApiClient.BASE_URL}image${link}"
                Glide.with(itemView.context)
                    .load(urlImage)
                    .placeholder(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .into(itemview.imageItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.anh_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        return holder.onBindata(listanh[position])
    }

    override fun getItemCount(): Int {
        return listanh.size
    }
}
