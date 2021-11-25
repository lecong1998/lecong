package ie.app.uetstudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.Entity.Question.QuestionDto
import kotlinx.android.synthetic.main.item_forum.view.*

class adapter_forum(
    var clickItem: ClickItem
    ) : RecyclerView.Adapter<adapter_forum.ViewHolder>()  {

    private var dataList: List<QuestionDto> = ArrayList<QuestionDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_forum, parent, false))
    }

    fun setData(list: List<QuestionDto>){
        this.dataList = list
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = dataList.get(position)
        holder.bindData(dataModel)
        holder.itemView.setOnClickListener(){
            clickItem.clickOnItem(dataModel)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(d: QuestionDto) {
            itemView.item_forum_content.text = d.content
            Glide.with(itemView.context).load(d.image).error(R.drawable.anhnentdoc).into(itemView.item_forum_image)
        }
    }
}

interface ClickItem{
    fun clickOnItem(m: QuestionDto)
}