package network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static network.RetrofitClient.retrofit;

public class ScalarClient {
    public static final String BASE_URL = "http://sansdigitals.com/phpdemos/foodgenee/api/";
    public static Retrofit retrofit;

    public static Retrofit getApiClient(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }
}
