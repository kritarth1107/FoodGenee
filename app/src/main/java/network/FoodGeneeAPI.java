package network;

import com.foodgeene.allhotels.hotelsmodel.HotelsModel;
import com.foodgeene.cart.AlertModel;
import com.foodgeene.cart.OrderListModel;
import com.foodgeene.cart.ReservationListModel;
import com.foodgeene.coinstransactions.model.Transaction;
import com.foodgeene.firebaseservices.FirebaseMessagingModel;
import com.foodgeene.foodpreference.model.AfterOrderModel;
import com.foodgeene.forgot.ForgotOto;
import com.foodgeene.forgot.ForgotPasswordModel;
import com.foodgeene.forgot.changereal.PasswordChangeModel;
import com.foodgeene.home.HomeMerchantModel;
import com.foodgeene.home.brandlist.brandmodel.Brand;
import com.foodgeene.home.hometwo.models.HomeTwoModel;
import com.foodgeene.login.LoginModel;
import com.foodgeene.orderratings.CancelModel;
import com.foodgeene.orderratings.RatingModel;
import com.foodgeene.payment.Checksum;
import com.foodgeene.payment.coupon.Coupon;
import com.foodgeene.preoder.preordermodel.BookTableModel;
import com.foodgeene.preoder.preordermodel.PreOrderModel;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.redeemedlistdetails.model.RedeemedModel;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.register.signupotp.OtpModel;
import com.foodgeene.register.signupotp.ResendOtpModel;
import com.foodgeene.rewarddetails.model.DetailModel;
import com.foodgeene.rewarddetails.model.RedeemCoinsModel;
import com.foodgeene.rewards.rewardmodels.RModel;
import com.foodgeene.rewards.rewardmodels.RedeemCount;
import com.foodgeene.scanner.ScannerModel;
import com.foodgeene.success.PostOrderModel;
import com.foodgeene.transactionlists.model.ListModel;
import com.foodgeene.updateprofile.UpdateModel;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
            @Field("foodtype") Integer foodtype,
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
            @Field("latitude") String latitude,@Field("longitude") String longitude,
            @Header("Authorization") String Authorization,
            @Header("Content-Type") String CType
    );

    @FormUrlEncoded
    @POST("merchant/merchants.php")
    Call<HomeMerchantModel> location(
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
    Call<PostOrderModel> OrderByCash(
            @Field("action") String action,
            @Field("merchantid") String merchantid,
            @Field("table") String table,
            @Field("productid") String productid,
            @Field("count") String count,
            @Field("price") String price,
            @Field("totalamount") String totalamount,
            @Field("orderid") String orderid,
            @Field("couponamount")String couponAmount,
            @Field("coupon")String coupon,
            @Field("tax") String tax,
            @Field("tips")String tips,
            @Field("subscription")String subscription,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );@FormUrlEncoded
    @POST("merchant/orders.php")
    Call<PostOrderModel> OrderByPrePaid(
                    @Field("action") String action,
                    @Field("merchantid") String merchantid,
                    @Field("table") String table,
                    @Field("productid") String productid,
                    @Field("count") String count,
                    @Field("price") String price,
                    @Field("totalamount") String totalamount,
                    @Field("transactionid") String transactionid,
                    @Field("transactiondate") String transactiondate,
                    @Field("couponamount")String couponAmount,
                    @Field("coupon")String coupon,
                    @Field("tax") String tax,
                    @Field("tips")String tips,
                    @Field("subscription")String subscription,
                    @Field("orderid") String orderid,
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
    @POST("merchant/orders.php")
    Call<ReservationListModel> GetReservationList(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/merchants.php")
    Call<HomeTwoModel> getRecomm(
            @Field("action") String action,
            @Field("latitude") String latitude,@Field("longitude") String longitude,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/merchants.php")
    Call<Brand> getBrandList(
            @Field("action") String action,
            @Field("latitude") String latitude,@Field("longitude") String longitude,
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
            @Field(" productid") String  productid,
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
    Call<RedeemCoinsModel> redeemCoins(
            @Field("action") String action,
            @Field("rewardid") String rewardId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<Transaction> transCoins(
            @Field("action") String action,
//            @Field("reason") String coinsGaine,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<RedeemedModel> redeemedListDet(
            @Field("action") String action,
            @Field("rewardid") String rewardId,
            @Field("rewardcouponid") String rewardcouponid,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );


    @FormUrlEncoded
    @POST("users/user-profile.php")
    Call<FirebaseMessagingModel> sendPushId(
            @Field("action") String action,
            @Field("pushid") String pushId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("users/feedback.php")
    Call<RatingModel> orderFeedback(
            @Field("action") String action,
            @Field("merchantid") String merchantId,
            @Field("rating") String rating,
            @Field("message") String message,
            @Field("orderid") String orderId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<ForgotPasswordModel> forgotPassword(
            @Field("action") String action,
            @Field("username") String forgotPassword
            );

    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<ForgotOto> verifyForOtp(
            @Field("action") String action,
            @Field("otp") String otp,
            @Field("usersid") String userid


    );

    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<PasswordChangeModel> changePassword(

            @Field("action") String action,
            @Field("password") String otp,
            @Field("usersid") String userid

    );

    @Multipart
    @POST("users/user-registration.php")
    Call<ResponseBody> updatePic(

            @Part MultipartBody.Part profilepic,
            @Part("usersid") String usersid,
            @Part("action") String action

    );

    @FormUrlEncoded
    @POST("users/feedback.php")
    Call<AlertModel> alertIcon(

            @Field("action") String action,
            @Field("orderid") String orderId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype

    );

    @FormUrlEncoded
    @POST("users/feedback.php")
    Call<CancelModel> cancelFeed(
            @Field("action") String action,
            @Field("orderid") String orderId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("merchant/coins.php")
    Call<ListModel> listTrans(
            @Field("action") String action,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );

    @FormUrlEncoded
    @POST("users/user-registration.php")
    Call<ResendOtpModel> resendOtp(
            @Field("action") String action,
            @Field("usersid") String userid
    );


    @FormUrlEncoded
    @POST("merchant/merchants.php")
    Call<HotelsModel> getAllHotel(
            @Field("action") String action,
            @Field("latitude") String latitude,@Field("longitude") String longitude,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype
    );


    @FormUrlEncoded
    @POST("merchant/preorders.php")
    Call<PreOrderModel> getPreOrder(
            @Field("action") String action,
            @Field("merchantid") String merchantId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype

    );

    @FormUrlEncoded
    @POST("merchant/orders.php")
    Call<PreOrderModel> getInvoice(
            @Field("action") String action,
            @Field("orderid") String orderid,
            @Field("email") String email,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype

    );


    @FormUrlEncoded
    @POST("merchant/preorders.php")
    Call<BookTableModel> bookATable(
            @Field("action") String action,
            @Field("merchantid") String merchantId,
            @Field("tableid") String tableId,
            @Field("bookdate") String bookedDate,
            @Field("booktime") String bookedTime,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype

    );


    @FormUrlEncoded
    @POST("merchant/orders.php")
    Call<AfterOrderModel> afterOrderDetails(
            @Field("action") String action,
            @Field("orderid") String merchantId,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype

    );


    @FormUrlEncoded
    @POST("merchant/orders.php")
    Call<AfterOrderModel> submitPreference(
            @Field("action") String action,
            @Field("orderid") String orderid,
            @Field("orderproductid") String orderproductid,
            @Field("inc") String inc,
            @Header("Authorization") String Auth,
            @Header("Content-Type") String Ctype

    );

}