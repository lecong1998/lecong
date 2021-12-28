package ie.app.uetstudents.ui.detailForum

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
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

        var url : String = ApiClient.BASE_URL+"image"+image as String
        Glide.with(requireActivity()).load(url)
            .error(R.drawable.img_error)
            .into(image_image)

        taianh.setOnClickListener {
            val request = DownloadManager.Request(Uri.parse(url.toString() + ""))
            val filename = image?.substring(image?.lastIndexOf("/")?.plus(1)!!)
            request.setTitle(filename)
            request.setMimeType("image/*")
            request.allowScanningByMediaScanner()
            request.setAllowedOverMetered(true)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
            val dl = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dl.enqueue(request)
        }
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