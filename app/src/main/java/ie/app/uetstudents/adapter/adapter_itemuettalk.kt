package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.R.drawable.ic_baseline_arrow_back_24
import ie.app.uetstudents.ui.Entity.Question.QuestionDto
import kotlinx.android.synthetic.main.item_forum.view.*
import kotlinx.android.synthetic.main.item_uettalk.view.*
import kotlinx.android.synthetic.main.itemcoment.view.*

class adapter_itemuettalk(
    var ClickItem: OnClickItem_UetTalk
) : RecyclerView.Adapter<adapter_itemuettalk.ViewHolder>()  {

    private var dataList: List<QuestionDto> = ArrayList<QuestionDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_uettalk, parent, false))
    }

    fun setData(list: List<QuestionDto>){
        this.dataList = list
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = dataList.get(position)
        holder.bindData(dataModel)
        holder.itemView.like_itemuettlk.setOnClickListener {
            ClickItem.ClickItem_like(dataModel)
            it.like_itemuettlk.setTextColor(
                R.color.purple_500
            )
            it.like_itemuettlk.setTypeface(null, Typeface.BOLD)
        }
        holder.itemView.comment_itemuettlk.setOnClickListener {
            ClickItem.ClickItem_comment(dataModel)
        }
        holder.itemView.setOnClickListener {
            ClickItem.ClickItem_uettalk(dataModel)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(d: QuestionDto) {
            itemView.txt_status_itemuettalk.text = d.content
            Glide.with(itemView.context).load(d.image).into(itemView.image_recyclerview_itemuettalk)
        }
    }
}

interface OnClickItem_UetTalk{
    fun ClickItem_like(QuestionDto : QuestionDto)
    fun ClickItem_comment(QuestionDto : QuestionDto)
    fun ClickItem_uettalk(QuestionDto : QuestionDto)
}