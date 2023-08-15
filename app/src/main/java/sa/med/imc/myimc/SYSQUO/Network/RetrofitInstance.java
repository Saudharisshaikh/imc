package sa.med.imc.myimc.SYSQUO.Network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sa.med.imc.myimc.BuildConfig;

public class RetrofitInstance {


    private static Retrofit retrofit = null;

    public static Retrofit getInstance(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

       // set your desired log level
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(2, TimeUnit.MINUTES)  // connect timeout
                .writeTimeout(2, TimeUnit.MINUTES)      // write timeout
                .readTimeout(2, TimeUnit.MINUTES)      // read timeout
                .build();



       // add logging as last interceptor
//        httpClient.addInterceptor(logging);
//        httpClient.addInterceptor(new BasicAuthInterceptor("vx2Q3XTNTamsfLjX2z4m", "k7G3fpPTLCttgFy3eG5r"));


        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL_VIDEO)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
