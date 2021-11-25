package ie.app.uetstudents.ui.uettalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import ie.app.uetstudents.R
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.*
import ie.app.uetstudents.databinding.FragmentNotificationsBinding
import ie.app.uetstudents.databinding.FragmentUettalkBinding
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Comment.CommentDto
import ie.app.uetstudents.ui.Entity.Comment.Question
import ie.app.uetstudents.ui.Entity.Comment.comment
import ie.app.uetstudents.ui.Entity.Comment.postcomment
import ie.app.uetstudents.ui.Entity.Question.QuestionDto
import ie.app.uetstudents.ui.Entity.Question.QuestionX
import ie.app.uetstudents.ui.Entity.like.Account
import ie.app.uetstudents.ui.Entity.like.Comment
import ie.app.uetstudents.ui.Entity.like.like_comment
import ie.app.uetstudents.ui.diendan.detailForum.DetailForumContract
import ie.app.uetstudents.ui.diendan.detailForum.DetailForumPresenter
import ie.app.uetstudents.ui.diendan.forum_main.forumContract
import ie.app.uetstudents.ui.diendan.forum_main.forumPresenter
import ie.app.uetstudents.ui.timkiem.SearchActivity
import kotlinx.android.synthetic.main.fragment_uettalk.*
import kotlinx.android.synthetic.main.fragment_uettalk.view.*
import kotlinx.android.synthetic.main.layout_bottomsheet.*
import kotlinx.android.synthetic.main.sheetbottom_comment_uettalk.*
import kotlinx.android.synthetic.main.sheetbottom_comment_uettalk.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UETTalkFragment: Fragment() , forumContract.View,OnClickItem_UetTalk,DetailForumContract.View,
    ClickItemCommentLike {

    private var _binding: FragmentUettalkBinding? = null

    private val binding get() = _binding!!

    private lateinit var presenter: forumContract.Presenter
    private lateinit var adapter_uettalk : adapter_itemuettalk

    private var bottomSheetDialog : BottomSheetDialog? = null
    private lateinit var adapter_comment_uettalk : adapter_comment
    private lateinit var presenter_uettalk_comment: DetailForumContract.Presenter

    private lateinit var bottomSheetView : View

    private var page_comment : Int = 1
    private var page_uettalk: Int =1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUettalkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter = forumPresenter(this, Repository(requireContext()))
        presenter.getQuestions(2,page_uettalk)
        adapter_uettalk = adapter_itemuettalk(this)
        root.recyclerview_item_uettalk.layoutManager = LinearLayoutManager(requireContext())
            root.recyclerview_item_uettalk.isNestedScrollingEnabled = false


        root.recyclerview_item_uettalk.adapter= adapter_uettalk
        root.uettalk_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v!!?.measuredHeight) ?: Int)
                {
                    page_uettalk++
                    root.uet_talk_progressbar.visibility= View.VISIBLE
                    presenter.getQuestions(2,page_uettalk)
                }
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        status_uettalk.setOnClickListener {
            this.findNavController().navigate(R.id.action_nav_uettalk_to_writingStatusFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun updateViewData(data: QuestionX) {
        uet_talk_progressbar?.visibility = View.GONE
        adapter_uettalk.setData(data.questionDtoList.reversed())
        recyclerview_item_uettalk?.adapter?.notifyDataSetChanged()
    }

    override fun ClickItem_like(QuestionDto: QuestionDto) {
        Toast.makeText(context,"đã thích",Toast.LENGTH_LONG).show()
    }

    override fun ClickItem_comment(QuestionDto: QuestionDto) {
       // Toast.makeText(context,"Bình luận",Toast.LENGTH_LONG).show()


        presenter_uettalk_comment = DetailForumPresenter(this, Repository(requireContext()))
        adapter_comment_uettalk = adapter_comment(this)


        bottomSheetDialog = BottomSheetDialog(
            this@UETTalkFragment.requireContext(),R.style.BottomSheetDialogTheme
        )
         bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.sheetbottom_comment_uettalk,bottomsheet_uettalk )

        bottomSheetView.comment_recyclerview_uettalk.layoutManager =
            LinearLayoutManager(context)

        presenter_uettalk_comment.getDetailComment(QuestionDto.id?.toInt()!!,page_comment)
        bottomSheetView.comment_recyclerview_uettalk.isNestedScrollingEnabled = false
        bottomSheetView.comment_recyclerview_uettalk.adapter = adapter_comment_uettalk
        bottomSheetView.comment_recyclerview_uettalk.adapter?.notifyDataSetChanged()

        bottomSheetView.comment_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v!!?.measuredHeight) ?: Int)
                {
                    page_comment++
                    bottomSheetView.comment_progressbar.visibility= View.VISIBLE
                    presenter_uettalk_comment.getDetailComment(QuestionDto.id,page_comment)
                }
            }
        })



        bottomSheetView.btn_update_comment_uettalk.setOnClickListener {
            xulybtncommemt(QuestionDto.id)
        }

        bottomSheetDialog!!.setContentView(bottomSheetView)
        bottomSheetDialog!!.show()


    }

    override fun ClickItem_uettalk(QuestionDto: QuestionDto) {
        val bundle = Bundle()
        bundle.putInt("id_question",QuestionDto.id!!)
        Toast.makeText( context,QuestionDto.id.toString(), Toast.LENGTH_SHORT).show()
        this.findNavController().navigate(R.id.action_nav_uettalk_to_detailForumFragment,bundle)
    }

    override fun getDataView(data: QuestionDto) {

    }


    override fun getDataViewComment(datacomment: comment) {
       // bottomSheetView.comment_processbar.progress = View.GONE
        bottomSheetView.comment_progressbar.visibility = View.GONE
        if (datacomment.commentDtoList.isEmpty())
        {
            Toast.makeText(context,"trống",Toast.LENGTH_LONG).show()
            bottomSheetView.txt_comment_chuacobinhluan.text = "Chưa có bình luận nào!"
        }
        else
        {


            adapter_comment_uettalk.setData(datacomment.commentDtoList.reversed())
            Toast.makeText(context,"đã có dữ liệu",Toast.LENGTH_LONG).show()
            bottomSheetView.comment_recyclerview_uettalk.adapter?.notifyDataSetChanged()


        }
    }

    override fun clickOnItem(m: CommentDto) {
        val idcomment = Comment(m.id!!.toInt())
        val account = Account(null)
        val likeComment = like_comment(account,idcomment)
        val call : Call<like_comment> = ApiClient.getClient.setLikeComment(likeComment)
        call.enqueue(object : Callback<like_comment>{
            override fun onResponse(call: Call<like_comment>, response: Response<like_comment>) {
                Log.e("Test_API_Like_Comment","Thành công")
            }

            override fun onFailure(call: Call<like_comment>, t: Throwable) {
                Log.e("Test_API_Like_Comment","Thành công")
            }
        })
    }


    fun xulybtncommemt(id_question: Int)
    {
        if (bottomSheetView.edt_comment_uettalk.text.isEmpty())
        {
            Toast.makeText(context,"Bạn Chưa nhập bình luận!",Toast.LENGTH_LONG).show()
        }
        else {

            val commentpost = postcomment(
                bottomSheetView.edt_comment_uettalk.text.toString(),
                null,
                Question(id_question)
            )
            val call: Call<comment> = ApiClient.getClient.setCommentQuestion(commentpost)
            call.enqueue(object : Callback<comment> {
                override fun onResponse(call: Call<comment>, response: Response<comment>) {
                    Log.e("Test", "thành công")
                    Toast.makeText(context,"bình luận thành công!",Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<comment>, t: Throwable) {
                    Log.e("Test", "thất bại")
                }
            })
            bottomSheetView.edt_comment_uettalk.text.clear()

            bottomSheetView.comment_progressbar.visibility = View.VISIBLE

            val call_get: Call<comment> =
                ApiClient.getClient.getCommentQuestion(id_question, page_comment)
            call_get.enqueue(object : Callback<comment> {
                override fun onResponse(call: Call<comment>, response: Response<comment>) {
                    if (response.isSuccessful) {

                        adapter_comment_uettalk.setData(response.body()?.commentDtoList!!.reversed())
                        bottomSheetView.comment_recyclerview_uettalk?.adapter?.notifyDataSetChanged()
                        bottomSheetView.comment_progressbar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<comment>, t: Throwable) {
                    Log.e("Test", "lỗi rồi")
                }
            })
        }
    }



}