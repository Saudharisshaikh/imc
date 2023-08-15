package sa.med.imc.myimc.Settings;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.fingerprint.FingerprintManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

/**
 * Fingerprint authentication for local only.
 * User can set fingerprint to access app dashboard.
 */
public class FingerActivity extends BaseActivity {

    @BindView(R.id.headingLabel)
    TextView headingLabel;
    @BindView(R.id.fingerprintImage)
    ImageView fingerprintImage;
    @BindView(R.id.paraLabel)
    TextView mParaLabel;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;
    @BindView(R.id.layout_fingerprint)
    RelativeLayout layoutFingerprint;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.layout_face_detect)
    RelativeLayout layoutFaceDetect;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private static final String TAG = FingerActivity.class.getName();

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    static int PICK_IMAGE = 22;
    String profile_pic_path;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 499;
    Uri tempUri;
    //
//    // apiEndpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0"
//    private final String apiEndpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0";
//
//    // Replace `<Subscription Key>` with your subscription key.
//    private final String subscriptionKey = "71fe61469595493cb4051402475ca4fa";
//
//    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient(apiEndpoint, subscriptionKey);
    ProgressDialog detectionProgressDialog;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        ButterKnife.bind(this);
//        detectionProgressDialog = new ProgressDialog(this);
//
//        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
//
//        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//
//        if (fingerprintManager == null) {
//            mParaLabel.setTextColor(getResources().getColor(R.color.app_red));
//            mParaLabel.setText("Not detected");
//
//        } else if (!fingerprintManager.isHardwareDetected()) {
//            mParaLabel.setTextColor(getResources().getColor(R.color.app_red));
//            mParaLabel.setText("Not detected");
//
//        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//            mParaLabel.setTextColor(getResources().getColor(R.color.app_red));
//            mParaLabel.setText(getResources().getString(R.string.permission_not_grant));
//
//        } else if (!keyguardManager.isKeyguardSecure()) {
//            mParaLabel.setText(getResources().getString(R.string.add_lock));

//        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//            mParaLabel.setText(getResources().getString(R.string.one_fingerprint));
//
//        } else {
//            mParaLabel.setText(getResources().getString(R.string.place_finger));
//            generateKey();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (cipherInit()) {
//                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
//                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
//                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
//                }
//            }
//        }
        //Create a thread pool with a single thread//

//        Executor newExecutor = Executors.newSingleThreadExecutor();
////Start listening for authentication events//
//
////        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
////            @Override
////
//////onAuthenticationError is called when a fatal error occurrs//
////
////            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
////                super.onAuthenticationError(errorCode, errString);
////                if (errorCode == BiometricPrompt.BIOMETRIC_ERROR_CANCELED) {
////                } else {
////
//////Print a message to Logcat//
////
////                    Log.d(TAG, "An unrecoverable error occurred");
////                }
////            }
////
//////onAuthenticationSucceeded is called when a fingerprint is matched successfully//
////
////            @Override
////            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
////                super.onAuthenticationSucceeded(result);
////
//////Print a message to Logcat//
////
////                Log.d(TAG, "Fingerprint recognised successfully");
////            }
////
//////onAuthenticationFailed is called when the fingerprint doesn’t match//
////
////            @Override
////            public void onAuthenticationFailed() {
////                super.onAuthenticationFailed();
////
//////Print a message to Logcat//
////
////                Log.d(TAG, "Fingerprint not recognised");
////            }
////        });
//
////Create the BiometricPrompt instance//
//
//        final BiometricPrompt promptInfo = new BiometricPrompt.Builder(this)
////Add some text to the dialog//
//
//                .setTitle("Title text goes here")
//                .setSubtitle("Subtitle goes here")
//                .setDescription("This is the description")
//                .setNegativeButton("Cancel", newExecutor, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//
////Build the dialog//
//
//                .build();
//
//        promptInfo.authenticate(new CancellationSignal(), newExecutor, new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//                Log.e(TAG, "onAuthenticationError");
//            }
//
//            @Override
//            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//                super.onAuthenticationHelp(helpCode, helpString);
//                Log.e(TAG, "onAuthenticationHelp");
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                Log.e(TAG, "onAuthenticationSucceeded");
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//                Log.e(TAG, "onAuthenticationFailed");
//            }
//        });
//Assign an onClickListener to the app’s “Authentication” button//
//
//        findViewById(R.id.paraLabel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myBiometricPrompt.authenticate(promptInfo);
//            }
//        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT |
                    KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public boolean cipherInit() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            else
                cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_done, R.id.button1})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;

            case R.id.lay_btn_done:
                finish();
                break;

            case R.id.button1:
//                checkPermissionFirst();
                break;
        }
    }
//
//    void checkPermissionFirst() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//
//                takePhoto();
////                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//
//            } else {
//                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
//            }
//        } else {
//            takePhoto();
////            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////            startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
////                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////                intent.setType("image/*");
////                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE); openGallery();
//        }
//    }

    void takePhoto() {
        String fileName = "My_Photo.jpg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

        tempUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK &&
//                data != null && data.getData() != null) {
//            Uri uri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
//                        getContentResolver(), uri);
//                ImageView imageView = findViewById(R.id.imageView1);
//                imageView.setImageBitmap(bitmap);
//
//                // Comment out for tutorial
//                detectAndFrame(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {


//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Uri selectedImage = getImageUri(getApplicationContext(), photo);
//            Uri selectedImage = data.getData();

            previewCapturedImage(tempUri);

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "User cancelled image capture", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
        }
    }


    private void previewCapturedImage(Uri select) {
        try {
            Bitmap bitmap = null;
            Uri tempUri = null;

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(select, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            Uri fileUri = Uri.parse(picturePath);
            c.close();

            try {
                File f = new File(fileUri.getPath());
                ExifInterface exif = new ExifInterface(f.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                int angle = 0;

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }

                Matrix mat = new Matrix();
                mat.postRotate(angle);

                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
                bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                imageView1.setImageBitmap(bitmap);
//                detectAndFrame(bitmap);


            } catch (IOException e) {
                Log.e("TAG", "-- Error in setting image");
            } catch (OutOfMemoryError oom) {
                Log.e("TAG", "-- OOM Error in setting image");
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Detect faces by uploading a face image.
// Frame faces after detection.
//    private void detectAndFrame(final Bitmap imageBitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        ByteArrayInputStream inputStream =
//                new ByteArrayInputStream(outputStream.toByteArray());
//
//        @SuppressLint("StaticFieldLeak") AsyncTask<InputStream, String, Face[]> detectTask =
//                new AsyncTask<InputStream, String, Face[]>() {
//                    String exceptionMessage = "";
//
//                    @SuppressLint("DefaultLocale")
//                    @Override
//                    protected Face[] doInBackground(InputStream... params) {
//                        try {
//                            publishProgress("Detecting...");
//                            Face[] result = faceServiceClient.detect(
//                                    params[0],
//                                    true,         // returnFaceId
//                                    false,        // returnFaceLandmarks
//                                    null          // returnFaceAttributes:
//                                /* new FaceServiceClient.FaceAttributeType[] {
//                                    FaceServiceClient.FaceAttributeType.Age,
//                                    FaceServiceClient.FaceAttributeType.Gender }
//                                */
//                            );
//                            if (result == null) {
//                                publishProgress("Detection Finished. Nothing detected");
//                                return null;
//                            }
//                            publishProgress(String.format("Detection Finished. %d face(s) detected", result.length));
//                            return result;
//                        } catch (Exception e) {
//                            exceptionMessage = String.format(
//                                    "Detection failed: %s", e.getMessage());
//                            return null;
//                        }
//                    }
//
//                    @Override
//                    protected void onPreExecute() {
//                        //TODO: show progress dialog
//                        detectionProgressDialog.show();
//
//                    }
//
//                    @Override
//                    protected void onProgressUpdate(String... progress) {
//                        //TODO: update progress
//                        detectionProgressDialog.setMessage(progress[0]);
//                    }
//
//                    @Override
//                    protected void onPostExecute(Face[] result) {
//                        //TODO: update face frames
//                        detectionProgressDialog.dismiss();
//
//                        if (!exceptionMessage.equals("")) {
//                            showError(exceptionMessage);
//                        }
//                        if (result == null) return;
//                        Face face = result[0];
////                        faceServiceClient.addPersonFace()
//                        Log.e("face_id", "   " + face.faceId);
//                        Log.e("result[0]", "   " + result[0].faceId.getLeastSignificantBits() + "\n" +
//                                result[0].faceId.getMostSignificantBits());
//
//                        UUID uuid = face.faceId;
//                        ImageView imageView = findViewById(R.id.imageView1);
//                        imageView.setImageBitmap(
//                                drawFaceRectanglesOnBitmap(imageBitmap, result));
//                        imageBitmap.recycle();
////                        UUID uuid1=new UUID(-6639264768673194309,-8232950227064175353);
//                        verifyAndFrame(uuid, uuid);
//                    }
//                };
//
//        detectTask.execute(inputStream);
//    }
//

    // Detect faces by uploading a face image.
// Frame faces after detection.
//    private void verifyAndFrame(final UUID save_id, final UUID face_id) {
//
//        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, VerifyResult> verifyTask =
//                new AsyncTask<String, String, VerifyResult>() {
//                    String exceptionMessage = "";
//
//                    @SuppressLint("DefaultLocale")
//                    @Override
//                    protected VerifyResult doInBackground(String... params) {
//                        try {
//                            publishProgress("Detecting...");
//                            VerifyResult result = faceServiceClient.verify(save_id, face_id);
//                            if (result == null) {
//                                publishProgress("Detection Finished. Nothing detected");
//                                return null;
//                            }
//
//                            // publishProgress(String.format("Detection Finished. %d face(s) detected", result.confidence));
//                            return result;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            exceptionMessage = String.format("Detection failed: %s", e.getMessage());
//                            return null;
//                        }
//                    }
//


//                    @Override
//                    protected void onPreExecute() {
//                        //TODO: show progress dialog
//                        detectionProgressDialog.show();
//
//                    }
//
//                    @Override
//                    protected void onProgressUpdate(String... progress) {
//                        //TODO: update progress
//                        detectionProgressDialog.setMessage(progress[0]);
//                    }
//
//                    @Override
//                    protected void onPostExecute(VerifyResult result) {
//                        //TODO: update face frames
//                        detectionProgressDialog.dismiss();
//
//                        if (!exceptionMessage.equals("")) {
//                            showError(exceptionMessage);
//                        }
//                        if (result == null) return;
//                        Log.e("verified", "" + result.confidence + "   " + result.isIdentical);
//                    }
//                };
//
//        verifyTask.execute();
//    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create().show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

//    private static Bitmap drawFaceRectanglesOnBitmap(
//            Bitmap originalBitmap, Face[] faces) {
//        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(10);
//        if (faces != null) {
//            for (Face face : faces) {
//                FaceRectangle faceRectangle = face.faceRectangle;
//                canvas.drawRect(
//                        faceRectangle.left,
//                        faceRectangle.top,
//                        faceRectangle.left + faceRectangle.width,
//                        faceRectangle.top + faceRectangle.height,
//                        paint);
//            }
//        }
//        return bitmap;
//    }

}
