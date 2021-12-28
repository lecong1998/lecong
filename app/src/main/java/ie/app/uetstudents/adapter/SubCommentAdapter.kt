package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.Entity.Comment.get.CommentDto
import ie.app.uetstudents.Entity.subcomment.get.SubcommentDto
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.itemcoment.view.*
import kotlinx.android.synthetic.main.itemcoment.view.like_comment
import kotlinx.android.synthetic.main.itemsub_comment.view.*

class SubCommentAdapter(var id_comment: Int, var clicktext: Clicktext) :
    RecyclerView.Adapter<SubCommentAdapter.ViewHolder>() {

    var dataList: ArrayList<SubcommentDto> = ArrayList()
    var click_phanhoi: truyen_name_account? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemsub_comment, parent, false)
        )
    }

    fun setData(list: ArrayList<SubcommentDto>) {
        this.dataList.clear()
        this.dataList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = dataList.get(position)
        holder.bindData(dataModel, clicktext)


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor")
        fun bindData(d: SubcommentDto, clicktext: Clicktext) {
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

            if (d.content?.contains("@user/") == true) {

                val begin: Int = d.content?.indexOf("@user/")
                val startname: Int = d.content?.indexOf("/", begin)
                val end: Int = d.content?.indexOf(" ", begin)

                val spaned = SpannableString(d.content)
                val fcolor = ForegroundColorSpan(Color.BLUE)
                spaned.setSpan(
                    RelativeSizeSpan(1.0f),
                    begin,
                    end,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
                spaned.setSpan(fcolor, begin, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                spaned.setSpan(
                    StyleSpan(Typeface.BOLD),
                    begin,
                    end,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
                spaned.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val stringname = d.content.substring(startname + 1, end)
                        clicktext.clicktext(stringname)
                    }
                }, begin, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                itemView.content_subcomment.movementMethod = LinkMovementMethod.getInstance()
                itemView.content_subcomment.text = spaned

            } else {
                itemView.content_subcomment.text = d.content
            }
            val time: String = d.time?.substring(11, 16) + " " + d.time?.substring(0, 10)
            itemView.time_subcomment.text = time


        }
    }


}

