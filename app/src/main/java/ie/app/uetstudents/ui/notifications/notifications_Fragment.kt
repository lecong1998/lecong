package ie.app.uetstudents.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ie.app.uetstudents.R
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.OnClickItem_Notification
import ie.app.uetstudents.adapter.adapter_notification
import ie.app.uetstudents.service.FirebaseService.Companion.UPDATE_NOTIFICATION
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.Entity.Question.get.QuestionX
import ie.app.uetstudents.Entity.notifications_comment.get.NotificationCommentDto
import ie.app.uetstudents.Entity.notifications_comment.get.get_notifi_comment
import ie.app.uetstudents.Entity.notifications_comment.put.request.comment_id_put
import ie.app.uetstudents.Entity.notifications_question.get.NotificationQuestionDto
import ie.app.uetstudents.Entity.notifications_question.get.notification_question
import ie.app.uetstudents.Entity.notifications_question.notification_item
import ie.app.uetstudents.Entity.notifications_question.put.request.question_id_put
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_notifications.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class notifications_Fragment : Fragment() , OnClickItem_Notification,notification_Contract.View{

    var list_notification_Question : ArrayList<NotificationQuestionDto> = ArrayList()
    var list_notification_Comment : ArrayList<NotificationCommentDto> = ArrayList()

    private lateinit var adapterNotification: adapter_notification
    private lateinit var presenter: notification_Contract.Presenter
    private var page : Int = 1

    var id_user : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        id_user= PreferenceUtils.getUser().id
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_notifications,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = notificationPresenter(this, Repository(requireContext()))
        presenter.getNotificationComment(id_user!!,page)
        presenter.getNotificationQuestion(id_user!!,page)

        adapterNotification = adapter_notification(this)
        notification_recyclerview.layoutManager = LinearLayoutManager(context)
        notification_recyclerview.adapter = adapterNotification
        adapterNotification.listnotifi_item?.sortedByDescending { notificationItem: notification_item -> notificationItem.time   }
        adapterNotification?.notifyDataSetChanged()

        if (adapterNotification.itemCount % 10 != 0)
        {
            search_more.visibility = View.GONE
        }
        /*search_more.setOnClickListener {
            progress_bar_notifi.visibility = View.VISIBLE
            page++
            presenter.getNotificationQuestion(id_user!!,page)
            presenter.getNotificationComment(id_user!!,page)
            progress_bar_notifi.visibility = View.GONE
            adapterNotification.listnotifi_item?.sortedByDescending { notificationItem: notification_item -> notificationItem.time   }
            adapterNotification?.notifyDataSetChanged()
        }*/
      notification_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v?.measuredHeight))
                {
                    progress_bar_notifi.visibility = View.VISIBLE
                    page++
                    presenter.getNotificationQuestion(id_user!!,page)
                    presenter.getNotificationComment(id_user!!,page)
                    progress_bar_notifi.visibility = View.GONE
                    adapterNotification.listnotifi_item?.sortedByDescending { notificationItem: notification_item -> notificationItem.time   }
                    adapterNotification?.notifyDataSetChanged()
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {
        if(event == UPDATE_NOTIFICATION) {
            Log.d("dLog", "notifications_Fragment  onMessageEvent: $event")
            page = 1
            adapterNotification.resetList()
            presenter.getNotificationComment(id_user!!,page)
            presenter.getNotificationQuestion(id_user!!,page)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item1 : MenuItem = menu.findItem(R.id.action_search)
        val item2 : MenuItem = menu.findItem(R.id.action_notification)
        val item3 : MenuItem = menu.findItem(R.id.action_profile)
        item1.setVisible(false)
        item2.setVisible(false)
        item3.setVisible(false)
    }

    override fun OnCLick(n: notification_item) {
        if (n.notification_item_id>1000)
        {
            val bundle = Bundle()
            bundle.putInt("id_question",n.notification_item_id.minus(1000))
            this.findNavController().navigate(R.id.action_action_notification_to_detailForumFragment,bundle)
            if (n.seen==false)
            {
                    Repository(requireContext()).putseenNotifiQuestion(
                        question_id_put(n.id)
                    )
            }
        }
        else if (n.notification_item_id<1000)
        {
            val call : Call<QuestionX> = ApiClient.getClient.getQuestion_of_comment(n.notification_item_id,id_user!!)
            call.enqueue(object : Callback<QuestionX>{
                override fun onResponse(call: Call<QuestionX>, response: Response<QuestionX>) {
                    if (response.isSuccessful)
                    {
                        val id_question = response.body()!!.questionDtoList[0].id
                        val bundle = Bundle()
                        bundle.putInt("id_question",id_question!!)

                        this@notifications_Fragment.findNavController().navigate(R.id.action_action_notification_to_detailForumFragment,bundle)
                        Log.e("Test_tucommentdenQuestion","Thành công")
                        if (n.seen== false)
                        {
                            Repository(requireContext()).putSeenNotifi_comment(
                                comment_id_put(n.id)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<QuestionX>, t: Throwable) {
                   Log.e("Test_tucommentdenQuestion","Thất bại")
                }
            })
        }
    }

    override fun updateViewNotification_question(notification_question: notification_question) {

        val listnotifi : ArrayList<NotificationQuestionDto> = ArrayList<NotificationQuestionDto>()
        notification_question.notificationQuestionDtoList.forEach {
            if (it.username.equals(PreferenceUtils.getUser().username) == false)
            {
                listnotifi.add(it)
            }
        }
        adapterNotification.setData_question(notification_question.notificationQuestionDtoList)
       adapterNotification.listnotifi_item?.sortedByDescending { notificationItem: notification_item -> notificationItem.time   }
        notification_recyclerview.adapter?.notifyDataSetChanged()
    }

    override fun updateViewNotification_comment(notification_comment: get_notifi_comment) {
        val listnotifi : ArrayList<NotificationCommentDto> = ArrayList()
        notification_comment.notificationCommentDtoList.forEach {
            if (it.username.equals(PreferenceUtils.getUser().username)== false)
            {
                listnotifi.add(it)
            }
        }
        adapterNotification.setdata_comment(notification_comment.notificationCommentDtoList)
        adapterNotification.listnotifi_item?.sortedByDescending { notificationItem: notification_item -> notificationItem.time }
        notification_recyclerview.adapter?.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        EventBus.getDefault().post(UPDATE_NOTIFICATION)
        super.onDestroyView()
    }
}

