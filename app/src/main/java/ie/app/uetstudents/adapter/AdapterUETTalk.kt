package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Question.get.ImageDto
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.ui.Entity.Question.get.question
import ie.app.uetstudents.ui.Entity.like_question.get.like_question
import ie.app.uetstudents.ui.tailieu.detailPDF
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_uettalk.*
import kotlinx.android.synthetic.main.item_uettalk.view.*
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterUETTalk(
    var context : Context,
    var ClickItem: OnClickItem_UetTalk,
    var callback : BaseAdapter.OnclickPdf<ImageDto>
) : RecyclerView.Adapter<AdapterUETTalk.ViewHolder>() {

    private var dataList: List<QuestionDtoX> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_uettalk, parent, false)
        )
    }

    fun setData(list: List<QuestionDtoX>) {
        this.dataList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var dataModel = dataList.get(position)
        holder.bindData(dataModel,callback)
        holder.itemView.like_itemuettlk.setOnClickListener {
            var soluotlike = dataModel.like_quantity
                if (dataModel.liked == false)
                {
                    holder.itemView.imagelike.setImageResource(R.drawable.ic_baseline_favorite_24)
                    holder.itemView.textlike.text= "Đã Thích"
                    soluotlike++
                    holder.itemView.numberlike.text = "${soluotlike} Người thích"

                }else
                {
                    holder.itemView.imagelike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    holder.itemView.textlike.text= "Thích"
                    soluotlike--
                    if (soluotlike!= 0)
                    {
                        holder.itemView.numberlike.text = "${soluotlike} Người thích"
                    }

                }

            ClickItem.ClickItem_like(dataModel)


        }
        holder.itemView.comment_itemuettlk.setOnClickListener {
            ClickItem.ClickItem_comment(dataModel)
        }
        holder.itemView.setOnClickListener {
            ClickItem.ClickItem_uettalk(dataModel)
        }



    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @SuppressLint("ResourceAsColor")
        fun bindData(d: QuestionDtoX,callback: BaseAdapter.OnclickPdf<ImageDto>) {
            var adapter = ListImageAdapter(itemView.context ,callback)
            itemView.txt_status_itemuettalk.text = d.content
            val thoigian: String = d.time?.substring(11, 16).toString()
            val ngay: String = d.time?.substring(0, 10).toString()
            itemView.time_uetttalk_item.setText(thoigian)
            itemView.date_uettalk_item.setText(ngay)
            itemView.listanh_uet_item.adapter = adapter
            itemView.name_uettalk_user.text = d.accountDto?.username
            val urlAvatar = "${ApiClient.BASE_URL}image${d.accountDto?.avatar}"
            Glide.with(itemView.context)
                .load(urlAvatar)
                .placeholder(R.drawable.img_default_user)
                .error(R.drawable.img_default_user)
                .into(itemView.image_uettalk_user)

            adapter.updateList(d.imageDtoList)
            if (d.like_quantity != 0)
            {
                itemView.numberlike.text = "${d.like_quantity} người thích"
            }
            if (d.comment_quantity != 0)
            {
                itemView.number_comment.text = "${d.comment_quantity} bình luận"
            }

            if (d.liked == false)
            {
                itemView.imagelike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                itemView.textlike.text="Thích"
                itemView.textlike.setTextColor(R.color.black)
            }else
            {
                itemView.imagelike.setImageResource(R.drawable.ic_baseline_favorite_24)
                itemView.textlike.text="Đã Thích"
                itemView.textlike.setTextColor(R.color.purple_500)
            }

        }
    }



}

interface OnClickItem_UetTalk {
    fun ClickItem_like(QuestionDto: QuestionDtoX)
    fun ClickItem_comment(QuestionDto: QuestionDtoX)
    fun ClickItem_uettalk(QuestionDto: QuestionDtoX)
}
