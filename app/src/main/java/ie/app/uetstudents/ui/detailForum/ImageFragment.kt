package ie.app.uetstudents.ui.detailForum

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ie.app.uetstudents.API.ApiClient
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.fragment_image.*


class ImageFragment : Fragment() {

    var image : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            image = it?.getString("image") as String
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity()).load(ApiClient.BASE_URL+"image"+image)
            .error(R.drawable.img_error)
            .into(image_image)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        val item2: MenuItem = menu.findItem(R.id.action_profile)
        val item3: MenuItem = menu.findItem(R.id.action_notification)
        item.isVisible = false
        item2.setVisible(false)
        item3.isVisible = false
    }

}