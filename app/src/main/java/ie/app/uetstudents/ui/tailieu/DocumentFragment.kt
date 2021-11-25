package ie.app.uetstudents.ui.tailieu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.app.uetstudents.adapter.adapter_pageView
import ie.app.uetstudents.databinding.FragmentDocumentsBinding
import ie.app.uetstudents.ui.tailieu.fragment.CNTTFragment
import ie.app.uetstudents.ui.tailieu.fragment.Co_KTFragment
import ie.app.uetstudents.ui.tailieu.fragment.DTVTFragment
import ie.app.uetstudents.ui.tailieu.fragment.RoBotFragment
import kotlinx.android.synthetic.main.fragment_documents.*
import kotlinx.android.synthetic.main.fragment_documents.view.*

class DocumentFragment: Fragment() {

    private lateinit var documentViewModel: DocumentViewModel
    private var _binding: FragmentDocumentsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        documentViewModel =
            ViewModelProvider(this).get(DocumentViewModel::class.java)

        _binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapterPageview = activity?.supportFragmentManager?.let { adapter_pageView(it) }
        adapterPageview?.addFragment(CNTTFragment(),"CNTT")
        adapterPageview?.addFragment(DTVTFragment(),"DTVT")
        adapterPageview?.addFragment(Co_KTFragment(), "Cơ Kỹ Thuật")
        adapterPageview?.addFragment(RoBotFragment(), "RoBot")
        root.viewpager?.adapter = adapterPageview
        val bundle = Bundle()
        bundle.putInt("id",1)
        root.tablayout?.setupWithViewPager(root.viewpager)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}