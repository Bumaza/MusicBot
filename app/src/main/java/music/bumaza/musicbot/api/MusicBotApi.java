package music.bumaza.musicbot.api;

import android.content.Context;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import music.bumaza.musicbot.BuildConfig;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicBotApi {

    public static Retrofit retrofit;
    public static MusicBotEndpoints endpoints;


    public static void initApi(){
        if(retrofit != null)
            return;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.connectTimeout(120, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        endpoints = retrofit.create(MusicBotEndpoints.class);
    }

    public static void clearApi(){
        retrofit = null;
    }
}
