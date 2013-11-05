package com.example.MultipartSpice.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.example.MultipartSpice.responses.UploadResponse;
import com.example.MultipartSpice.utils.CustomMultipartEntity;
import com.google.api.client.http.HttpStatusCodes;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;

/**
 * Class to upload images to server
 *
 * Project: MultipartSpice
 * Package: com.example.MultipartSpice
 * User: Nelson Sachse
 */
public class UploaderService extends IntentService {
    private static final String URL = "..add endpoint..";
    public static final String IMAGE_PATH = "IMAGE_PATH";
    public static final String SRC_FILE = "src_file";

    private int mUploadProgression;
    private long totalSize;
    private Intent mLocalIntent;
    public String mPath;

    /**
     * Creates an IntentService.
     * Invoked by your subclass's constructor.
     */
    public UploaderService() {
        super("Uploader Service Worker Thread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String serverResponse = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        UploadResponse uploadResponse = null;

        try {
            mPath = intent.getExtras().get(IMAGE_PATH).toString();
        } catch (Exception exception) {
            Log.w("Uploader", "Fail intent path :" + exception.getLocalizedMessage());
        }

        try {
            CustomMultipartEntity multipartContent = new CustomMultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new CustomMultipartEntity.ProgressListener() {
                @Override
                public void transferred(long num) {
                    publishProgress((int) ((num / (float) totalSize) * 100));
                }
            });

            // add parts
            multipartContent.addPart(SRC_FILE, new FileBody(new File(mPath)));
            totalSize = multipartContent.getContentLength();

            // send
            httpPost.setEntity(multipartContent);
            HttpResponse response = httpClient.execute(httpPost);
            serverResponse = EntityUtils.toString(response.getEntity());

            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatusCodes.STATUS_CODE_OK:
                    new UploadResponse(null);
                    break;

                case 400:
                case 401:
                case 403:
                case 404:
                    new UploadResponse(serverResponse);
                    break;

                default:
                    throw new RuntimeException("Something went wrong :" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            Log.w("Uploader", "Exception" + e);
        }

        //finish upload
        onPostExecute(uploadResponse);
    }

    protected void publishProgress(int value) {
        mUploadProgression = value;
        mLocalIntent = new Intent("com.example.MultipartSpice.BROADCAST_UPLOAD")
                .putExtra("com.example.MultipartSpice.BROADCAST_PROGRESSION", mUploadProgression);

        //by the use of the local broadcast, who ever is register to this broadcast will get the progress of the uploader.
        LocalBroadcastManager.getInstance(this).sendBroadcast(mLocalIntent);
    }

    protected void onPostExecute(UploadResponse uploadResponse) {
        //I will just propagate the termination of the upload by the use of a local broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(mLocalIntent);
    }
}
