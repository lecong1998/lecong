package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.MainActivity
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.Entity.Comment.CommentDto
import ie.app.uetstudents.ui.Entity.Comment.comment
import ie.app.uetstudents.ui.Entity.like.like_comment
import kotlinx.android.synthetic.main.item_forum.view.*
import kotlinx.android.synthetic.main.itemcoment.view.*
import kotlinx.coroutines.withContext


class adapter_comment(
    var clickItem: ClickItemCommentLike
) : RecyclerView.Adapter<adapter_comment.ViewHolder>()  {

    private var dataList: List<CommentDto> = ArrayList<CommentDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.itemcoment, parent, false))
    }

    fun setData(list: List<CommentDto>){
        this.dataList = list
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

            solanthich= solanthich+1
            if (solanthich % 2 == 1)
            {
                it.like_comment.setTextColor(
                    R.color.purple_500
                )
                it.like_comment.setTypeface(null,Typeface.BOLD)
            }
            else
            {
                it.like_comment.setTextColor(
                    R.color.black
                )
                it.like_comment.setTypeface(null,Typeface.NORMAL)
            }
        }


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(d: CommentDto) {
            itemView.name_comment_account.text = "Lê Công"
            Glide.with(itemView.context).load(d.image).error(R.drawable._60279747_1127526494354946_6683273208343303265_n).into(itemView.image_comment_account)
            itemView.content_comment.text = d.content
        }
    }


}

interface ClickItemCommentLike{
    fun clickOnItem(m : CommentDto)
}
