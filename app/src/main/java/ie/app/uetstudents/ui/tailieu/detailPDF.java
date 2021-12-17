package ie.app.uetstudents.ui.tailieu;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ie.app.uetstudents.R;
import ie.app.uetstudents.ui.API.ApiClient;
import retrofit2.http.Url;

public class detailPDF extends AppCompatActivity {

    PDFView pdfView;
    ImageButton imageButton;
    TextView tentailieu;
    ImageButton download;

    URL url = null;
    String filename ;


   // String pdfurl = "http://34.126.110.103:8080/uetshare/image/home/uet/data/account_1/exam_document_5/giaitich.pdf";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_document_exam);
        pdfView = findViewById(R.id.idPDFView);
        imageButton = findViewById(R.id.back_document_exam);
        tentailieu = findViewById(R.id.tentailieu);
        download = findViewById(R.id.download);

        /*---------------------------------------------*/
        Intent intent = this.getIntent();
        String link =  intent.getStringExtra("ExamDocument");
       String pdfurl1 = ApiClient.INSTANCE.getBASE_URL()+"image"+link;

        try {
            url = new URL(pdfurl1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        filename = link.substring(link.lastIndexOf("/")+1);
        tentailieu.setText(filename);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url+""));
                request.setTitle(filename);
                request.setMimeType("application/pdf");
                request.allowScanningByMediaScanner();
                request.setAllowedOverMetered(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
                DownloadManager dl = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dl.enqueue(request);
            }
        });


        new RetrivePDFfromUrl().execute(pdfurl1);

    }

    private class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream)
                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .load();
        }
    }
}
