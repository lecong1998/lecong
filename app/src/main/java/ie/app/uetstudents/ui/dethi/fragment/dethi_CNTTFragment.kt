package ie.app.uetstudents.ui.dethi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ie.app.uetstudents.R
import ie.app.uetstudents.adapter.adapter_chude
import kotlinx.android.synthetic.main.fragment_c_n_t_t2.*
import kotlinx.android.synthetic.main.fragment_ro_bot.*
import kotlinx.android.synthetic.main.fragment_ro_bot.list_subjects_robot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CNTTFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class dethi_CNTTFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_c_n_t_t2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listmonhoc = ArrayList<String>()
        listmonhoc.add("Lập trình hướng đối tượng")
        listmonhoc.add("Phát triển ứng dụng Web")
        listmonhoc.add("Toán rời rạc")
        listmonhoc.add("Điện Quang")
        //var adapter = adapter_chude(listmonhoc)
        dethi_list_subjects_cntt.layoutManager = LinearLayoutManager(requireContext())
        //dethi_list_subjects_cntt.adapter = adapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CNTTFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            dethi_CNTTFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}