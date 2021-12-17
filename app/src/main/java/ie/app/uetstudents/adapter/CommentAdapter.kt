package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Comment.get.CommentDto
import ie.app.uetstudents.ui.Entity.like.Get.like_comment_get
import kotlinx.android.synthetic.main.itemcoment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentAdapter(
    var clickItem: ClickItemCommentLike
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

     var dataList: ArrayList<CommentDto> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemcoment, parent, false)
        )
    }

    fun setData(list: ArrayList<CommentDto>) {
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



        var solanthich = 0

        holder.itemView.like_comment.setOnClickListener {
            clickItem.clickOnItem(dataModel)

            solanthich = solanthich + 1
            if (solanthich % 2 == 1) {
                it.like_comment.setTextColor(
                    R.color.purple_500
                )
                it.like_comment.setTypeface(null, Typeface.BOLD)
            } else {
                it.like_comment.setTextColor(
                    R.color.black
                )
                it.like_comment.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(d: CommentDto) {
            itemView.name_comment_account.text = "Lê Công"
            d.image?.let {
                Glide.with(itemView.context)
                    .load("${ApiClient.BASE_URL}image${d.image}")
                    .error(R.drawable.img_default)
                    .into(itemView.anh_comment)
            }
            itemView.content_comment.text = d.content

            val time: String = d.time?.substring(11, 16)+ " " + d.time?.substring(0, 10)
            itemView.time_comment.text = time
        }
    }



}

interface ClickItemCommentLike {
    fun clickOnItem(m: CommentDto)
}
