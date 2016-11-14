package list.umorili.android.com.umorili.rest;

import android.util.Log;

import org.androidannotations.annotations.EBean;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestList  {
    public final static String BASE_NAME = "http://www.umori.li/";


    private UmoriliApi umoriliApi;

    public  RestList(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_NAME)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        umoriliApi = retrofit.create(UmoriliApi.class);
    }

    public UmoriliApi getUmoriliApi() {
        return umoriliApi;
    }
}
