package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.Entity.Comment.get.CommentDto
import ie.app.uetstudents.Entity.subcomment.get.SubcommentDto
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.itemcoment.view.*
import kotlinx.android.synthetic.main.itemcoment.view.like_comment
import kotlinx.android.synthetic.main.itemsub_comment.view.*

class SubCommentAdapter (var id_comment : Int)
    : RecyclerView.Adapter<SubCommentAdapter.ViewHolder>() {

    var dataList: List<SubcommentDto> = ArrayList()
    var click_phanhoi : truyen_name_account? = null

    fun listener(listener : truyen_name_account)
    {
        click_phanhoi = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemsub_comment, parent, false)
        )
    }

    fun setData(list: List<SubcommentDto>) {
        this.dataList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = dataList.get(position)
        holder.bindData(dataModel)



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor")
        fun bindData(d: SubcommentDto) {
            itemView.name_subcomment_account.text = d.accountDto?.username

            Glide.with(itemView.context)
                .load("${ApiClient.BASE_URL}image${d.accountDto?.avatar}")
                .placeholder(R.drawable.img_default)
                .error(R.drawable.img_default)
                .into(itemView.image_subcomment_account)
            d.image?.let {
                Glide.with(itemView.context)
                    .load("${ApiClient.BASE_URL}image${d.image}")
                    .placeholder(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .into(itemView.anh_subcomment)
            }
            itemView.content_subcomment.text = d.content

            val time: String = d.time?.substring(11, 16)+ " " + d.time?.substring(0, 10)
            itemView.time_subcomment.text = time


        }
    }



}

