package ie.app.uetstudents.ui.uettalk

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Comment.Question
import ie.app.uetstudents.ui.Entity.Question.*
import kotlinx.android.synthetic.main.fragment_writing_status.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WritingStatusFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_writing_status, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update_status.setOnClickListener {
            callApi(edt_status.text.toString())
            this.findNavController().navigate(R.id.action_writingStatusFragment_to_nav_uettalk)
        }
    }

    private fun callApi(writeContent: String) {
        val category = Category(1)
        val listcategory = ArrayList<Category>()
        listcategory.add(category)
        val type_content = TypeContent(2)
        val questionPost = QuestionPost(listcategory,writeContent.toString(),null,writeContent.toString(),type_content)

        val call : Call<QuestionPost> = ApiClient.getClient.setQuestion(questionPost)
        call.enqueue(object : Callback<QuestionPost>{
            override fun onResponse(call: Call<QuestionPost>, response: Response<QuestionPost>) {
                if(response.isSuccessful)
                {
                    Log.e("Test","Thành công")
                }

            }

            override fun onFailure(call: Call<QuestionPost>, t: Throwable) {
                Log.e("Test","Thất bại")
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item : MenuItem= menu.findItem(R.id.action_search)
        val item2 : MenuItem = menu.findItem(R.id.action_profile)
        item.isVisible = false
        item2.setVisible(false)
    }
}