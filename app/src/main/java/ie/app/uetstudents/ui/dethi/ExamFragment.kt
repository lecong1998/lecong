package ie.app.uetstudents.ui.dethi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.app.uetstudents.adapter.adapter_pageView
import ie.app.uetstudents.databinding.FragmentExamBinding
import ie.app.uetstudents.ui.dethi.fragment.dethi_CNTTFragment
import ie.app.uetstudents.ui.dethi.fragment.dethi_Co_KTFragment
import ie.app.uetstudents.ui.dethi.fragment.dethi_DTVTFragment
import ie.app.uetstudents.ui.dethi.fragment.dethi_RoBotFragment
import ie.app.uetstudents.ui.tailieu.fragment.CNTTFragment
import ie.app.uetstudents.ui.tailieu.fragment.Co_KTFragment
import ie.app.uetstudents.ui.tailieu.fragment.DTVTFragment
import ie.app.uetstudents.ui.tailieu.fragment.RoBotFragment
import kotlinx.android.synthetic.main.fragment_documents.view.*
import kotlinx.android.synthetic.main.fragment_exam.view.*

class ExamFragment: Fragment() {
    private lateinit var examViewModel: ExamViewModel
    private var _binding: FragmentExamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        examViewModel =
            ViewModelProvider(this).get(ExamViewModel::class.java)

        _binding = FragmentExamBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adapterPageview = activity?.supportFragmentManager?.let { adapter_pageView(it) }
        adapterPageview?.addFragment(dethi_CNTTFragment(),"CNTT")
        adapterPageview?.addFragment(dethi_DTVTFragment(),"DTVT")
        adapterPageview?.addFragment(dethi_Co_KTFragment(), "Cơ Kỹ Thuật")
        adapterPageview?.addFragment(dethi_RoBotFragment(), "RoBot")
        root.viewpager_dethi?.adapter = adapterPageview
        root.tablayout_dethi?.setupWithViewPager(root.viewpager_dethi)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}