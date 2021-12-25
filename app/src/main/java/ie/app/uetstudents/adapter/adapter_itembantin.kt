package ie.app.uetstudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.app.uetstudents.R
import ie.app.uetstudents.Entity.Question.get.QuestionDtoX
import kotlinx.android.synthetic.main.item_news.view.*

class adapter_itembantin(
    var listitem: ArrayList<QuestionDtoX>,
    var Onclick : OnclickItem_NotificationUet
    ): RecyclerView.Adapter<adapter_itembantin.viewholder>() {
    inner class viewholder(var itemview : View): RecyclerView.ViewHolder(itemview) {
        fun onBindata(itembantin: QuestionDtoX)
        {
            itemview.item_news_image.setImageResource(R.drawable.mau_thong_bao_chung_moi_va_chuan_nhat)
            itemview.item_news_txtname.text = itembantin.title
            val time: String = itembantin.time?.substring(11, 16)+ " " + itembantin.time?.substring(0, 10)
            itemview.item_news_txttime.text= time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        )
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val dataModel = listitem.get(position)
         holder.onBindata(dataModel)
        holder.itemview.setOnClickListener {
            Onclick.OnclickItem(dataModel)
        }
    }

    override fun getItemCount(): Int {
        return listitem.size
    }
}
interface OnclickItem_NotificationUet{
    fun OnclickItem(itembantin: QuestionDtoX)
}