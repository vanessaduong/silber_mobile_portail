package esipe.fisa.silber.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zouhairhajji on 16/11/2017.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private final static String REST_SERVER = "http://rest.portail.silber.inside.esiag.info";

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();




        GsonConverterFactory gsonFactory = GsonConverterFactory.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(REST_SERVER)
                .addConverterFactory(gsonFactory)
                .client(client)
                .build();

        return retrofit;
    }
}
