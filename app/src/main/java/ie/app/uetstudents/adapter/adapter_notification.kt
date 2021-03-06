package ie.app.uetstudents.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.notifications_comment.get.NotificationCommentDto
import ie.app.uetstudents.ui.Entity.notifications_comment.get.get_notifi_comment
import ie.app.uetstudents.ui.Entity.notifications_question.get.NotificationQuestionDto
import ie.app.uetstudents.ui.Entity.notifications_question.notification_item
import kotlinx.android.synthetic.main.item_notification.view.*

class adapter_notification( var ClickItem : OnClickItem_Notification)
    : RecyclerView.Adapter<adapter_notification.ViewHolder>(){

     var listnotifi_item : ArrayList<notification_item>? = ArrayList()

    fun resetList() {
        listnotifi_item?.clear()
        notifyDataSetChanged()
    }

    fun setData_question(list_notifi_question : List<NotificationQuestionDto>)
    {
        list_notifi_question.forEach {
            val notifi_item = notification_item(it.action_type,it.avatar,it.id,it.notification_item_id+1000,it.seen,it.username,it.time)
            listnotifi_item?.add(notifi_item)
        }
        notifyDataSetChanged()
    }
    fun setdata_comment(list_notifi_comment : List<NotificationCommentDto>)
    {
        list_notifi_comment.forEach {
            val notifi_item = notification_item(it.action_type,it.avatar,it.id,it.notification_item_id,it.seen,it.username,it.time!!)
            listnotifi_item?.add(notifi_item)
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(var itemview : View) : RecyclerView.ViewHolder(itemview) {
        @SuppressLint("ResourceAsColor")
        fun OnBinData(n : notification_item)
        {
            Glide.with(itemView.context).load(ApiClient.BASE_URL+ "image"+n.avatar).error(R.drawable._60279747_1127526494354946_6683273208343303265_n).into(itemview.item_notification_image)
            if (n.action_type == "LIKE")
            {
                if(n.notification_item_id>=1000)
                {
                    itemview.item_notification_txtcontent.text = "${n.username} ???? Thich B??i Vi???t c???a b???n!"
                }
                else
                {
                    itemview.item_notification_txtcontent.text = "${n.username} ???? Thich B??nh lu???n C???a b???n!"
                }

            }
            if (n.action_type == "COMMENT")
            {
                itemview.item_notification_txtcontent.text = "${n.username} ???? B??nh lu???n B??i Vi???t c???a b???n!"
            }
            val time: String = n.time?.substring(11, 16)+ " " + n.time?.substring(0, 10)
            itemview.item_notification_time.text = time

            if(n.seen == false)
            {
                itemview.item_notification_txtcontent.setTypeface(Typeface.DEFAULT,Typeface.BOLD)
                itemview.mau_notification.setBackgroundResource(R.drawable.vienxanh)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification,parent,false)
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datamodel = listnotifi_item?.get(position)
        holder.OnBinData(datamodel!!)
        holder.itemview.setOnClickListener {
            ClickItem.OnCLick(datamodel!!)
            if(datamodel.seen == false)
            {
                holder.itemview.item_notification_txtcontent.setTypeface(Typeface.DEFAULT)
                holder.itemview.mau_notification.setBackgroundColor(R.color.white)
            }
        }
    }

    override fun getItemCount(): Int {
        return listnotifi_item!!.size
    }
}
interface OnClickItem_Notification{
    fun OnCLick(n : notification_item)
}