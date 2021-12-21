package ie.app.uetstudents.ui.thongbao.detail

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ie.app.uetstudents.R
import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.adapter.OnclickImageDto
import ie.app.uetstudents.adapter.adapter_pdf
import ie.app.uetstudents.ui.Entity.Question.get.ImageDto
import ie.app.uetstudents.ui.Entity.Question.get.QuestionDtoX
import ie.app.uetstudents.ui.tailieu.detailPDF
import ie.app.uetstudents.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_detail_notifi.*

class DatailNotiFragment: Fragment() , DetailContract.View , OnclickImageDto {
    var id_question = 0
    private lateinit var presenter: DetailContract.Presenter
    var id_user = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            id_question = it!!.getInt("id_question")
        }
        id_user = PreferenceUtils.getUser().id
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_notifi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = DetailPresenter(this, Repository(requireContext()))
        presenter.getQuestionDetail(id_question, id_user)

    }

    override fun UpdateViewDetail(Question: QuestionDtoX) {
        detai_notifi_title.text = Question.title
        content_notifi.text = Question.content
        val time: String = Question.time?.substring(11, 16)+ " " + Question.time?.substring(0, 10)
        time_detail.text = time
        val adapterPdf = adapter_pdf(Question.imageDtoList,this)
        recyclerview_listtailieu.layoutManager = LinearLayoutManager(requireContext())
        recyclerview_listtailieu.adapter= adapterPdf
    }

    override fun OnclickItem(imageDto: ImageDto) {
        val intent = Intent(activity, detailPDF::class.java)
        intent.putExtra("ExamDocument", imageDto.image)
        startActivity(intent)
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

}