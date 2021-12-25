package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.Entity.Comment.get.CommentDto
import ie.app.uetstudents.Entity.subcomment.get.getsubcomment
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.itemcoment.view.*
import retrofit2.Callback
import retrofit2.Response


class CommentAdapter(
    var clickItem: ClickItemCommentLike
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

     var dataList: ArrayList<CommentDto> = ArrayList()
     var click_phanhoi : truyen_name_account? = null
    var adapter_subcomment : SubCommentAdapter? = null

    fun listener(listener : truyen_name_account)
    {
        click_phanhoi = listener
    }

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

        var page : Int = 1

        var solanthich = dataModel.like_quantity
        var liked = dataModel.liked
        holder.itemView.like_comment.setOnClickListener {

            if (liked == false)
            {
                holder.itemView.like_comment.text = "Đã thích"
                holder.itemView.like_comment.setTextColor(R.color.purple_500)
                liked = true
                solanthich++
                holder.itemView.soluotlikecomment.visibility = View.VISIBLE
                holder.itemView.soluotlikecomment.text = solanthich.toString()
            }else
            {   holder.itemView.like_comment.text = "Thích"
                holder.itemView.like_comment.setTextColor(R.color.black)
                liked = false
                solanthich--
                if (solanthich>0)
                {
                    holder.itemView.soluotlikecomment.visibility = View.VISIBLE
                    holder.itemView.soluotlikecomment.text = solanthich.toString()
                }else{
                    holder.itemView.soluotlikecomment.visibility = View.GONE
                }
            }
            clickItem.clickOnItem(dataModel,liked)
        }
        var solanlicksubcomment : Int= 0
        holder.itemView.comment_phanhoi.setOnClickListener {
            click_phanhoi?.truyen_name_account(dataModel.accountDto?.id!!,dataModel.id!!)
        }

        if (dataModel.sub_comment_quantity>0)
        {
            holder.itemView.numbersubcomment.setOnClickListener {
                solanlicksubcomment++
                if(solanlicksubcomment%2 == 1)
                {
                    holder.itemView.listsubcomment.visibility = View.VISIBLE
                    val call  = ApiClient.getClient.getSubComment(dataModel.id!!,page)
                    call.enqueue(object : Callback<getsubcomment>{
                        override fun onResponse(
                            call: retrofit2.Call<getsubcomment>,
                            response: Response<getsubcomment>
                        ) {
                            if (response.isSuccessful)
                            {
                                adapter_subcomment = SubCommentAdapter(dataModel.id)
                                adapter_subcomment!!.setData(response.body()?.subCommentDtoList!!)
                                holder.itemView.listsubcomment.layoutManager = LinearLayoutManager(holder.itemView.context)
                                holder.itemView.listsubcomment.adapter = adapter_subcomment
                                Log.e("lay subcomment","Thành công")
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<getsubcomment>, t: Throwable) {
                            Log.e("lay subcomment","thất bại")
                        }
                    })
                }else
                {
                    holder.itemView.listsubcomment.visibility = View.GONE
                }

            }
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor")
        fun bindData(d: CommentDto) {
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
            itemView.content_comment.text = d.content

            val time: String = d.time?.substring(11, 16)+ " " + d.time?.substring(0, 10)
            itemView.time_comment.text = time

            if (d.like_quantity>0)
            {
                itemView.soluotlikecomment.visibility = View.VISIBLE
                itemView.soluotlikecomment.text = d.like_quantity.toString()
            }
            if (d.liked == true)
            {
                itemView.like_comment.setTextColor(R.color.purple_500)
            }else
            {
                itemView.like_comment.setTextColor(R.color.black)
            }

            if (d.sub_comment_quantity>0)
            {
                itemView.numbersubcomment.text = d.sub_comment_quantity.toString()
                itemView.numbersubcomment.visibility = View.VISIBLE
            }
        }
    }



}

interface ClickItemCommentLike {
    fun clickOnItem(m: CommentDto,liked : Boolean)
}
interface truyen_name_account{
    fun truyen_name_account(id_account : Int,id_comment : Int)
}