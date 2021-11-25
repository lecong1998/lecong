package ie.app.uetstudents.ui.timkiem

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import ie.app.uetstudents.R
import ie.app.uetstudents.adapter.adapter_pageView
import ie.app.uetstudents.ui.API.ApiClient
import ie.app.uetstudents.ui.Entity.Search.PersonDto
import ie.app.uetstudents.ui.Entity.Search.QuestionDto
import ie.app.uetstudents.ui.Entity.Search.search_person
import ie.app.uetstudents.ui.Entity.Search.search_question
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.searchdialog_fullscreen.*
import kotlinx.android.synthetic.main.searchdialog_fullscreen.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity: AppCompatActivity(),OnCLickItem_search , OnClickItem_SearchPerson {


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchdialog_fullscreen)
        var page_search = 1
        var type_content_id = 1
        back_search.setOnClickListener {
            onBackPressed()
        }
        search_forum.setOnClickListener {
            type_content_id = 1
        }
        search_uettalk.setOnClickListener {
            type_content_id = 2
        }
        search_person.setOnClickListener {
            type_content_id = 3
        }

        when(type_content_id)
        {
            1 -> {
                search_forum.setBackgroundColor(R.color.purple_500)
                search_uettalk.setBackgroundColor(R.color.white)
                search_person.setBackgroundColor(R.color.white)
            }
            2 -> {
                search_forum.setBackgroundColor(R.color.white)
                search_uettalk.setBackgroundColor(R.color.purple_500)
                search_person.setBackgroundColor(R.color.white)
            }
            3 -> {
                search_forum.setBackgroundColor(R.color.white)
                search_uettalk.setBackgroundColor(R.color.white)
                search_person.setBackgroundColor(R.color.purple_500)
            }
        }
        search_timkiem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                getdulieusearch(page_search,"",type_content_id)
                search_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
                    override fun onScrollChange(
                        v: NestedScrollView?,
                        scrollX: Int,
                        scrollY: Int,
                        oldScrollX: Int,
                        oldScrollY: Int
                    ) {
                        if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v!!?.measuredHeight) ?: Int)
                        {
                            page_search++
                            getdulieusearch(page_search,"",type_content_id)

                        }
                    }
                })
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                getdulieusearch(page_search,s.toString(),type_content_id)
                search_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
                    override fun onScrollChange(
                        v: NestedScrollView?,
                        scrollX: Int,
                        scrollY: Int,
                        oldScrollX: Int,
                        oldScrollY: Int
                    ) {
                        if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v!!?.measuredHeight) ?: Int)
                        {
                            page_search++
                            getdulieusearch(page_search,s.toString(),type_content_id)

                        }
                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {
                getdulieusearch(page_search,s.toString(),type_content_id)
                search_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
                    override fun onScrollChange(
                        v: NestedScrollView?,
                        scrollX: Int,
                        scrollY: Int,
                        oldScrollX: Int,
                        oldScrollY: Int
                    ) {
                        if (scrollY == v?.getChildAt(0)?.measuredHeight?.minus(v!!?.measuredHeight) ?: Int)
                        {
                            page_search++
                            getdulieusearch(page_search,s.toString(),type_content_id)

                        }
                    }
                })
                Log.e("chua","chua")
            }

        })
    }
    private fun getdulieusearch(page : Int , textsearch: String,type_content_id : Int) {
        if(type_content_id == 1 ||type_content_id == 2)
        {
            var searchquestion: search_question
            val call : Call<search_question> = ApiClient.getClient.getQuestionSearch(page,textsearch.toString(),type_content_id)

            call.enqueue(object : Callback<search_question> {
                override fun onResponse(
                    call: Call<search_question>,
                    response: Response<search_question>
                ) {
                    if (response.isSuccessful)
                    {
                        searchquestion = response.body()!!
                        Log.e("Test_search","Thành Công")
                        val adapter  = adapter_search(searchquestion?.questionDtoList,this@SearchActivity)
                        search_recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)
                        search_recyclerview.adapter = adapter

                    }

                }

                override fun onFailure(call: Call<search_question>, t: Throwable) {
                    Log.e("Test_search","Thất bại")
                }

            })
        }else if (type_content_id == 3)
        {
            var searchPerson : search_person
            val call : Call<search_person> = ApiClient.getClient.getPerSonSearch(page,textsearch.toString())
            call.enqueue(object : Callback<search_person> {
                override fun onResponse(
                    call: Call<search_person>,
                    response: Response<search_person>
                ) {
                    if (response.isSuccessful)
                    {
                        searchPerson = response.body()!!
                        Log.e("Test_search","Thành Công")
                        val adapter  = adapter_search_person(searchPerson.personDtoList!!,this@SearchActivity)
                        search_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                        search_recyclerview.adapter = adapter
                    }

                }

                override fun onFailure(call: Call<search_person>, t: Throwable) {
                    Log.e("Test_search","Thất Bại")
                }
            })
        }

    }

    override fun Onclick(Questiontdo: QuestionDto) {
        Toast.makeText(this,Questiontdo.title, Toast.LENGTH_SHORT).show()
    }

    override fun ClickItem(personDto: PersonDto) {
        Toast.makeText(this,personDto.account_username,Toast.LENGTH_SHORT).show()
    }
}