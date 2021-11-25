package ie.app.uetstudents.ui.diendan.Write

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.findNavController
import ie.app.uetstudents.R
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Category.category
import ie.app.uetstudents.ui.Entity.Question.*
import kotlinx.android.synthetic.main.fragment_write.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WriteFragment : Fragment(){




    private var database : QuestionDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chuyentrang?.setOnClickListener {
            callapi(edtxt_status.text.toString())
            it.findNavController().navigate(R.id.writeFragment_to_forumFragment)

        }
    }

    fun callapi(writeContent : String)
    {
        val category = Category(1)
        val listcategory = ArrayList<Category>()
        listcategory.add(category)
        val type_content = TypeContent(1)
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
        val item : MenuItem = menu.findItem(R.id.action_search)
        val item2 : MenuItem = menu.findItem(R.id.action_profile)
        item.isVisible = false
        item2.setVisible(false)
    }

}
