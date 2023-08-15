package sa.med.imc.myimc.SignUp.presenter;

import static sa.med.imc.myimc.Network.ImcApplication.TAG;

import android.util.Log;

import com.squareup.okhttp.MultipartBuilder;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class UploadFormData {
    public UploadFormData() {
    }

    public JSONObject uploadImage(String URL_UPLOAD_IMAGE, String user, String sourceidCardFile, String sourceinsuranceAttachmentFile) {

        try {
            File idCardFile = new File(sourceidCardFile);
            File insuranceAttachmentFile = new File(sourceinsuranceAttachmentFile);

            Log.d(TAG, "File...::::" + idCardFile + " : " + idCardFile.exists());
            Log.d(TAG, "File...::::" + insuranceAttachmentFile + " : " + insuranceAttachmentFile.exists());
            //Determining the media type
            final MediaType MEDIA_TYPE = MediaType.parse("image/png");


            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("user", user)
                    .addFormDataPart("idCard", "idCard.png", RequestBody.create(MEDIA_TYPE, idCardFile))
                    .addFormDataPart("insuranceAttachment", "insuranceAttachment.png", RequestBody.create(MEDIA_TYPE, insuranceAttachmentFile))
                    .build();

            Request request = new Request.Builder()
                    .url(URL_UPLOAD_IMAGE)
                    .post(requestBody)
                    .addHeader("Content-Type","multipart/form-data")
                    .addHeader("Accept-Language","en")
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}
