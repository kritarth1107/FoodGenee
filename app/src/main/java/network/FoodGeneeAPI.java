package network;

import com.foodgeene.cart.Order;
import com.foodgeene.cart.Orderlist;
import com.foodgeene.home.HomeMerchantModel;
import com.foodgeene.login.LoginModel;
import com.foodgeene.payment.Checksum;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.scanner.ScannerModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import updateprofile.UpdateModel;

public interface FoodGeneeAPI {

    @FormUrlEncoded
    @POST("paytmapitest/generateChecksum.php")
    Call<Checksum> getChecksum(
            @Field("MID") String mId,
            @Field("ORDER_ID") String orderId,
            @Field("CUST_ID") String custId,
            @Field("CHANNEL_ID") String channelId,
            @Field("TXN_AMOUNT") String txnAmount,
            @Field("WEBSITE") String website,
            @Field("CALLBACK_URL") String callbackUrl,
            @Field("INDUSTRY_TYPE_ID") String industryTypeId
    );


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



    @FormUrlEncoded
    @POST("users/user-profile.php")
    Call<UserModel> userDetails(
            @Field("action") String action,
            @Header("Authorization") String Authorization,
            @Header("Content-Type") String CType
    );

    @FormUrlEncoded
    @POST("merchant/merchants.php")
    Call<HomeMerchantModel> merchantList(
            @Field("action") String action,
            @Header("Authorization") String Authorization,
            @Header("Content-Type") String CType
    );


    @FormUrlEncoded
    @POST("users/user-profile.php")
    Call<UpdateModel> updateUser(
            @Field("action") String action,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );
    @FormUrlEncoded
    @POST("merchant/orders.php")
    Call<RegisterModel> OrderByCash(
            @Field("action") String action,
            @Field("merchantid") String merchantid,
            @Field("table") String table,
            @Field("productid") String productid,
            @Field("count") String count,
            @Field("price") String price,
            @Field("totalamount") String totalamount,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );@FormUrlEncoded
    @POST("merchant/orders.php")
    Call<RegisterModel> OrderByPrePaid(
                    @Field("action") String action,
                    @Field("merchantid") String merchantid,
                    @Field("table") String table,
                    @Field("productid") String productid,
                    @Field("count") String count,
                    @Field("price") String price,
                    @Field("totalamount") String totalamount,
                    @Field("transactionid") String transactionid,
                    @Field("transactiondate") String transactiondate,
                    @Header("Authorization") String Auth,
                    @Header("Content-Type") String Ctype
            );
    @FormUrlEncoded
    @POST("merchant/orders.php")
    Call<Order> GetOrderList(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );



}