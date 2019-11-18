package network;

import com.foodgeene.cart.OrderListModel;
import com.foodgeene.home.HomeMerchantModel;
import com.foodgeene.home.brandlist.brandmodel.Brand;
import com.foodgeene.home.hometwo.models.HomeTwoModel;
import com.foodgeene.home.hometwo.models.Merchantlist;
import com.foodgeene.login.LoginModel;
import com.foodgeene.payment.Checksum;
import com.foodgeene.payment.coupon.Coupon;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.register.signupotp.OtpModel;
import com.foodgeene.rewarddetails.model.DetailModel;
import com.foodgeene.rewarddetails.model.RedeemCoins;
import com.foodgeene.rewards.rewardmodels.RModel;
import com.foodgeene.rewards.rewardmodels.RedeemCount;
import com.foodgeene.scanner.ScannerModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import updateprofile.UpdateModel;

public interface FoodGeneeAPI {

    @FormUrlEncoded
    @POST("paytmapi/generateChecksum.php")
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
            @Field("mobile") String mobile,
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
    Call<OrderListModel> GetOrderList(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/merchants.php")
    Call<HomeTwoModel> getRecomm(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/merchants.php")
    Call<Brand> getBrandList(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );



    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<OtpModel> verifyOtp(
            @Field("action") String action,
            @Field("otp") String otp,
            @Field("usersid") String userid


            );


    @FormUrlEncoded
    @POST("merchant/coupon.php")
    Call<Coupon> applyCoupon(
            @Field("action") String action,
            @Field("coupon") String coupon,
            @Field("amount") String amount,
            @Field("merchant_id") String merchant_id,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<RModel> rewardsList(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<RModel> coinsTrans(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<RedeemCount> redeemCount(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<DetailModel> rewardListDetails(
            @Field("action") String action,
            @Field("rewardid") String rewardId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<RedeemCoins> redeemCoins(
            @Field("action") String action,
            @Field("rewardid") String rewardId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );


}