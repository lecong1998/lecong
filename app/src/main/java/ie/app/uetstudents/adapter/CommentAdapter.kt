package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.telecom.Call
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.Entity.Comment.get.CommentDto
import ie.app.uetstudents.Entity.subcomment.get.SubcommentDto
import ie.app.uetstudents.Entity.subcomment.get.getsubcomment
import ie.app.uetstudents.RealPathUtil.MyMovementMethod
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_detail_forum.*
import kotlinx.android.synthetic.main.itemcoment.view.*
import kotlinx.android.synthetic.main.itemsub_comment.view.*
import retrofit2.Callback
import retrofit2.Response


class CommentAdapter(
    var clickItem: ClickItemCommentLike,
    var clicktext: Clicktext
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    var dataList: ArrayList<CommentDto> = ArrayList()
    var click_phanhoi: truyen_name_account? = null
    var adapter_subcomment: SubCommentAdapter? = null

    fun listener(listener: truyen_name_account) {
        click_phanhoi = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.itemcoment, parent, false)
        )
    }

    fun setData(list: ArrayList<CommentDto>) {
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

        var page: Int = 1

        var solanthich = dataModel.like_quantity
        var liked = dataModel.liked
        holder.itemView.like_comment.setOnClickListener {

            if (liked == false) {
                holder.itemView.like_comment.text = "Đã Thích"
                holder.itemView.like_comment.setTextColor(R.color.purple_500)
                liked = true
                solanthich++
                holder.itemView.soluotlikecomment.visibility = View.VISIBLE
                holder.itemView.soluotlikecomment.text = solanthich.toString()
            } else {
                holder.itemView.like_comment.text = "Thích"
                holder.itemView.like_comment.setTextColor(R.color.black)
                liked = false
                solanthich--
                if (solanthich > 0) {
                    holder.itemView.soluotlikecomment.visibility = View.VISIBLE
                    holder.itemView.soluotlikecomment.text = solanthich.toString()
                } else {
                    holder.itemView.soluotlikecomment.visibility = View.GONE
                }
            }
            clickItem.clickOnItem(dataModel, liked)
        }
        var solanlicksubcomment: Int = 0
        holder.itemView.comment_phanhoi.setOnClickListener {
            click_phanhoi?.truyen_name_account(
                dataModel.accountDto?.id!!,
                dataModel.id!!,
                holder.itemView.listsubcomment
            )
        }

        if (dataModel.sub_comment_quantity > 0) {
            holder.itemView.numbersubcomment.setOnClickListener {
                solanlicksubcomment++
                if (solanlicksubcomment % 2 == 1) {
                    holder.itemView.listsubcomment.visibility = View.VISIBLE
                    callsubcomment(holder, page, dataModel)

                    if (dataModel.sub_comment_quantity > 10) {
                        holder.itemView.more.visibility = View.VISIBLE
                        holder.itemView.more.setOnClickListener {
                            page++
                            callsubcomment(holder, page, dataModel)
                        }
                    }
                } else {
                    holder.itemView.listsubcomment.visibility = View.GONE
                }

            }


        }


    }

    fun callsubcomment(holder: ViewHolder, page: Int, dataModel: CommentDto) {

        val call = ApiClient.getClient.getSubComment(dataModel.id!!, page)
        call.enqueue(object : Callback<getsubcomment> {
            override fun onResponse(
                call: retrofit2.Call<getsubcomment>,
                response: Response<getsubcomment>
            ) {
                if (response.isSuccessful) {
                    adapter_subcomment = SubCommentAdapter(dataModel.id, clicktext)
                    adapter_subcomment!!.setData(response.body()?.subCommentDtoList!! as ArrayList<SubcommentDto>)
                    holder.itemView.listsubcomment.layoutManager =
                        LinearLayoutManager(holder.itemView.context)
                    holder.itemView.listsubcomment.adapter?.notifyDataSetChanged()
                    holder.itemView.listsubcomment.adapter = adapter_subcomment
                    Log.e("lay subcomment", "Thành công")
                    if (response.body()?.result_quantity!! % 10 != 0) {
                        holder.itemView.more.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<getsubcomment>, t: Throwable) {
                Log.e("lay subcomment", "thất bại")
            }
        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor")
        fun bindData(d: CommentDto, clicktext: Clicktext) {
            itemView.name_comment_account.text = d.accountDto?.username

            Glide.with(itemView.context)
                .load("${ApiClient.BASE_URL}image${d.accountDto?.avatar}")
                .error(R.drawable.img_default)
                .into(itemView.image_comment_account)
            d.image?.let {
                Glide.with(itemView.context)
                    .load("${ApiClient.BASE_URL}image${d.image}")
                    .error(R.drawable.img_default)
                    .into(itemView.anh_comment)
            }


            if (d.content?.contains("@user/") == true) {
                val begin: Int = d.content?.indexOf("@user/")
                val startname: Int = d.content?.indexOf("/", begin)
                val end: Int = d.content?.indexOf(" ", begin)
                if (end > 0) {
                    val spaned = SpannableString(d.content)
                    val fcolor = ForegroundColorSpan(Color.BLUE)
                    spaned.setSpan(RelativeSizeSpan(1.0f), begin, end, 0)
                    spaned.setSpan(fcolor, begin, end, 0)
                    spaned.setSpan(StyleSpan(Typeface.BOLD), begin, end, 0)
                    spaned.setSpan(object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            val stringname = d.content.substring(startname + 1, end)
                            clicktext.clicktext(stringname,"person")
                        }
                    }, begin, end, 0)
                    itemView.content_comment.setText(spaned, TextView.BufferType.SPANNABLE).let {
                        itemView.content_comment.setOnClickListener {
                            val stringname = d.content.substring(startname + 1, end)
                            clicktext.clicktext(stringname,"person")
                        }
                    }


                } else {
                    itemView.content_comment.text = d.content
                }


            }else
                if(d.content?.contains("@forum/") == true)
            {
                val begin: Int = d.content?.indexOf("@forum/")!!
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
                        clicktext.clicktext(stringname,"forum")
                    }
                }, begin, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

                itemView.content_comment.setText(spaned, TextView.BufferType.SPANNABLE).let {
                    itemView.content_comment.setOnClickListener {
                        val stringname = d.content.substring(startname + 1, end)
                        clicktext.clicktext(stringname,"forum")
                    }
                }
            }else
                if (d.content?.contains("@uettalk/") == true)
                {
                    val begin: Int = d.content?.indexOf("@uettalk/")
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
                            clicktext.clicktext(stringname,"uettalk")
                        }
                    }, begin, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)



                    itemView.content_comment.setText(spaned, TextView.BufferType.SPANNABLE).let {
                        itemView.content_comment.setOnClickListener {
                            val stringname = d.content.substring(startname + 1, end)
                            clicktext.clicktext(stringname,"uettalk")
                        }
                    }
                }
            else {
                itemView.content_comment.text = d.content
            }

            val time: String = d.time?.substring(11, 16) + " " + d.time?.substring(0, 10)
            itemView.time_comment.text = time

            if (d.like_quantity > 0) {
                itemView.soluotlikecomment.visibility = View.VISIBLE
                itemView.soluotlikecomment.text = d.like_quantity.toString()
            }
            if (d.liked == true) {
                itemView.like_comment.text = "Đã Thích"
                itemView.like_comment.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.purple_500
                    )
                )
            } else {
                itemView.like_comment.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
            }

            if (d.sub_comment_quantity > 0) {
                itemView.numbersubcomment.text = d.sub_comment_quantity.toString()
                itemView.numbersubcomment.visibility = View.VISIBLE
            }
        }
    }


}

interface ClickItemCommentLike {
    fun clickOnItem(m: CommentDto, liked: Boolean)
}

interface truyen_name_account {
    fun truyen_name_account(id_account: Int, id_comment: Int, recyclerView: RecyclerView)
}

interface Clicktext {
    fun clicktext(name_account: String,type: String)
}