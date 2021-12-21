package ie.app.uetstudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDtoX
import kotlinx.android.synthetic.main.item_forum.view.*

class adapter_forum(
    var clickItem: ClickItem
    ) : RecyclerView.Adapter<adapter_forum.ViewHolder>()  {

    private var dataList: List<QuestionDtoX> = ArrayList<QuestionDtoX>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_forum, parent, false))
    }

    fun setData(list: List<QuestionDtoX>){
        this.dataList = list
        notifyDataSetChanged()
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
        fun bindData(d: QuestionDtoX) {
            itemView.item_forum_content.text = d.title
            val time: String = d.time?.substring(11, 16)+ " " + d.time?.substring(0, 10)
            itemView.item_forum_time.text = time
            itemView.item_forum_numbercomment.text = d.comment_quantity.toString()
            Glide.with(itemView.context)
                .load(ApiClient.BASE_URL+"image"+d.accountDto?.avatar)
                .placeholder(R.drawable.img_default_user)
                .error(R.drawable.img_default_user)
                .into(itemView.item_forum_image)
        }
    }
}

interface ClickItem{
    fun clickOnItem(m: QuestionDtoX)
}