package ie.app.uetstudents.ui.thongbao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ie.app.uetstudents.R
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.OnclickItem_NotificationUet
import ie.app.uetstudents.adapter.adapter_itembantin
import ie.app.uetstudents.adapter.itembantin
import ie.app.uetstudents.databinding.FragmentHomeBinding
import ie.app.uetstudents.databinding.FragmentNotificationsBinding
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.ui.Entity.Question.get.question
import ie.app.uetstudents.ui.home.HomeViewModel
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.fragment_news.view.item_news_recyclerview
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsFragment: Fragment() ,NotificationUetContract.View,OnclickItem_NotificationUet{

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var adapter : adapter_itembantin? = null
    private lateinit var presenter: NotificationUetContract.Presenter

    var page :Int = 1
    var iduser : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iduser = PreferenceUtils.getUser().id
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter = NotificationUetPresent(this, Repository(requireContext()))
        presenter.getQuestionList(page,3,iduser!!)


        root.notificationuet_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY== v?.getChildAt(0)?.measuredHeight?.minus(v.measuredHeight))
                {
                    page++
                    root.notification_progressbar.visibility= View.VISIBLE
                    presenter.getQuestionList(page,3,iduser!!)
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun UpdateDataViewItem(question: question) {
        adapter = adapter_itembantin(question.questionDtoList as ArrayList<QuestionDtoX>,this)
        item_notifications_recyclerview.layoutManager = LinearLayoutManager(requireContext())
        // adapter = adapter_itembantin(listnotification)
        item_notifications_recyclerview.adapter = adapter
        notification_progressbar.visibility = View.GONE
    }

    override fun OnclickItem(itembantin: QuestionDtoX) {
        val bundle = Bundle()
        bundle.putInt("id_question",itembantin.id)
        this.findNavController().navigate(R.id.action_nav_notifications_to_detailNotifiFragment,bundle)

    }
}