package sa.med.imc.myimc.Network;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sa.med.imc.myimc.BuildConfig;

/**
 * Retrofit service generator to call API
 */

public class AppointmentServiceGenerator {
    private static final String TAG = "RetrofitManager";

    // endregion
    private static Gson gson = new GsonBuilder().setLenient().serializeNulls().create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson));
    String token = "";
    private static OkHttpClient defaultOkHttpClient = getUnsafeOkHttpClient();


    // No need to instantiate this class.
    private AppointmentServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, BuildConfig.BASE_URL, new AppointmentServiceGenerator.MyOkHttpInterceptor(""));
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, String no) {
        return createService(serviceClass, baseUrl, new AppointmentServiceGenerator.MyOkHttpInterceptor(no));
    }

    public static <S> S createService(Class<S> serviceClass, String token) {
        return createService(serviceClass, BuildConfig.BASE_URL, new AppointmentServiceGenerator.MyOkHttpInterceptor(token));
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Interceptor networkInterceptor) {

        OkHttpClient.Builder okHttpClientBuilder = defaultOkHttpClient.newBuilder();

        if (networkInterceptor != null) {
            okHttpClientBuilder.addNetworkInterceptor(networkInterceptor);
        }
        okHttpClientBuilder.writeTimeout(120, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(90, TimeUnit.SECONDS);

        OkHttpClient modifiedOkHttpClient = okHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor()).build();
        // Install the all-trusting trust manager

        retrofitBuilder.client(modifiedOkHttpClient);
        retrofitBuilder.baseUrl(baseUrl);
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());//RxJava2CallAdapterFactory.create()
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);

    }

    private static Cache getCache() {
        Cache mCache = null;
        try {
            mCache = new Cache(new File(ImcApplication.getInstance().getCacheDir(), "http_cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!");
        }

        return mCache;
    }

    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return httpLoggingInterceptor;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.sslSocketFactory(sslSocketFactory, x509TrustManager);//, (X509TrustManager) trustAllCerts[0]
            builder.hostnameVerifier((hostname, session) -> true);

            OkHttpClient okHttpClient = builder.cache(getCache()).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class MyOkHttpInterceptor implements Interceptor {
        String token = "", lang = "", isGuardian = "no";

        MyOkHttpInterceptor(String token) {
            this.token = token;
            if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0)
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            else
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

            isGuardian = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_GUARDIAN, "no");

            if (lang.length() == 0)
                lang = Constants.ENGLISH;

            if (isGuardian.length() == 0)
                isGuardian = "no";
        }


        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request newRequest = null;

            if (token != null) {
                if (token.length() > 0 && token.contains("Bearer")) {
                    newRequest = originalRequest.newBuilder()
                            .header("Authorization", token)
//                            .header("Content-type", "application/json")
                            .header("Accept-Language", lang)
                            .header("isGuardian", isGuardian)
                            .header("reportFormat", "pdf")
                            .header("platform", "android")
                            .build();

                } else {
                    if (token.length() > 0)
                        if (token.contains("Basic")) {
                            newRequest = originalRequest.newBuilder()
                                    .header("Authorization", token)
                                    .header("isGuardian", isGuardian)
                                    .header("platform", "android")
                                    .build();
                        } else
                            newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .header("Accept-Language", lang)
                                    .header("isGuardian", isGuardian)
                                    .header("platform", "android")
                                    .build();
                    else
                        newRequest = originalRequest.newBuilder()
                                .header("Accept-Language", lang)
                                .header("platform", "android")
                                .build();
                }
            } else
                newRequest = originalRequest.newBuilder()
                        .header("Accept-Language", lang)
                        .header("platform", "android")
                        .build();

//            Log.e("headers  ", String.valueOf(newRequest.headers()));
//            final String userAgent = newRequest.header("User-Agent");
//            String userAgent = System.getProperty("http.agent");
//            Log.e("headers  ", userAgent);

            return chain.proceed(newRequest);
        }
    }

}
