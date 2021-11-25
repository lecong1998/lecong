package ie.app.uetstudents.ui.diendan.detailForum

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import ie.app.uetstudents.R
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.ClickItemCommentLike
import ie.app.uetstudents.adapter.adapter_comment
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
import kotlinx.android.synthetic.main.fragment_detail_forum.*
import kotlinx.android.synthetic.main.fragment_detail_forum.view.*
import kotlinx.android.synthetic.main.itemcoment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailForumFragment : Fragment(), DetailForumContract.View, ClickItemCommentLike {

    private var id_question: Int? = null

    private lateinit var adapter_comment: adapter_comment
    private lateinit var presenterDetailForum: DetailForumContract.Presenter
    private var page_comment = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            id_question = it?.getInt("id_question")
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_forum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenterDetailForum = DetailForumPresenter(this, Repository(requireContext()))

        id_question?.let { presenterDetailForum.getDetailForum(it) }
        id_question?.let { presenterDetailForum.getDetailComment(it, page_comment) }

        adapter_comment = adapter_comment(this)

        view.detail_comment_forum_recyclerview.layoutManager = LinearLayoutManager(context)
        view.detail_comment_forum_recyclerview.adapter = adapter_comment
        view.detail_comment_forum_recyclerview.isNestedScrollingEnabled= false
        view.detail_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v.measuredHeight))
                {
                    page_comment++
                    view.detailforum_progressbar.visibility = View.VISIBLE
                    id_question?.let { presenterDetailForum.getDetailComment(it,page_comment) }
                }
            }
        })


        /*-----------------------Bình luận-----------------------*/

        btndang_detail_forum.setOnClickListener(
            View.OnClickListener {
                detailforum_progressbar.visibility = View.VISIBLE
                if(edt_detail_forum.text.isEmpty())
                {
                    Toast.makeText(context,"Bạn chưa nhập bình luận!",Toast.LENGTH_LONG).show()
                    detailforum_progressbar.visibility = View.GONE
                }
                else
                {
                    CallApiComment(edt_detail_forum.text.toString())
                    edt_detail_forum.text.clear()
                    Toast.makeText(context,"Bình luận thành công",Toast.LENGTH_SHORT).show()
                    chuacocomment.text = ""

                    val call : Call<comment> =
                        id_question?.let { it1 -> ApiClient.getClient.getCommentQuestion(it1,page_comment) }!!

                    call.enqueue(object : Callback<comment>{
                        override fun onResponse(call: Call<comment>, response: Response<comment>) {
                            if(response.isSuccessful)
                            {
                                adapter_comment.setData(response.body()!!.commentDtoList.reversed())
                                view.detail_comment_forum_recyclerview.adapter = adapter_comment
                                detailforum_progressbar.visibility = View.GONE
                            }
                        }

                        override fun onFailure(call: Call<comment>, t: Throwable) {
                            Log.e("Test_api","Thất bại")
                        }
                    })

                }

            }
        )


    }

    override fun clickOnItem(m: CommentDto) {

       // Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show()
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

    override fun getDataView(data: QuestionDto) {
        txtcontent_forum.text = data.content.toString()
        Log.e("data", data.content.toString())

    }

    override fun getDataViewComment(datacomment: comment) {


        adapter_comment.setData(datacomment.commentDtoList.reversed())
        adapter_comment.notifyDataSetChanged()
        if (datacomment.commentDtoList.isEmpty())
        {
            chuacocomment.text = "Chưa có bình luận nào"
        }
        detailforum_progressbar.visibility = View.GONE

    }

    fun CallApiComment(comment: String)
    {
        val commentDto = CommentDto(null,comment,null,null,id_question!!.toInt())
        val questionid = Question(id_question!!.toInt())
        var Postcomment = postcomment(comment,"image",questionid)
        val call : Call<comment> = ApiClient.getClient.setCommentQuestion(Postcomment)
        call.enqueue(object : Callback<comment>{
            override fun onResponse(call: Call<comment>, response: Response<comment>) {
                Log.e("Test_API_Post_Comment","Thành công")
            }

            override fun onFailure(call: Call<comment>, t: Throwable) {
                Log.e("Test_API_Post_Comment","thất bại")
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item : MenuItem = menu.findItem(R.id.action_profile)
        val item1 : MenuItem = menu.findItem(R.id.action_search)
        item.isVisible = false
        item1.isVisible = false
    }

}