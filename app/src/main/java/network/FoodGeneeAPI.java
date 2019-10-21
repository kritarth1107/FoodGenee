package network;

import com.foodgeene.login.LoginModel;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.scanner.ScannerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FoodGeneeAPI {

    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<RegisterModel> submitRegistration(
            @Field("action") String action,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<LoginModel> submitLogin(
            @Field("action") String action,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<LoginModel> submitForgotPassword(
            @Field("action") String action,
            @Field("username") String username
    );
    @FormUrlEncoded
    @POST("merchant/qr-scanner.php")
    Call<ScannerModel> submitScannedQR(
            @Field("action") String action,
            @Field("enckey") String enckey,
            @Header("Authorization") String Authorization,
            @Header("Content-Type") String CType
    );



}