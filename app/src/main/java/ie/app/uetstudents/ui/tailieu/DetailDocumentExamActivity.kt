package ie.app.uetstudents.ui.tailieu

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView
import ie.app.uetstudents.R
import kotlinx.android.synthetic.main.activity_detail_document_exam.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DetailDocumentExamActivity : AppCompatActivity() {




    var pdfurl =
        "http://34.126.110.103:8080/uetshare/image/home/uet/data/account_1/exam_document_5/giaitich.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        back_document_exam.setOnClickListener {
            onBackPressed()
        }
        setContentView(R.layout.activity_detail_document_exam)

        RetrivePDFfromUrl(idPDFView).execute(pdfurl)
    }

    private class RetrivePDFfromUrl(var Pdfview : PDFView) :
        AsyncTask<String?, Void?, InputStream?>() {
        protected override fun doInBackground(vararg params: String?): InputStream? {
            var inputStream: InputStream? = null
            try {
                val url = URL(params[0])
                // below is the step where we are
                // creating our connection.
                val urlConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: IOException) {
                // this is the method
                // to handle errors.
                e.printStackTrace()
                return null
            }
            return inputStream
        }

        override fun onPostExecute(inputStream: InputStream?) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            Pdfview.fromStream(inputStream).load()
        }
    }
}