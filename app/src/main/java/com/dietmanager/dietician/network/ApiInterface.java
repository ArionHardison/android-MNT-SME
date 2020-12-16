package com.dietmanager.dietician.network;

import com.dietmanager.dietician.model.Addon;
import com.dietmanager.dietician.model.AuthToken;
import com.dietmanager.dietician.model.CancelReasons;
import com.dietmanager.dietician.model.Category;
import com.dietmanager.dietician.model.ChangePassword;
import com.dietmanager.dietician.model.Cuisine;
import com.dietmanager.dietician.model.ForgotPassword;
import com.dietmanager.dietician.model.ForgotPasswordResponse;
import com.dietmanager.dietician.model.HistoryModel;
import com.dietmanager.dietician.model.HistoryOrder;
import com.dietmanager.dietician.model.IncomingOrders;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.Otp;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.model.RegisterResponse;
import com.dietmanager.dietician.model.ResetPasswordResponse;
import com.dietmanager.dietician.model.RevenueResponse;
import com.dietmanager.dietician.model.SmallMessageResponse;
import com.dietmanager.dietician.model.StripeResponse;
import com.dietmanager.dietician.model.Transporter;
import com.dietmanager.dietician.model.WalletHistory;
import com.dietmanager.dietician.model.assignchef.AssignChefItem;
import com.dietmanager.dietician.model.currentfood.CurrentFoodItem;
import com.dietmanager.dietician.model.followers.Followers;
import com.dietmanager.dietician.model.food.FoodItem;
import com.dietmanager.dietician.model.food.FoodResponse;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;
import com.dietmanager.dietician.model.initeduser.InvitedUserItem;
import com.dietmanager.dietician.model.ordernew.OrderResponse;
import com.dietmanager.dietician.model.product.ProductResponse;
import com.dietmanager.dietician.model.subscribe.SubscribeItem;
import com.dietmanager.dietician.model.subscriptionplan.SubscriptionPlanItem;
import com.dietmanager.dietician.model.timecategory.TimeCategoryItem;
import com.dietmanager.dietician.model.transaction.TransactionResponse;
import com.dietmanager.dietician.model.userrequest.UserRequestItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    /*-------------USER--------------------*/
    @GET("api/dietitian/profile")
    Call<Profile> getProfile(@QueryMap HashMap<String, String> params);

    @Multipart
    @POST("api/dietitian/profile")
    Call<Profile> updateProfileWithImage(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part filename);

    @GET("api/dietitian/wallet/request")
    Call<TransactionResponse> fetchTransactions();

    @FormUrlEncoded
    @POST("api/dietitian/wallet/request")
    Call<SmallMessageResponse> requestAmount(@Field("amount") String amount);

    @Multipart
    @POST("api/dietitian/add/food")
    Call<MessageResponse> addFood(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part filename);


    @FormUrlEncoded
    @POST("api/dietitian/chat/push")
    Call<Object> chatPost(@FieldMap HashMap<String, String> paramss);

    @FormUrlEncoded
    @POST("api/dietitian/add/food")
    Call<MessageResponse> addAdminFood(@FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("api/dietitian/subscription")
    Call<MessageResponse> addSubscriptionPlan(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/dietitian/subscription/{id}")
    Call<MessageResponse> editSubscriptionPlan(@FieldMap HashMap<String, String> params,@Path("id")int id);

    @Multipart
    @POST("api/dietitian/profile/{id}")
    Call<Profile> updateProfileWithFile(@Path("id") int id, @PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part filename1, @Part MultipartBody.Part filename2);

    @Multipart
    @POST("api/dietitian/profile/{id}")
    Call<Profile> updateProfile(@Path("id") int id, @PartMap HashMap<String, RequestBody> params);

    @FormUrlEncoded
    @POST("api/dietitian/follower/create")
    Call<MessageResponse> inviteUser( @FieldMap HashMap<String, String> params);
//
//    @Multipart
//    @POST("api/user/profile")
//    Call<User> updateProfileWithImage(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part filename);
//
    @FormUrlEncoded
    @POST("api/dietitian/register/otp")
    Call<Otp> postOtp(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/dietitian/oauth/token")
    Call<AuthToken> login(@FieldMap HashMap<String, String> params);

    @Multipart
    @POST("api/dietitian/register")
    Call<RegisterResponse> postRegister(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part filename);

    @Multipart
    @POST("api/dietitian/register")
    Call<Profile> signUp(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part filename1, @Part MultipartBody.Part filename2);

    @GET("api/dietitian/cuisines")
    Call<List<Cuisine>> getCuisines();

    @FormUrlEncoded
    @PATCH("api/dietitian/order/{id}")
    Call<Order> updateOrderStatus(@Path("id") int id, @FieldMap HashMap<String, String> params);

    @GET("api/dietitian/order/{id}")
    Call<OrderResponse> getParticularOrders(@Path("id") int id);

    @GET("api/dietitian/products")
    Call<List<ProductResponse>> getProductList();

    @GET("api/dietitian/followers")
    Call<List<Followers>> getFollowersList();

    @GET("api/dietitian/reasons")
    Call<CancelReasons> getCancelReasonList();

    /*-------------ADD-ONS--------------------*/
    @GET("api/dietitian/addons")
    Call<List<Addon>> getAddons();

    @FormUrlEncoded
    @POST("api/dietitian/addons")
    Call<Addon> addAddon(@Field("name") String name, @Field("calories") String mCalories, @Field("shop_id") String shop_id);

    @FormUrlEncoded
    @PATCH("api/dietitian/addons/{id}")
    Call<Addon> updateAddon(@Path("id") int id, @Field("name") String name, @Field("calories") String mCalories, @Field("shop_id") String shop_id);

    @DELETE("api/dietitian/addons/{id}")
    Call<List<Addon>> deleteAddon(@Path("id") int id);

    /*------------- CATEGORY --------------------*/
    @GET("api/dietitian/categories")
    Call<List<Category>> getCategory();

    @GET("api/dietitian/get/ingredients")
    Call<List<IngredientsItem>> getIngredients();

    @Multipart
    @POST("api/dietitian/categories")
    Call<Category> addCategory(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part filename);

    @Multipart
    @POST("api/dietitian/categories/{id}")
    Call<Category> updateCategory(@Path("id") int id, @PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part filename);

    @DELETE("api/dietitian/categories/{id}")
    Call<List<Category>> deleteCategory(@Path("id") int id);

    /*---------------Change Password---------------*/
    @FormUrlEncoded
    @POST("api/dietitian/password")
    Call<ChangePassword> changePassword(@FieldMap HashMap<String, String> params);

    @GET("api/dietitian/wallet/transaction")
    Call<List<WalletHistory>> getWalletHistory();

    @FormUrlEncoded
    @POST("api/dietitian/forgot/password")
    Call<ForgotPasswordResponse> forgotPassword(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/dietitian/forgot/password")
    Call<ForgotPassword> forgotPassword(@Field("phone") String mobile,
                                        @Field("hashcode") String hashcode);

    @FormUrlEncoded
    @POST("api/dietitian/verifyotp")
    Call<ForgotPasswordResponse> verifyOTP(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/dietitian/assign/chef")
    Call<MessageResponse> assignChefPost(@FieldMap HashMap<String, String> params);
    @FormUrlEncoded
    @POST("api/dietitian/diet/cancel/order")
    Call<MessageResponse> cancelOrderPost(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/dietitian/reset/password")
    Call<ResetPasswordResponse> resetPassword(@FieldMap HashMap<String, String> params);

    /*........Revenue Fragment......*/
    @GET("api/dietitian/revenue")
    Call<RevenueResponse> getRevenueDetails();

    //    @Headers("Content-Type: application/json")
    @Multipart
    @POST("api/dietitian/products")
    Call<ProductResponse> addProduct(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part filepart1, @Part MultipartBody.Part filepart2);

    @Multipart
    @POST("api/dietitian/products/{id}")
    Call<ProductResponse> updateProduct(@Path("id") int id, @PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part filepart1, @Part MultipartBody.Part filepart2);

    @DELETE("api/dietitian/products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

    /*....Account....*/
    @DELETE("api/dietitian/remove/{id}")
    Call<ResponseBody> deleteAccount(@Path("id") String id);

    @GET("api/dietitian/logout")
    Call<ResponseBody> logOut();

    /*-------------ORDER--------------------*/
    @GET("api/dietitian/order")
    Call<IncomingOrders> getIncomingOrders(@Query("t") String type);

    @GET("/api/dietitian/order")
    Call<HistoryOrder> getHistoryOrders(@QueryMap Map<String, String> params);

    @GET("/api/dietitian/time/category")
    Call<List<TimeCategoryItem>> getTimeCategory();

    @GET("/api/dietitian/admin/foods")
    Call<List<FoodItem>> getFood(@Query("category_id") int categoryId);

    @GET("/api/dietitian/current/food")
    Call<List<CurrentFoodItem>> getCurrentFood(@Query("category_id") int categoryId, @Query("day") int dayId);

    @GET("api/dietitian/diet/orders/history")
    Call<List<UserRequestItem>> getHistory(@Query("list") String status);

    @GET("api/dietitian/subscribers/list")
    Call<List<SubscribeItem>> getSubscribedList();

    @GET("api/dietitian/diet-order/{id}/chef")
    Call<List<AssignChefItem>> getAssignChefList(@Path("id")int id);

    @GET("api/dietitian/invited/users/lists")
    Call<List<InvitedUserItem>> getInvitedUser();

    @GET("api/dietitian/new/orders")
    Call<List<UserRequestItem>> getUserRequests();

    @GET("api/dietitian/subscription")
    Call<List<SubscriptionPlanItem>> getSubscribePlanList();

    @GET("api/dietitian/history")
    Call<HistoryModel> getFilterHistory(@QueryMap HashMap<String, String> params);

    @GET("api/dietitian/transporterlist")
    Call<List<Transporter>> getTransporter();

    @GET("api/dietitian/cuisines")
    Call<List<Cuisine>> getImages();

//    @GET("api/user/ongoing/order")
//    Call<List<Order>> getOngoingOrders();
//
//    @GET("api/user/order/{id}")
//    Call<Order> getParticularOrders(@Path("id") int id);
//
//    @GET("api/user/order")
//    Call<List<Order>> getPastOders();
//
//    @DELETE("api/user/order/{id}")
//    Call<Order> cancelOrder(@Path("id") int id, @Query("reason") String reason);
//
//    @FormUrlEncoded
//    @POST("api/user/rating")
//    Call<ChangePassword> rate(@FieldMap HashMap<String, String> params);
//
//    @FormUrlEncoded
//    @POST("api/user/reorder")
//    Call <AddCart> reOrder(@FieldMap HashMap<String, String> params);
//
//     /*-------------DISPUTE--------------------*/
//    @GET("api/user/disputehelp")
//    Call<List<DisputeMessage>> getDisputeList();
//
//    @FormUrlEncoded
//    @POST("api/user/dispute")
//    Call<Order> postDispute(@FieldMap HashMap<String, String> params);
//
//    /*-------------SEARCH--------------------*/
//    @GET("api/user/search")
//    Call<Search> getSearch(@QueryMap HashMap<String, String> params);
//
//    /*-----------------------WALLET-----------------------*/
//    @GET("api/user/wallet")
//    Call<List<WalletHistory>> getWalletHistory();
//
//    @GET("api/user/wallet/promocode")
//    Call<List<Promotions>> getWalletPromoCode();
//
//    @FormUrlEncoded
//    @POST("api/user/wallet/promocode")
//    Call<PromotionResponse> applyWalletPromoCode(@Field("promocode_id") String id);

    @GET("json?")
    Call<ResponseBody> getResponse(@Query("latlng") String param1, @Query("key") String param2);

//    /*-------------PAYMENT--------------------*/
//    @GET("api/user/card")
//    Call<List<Card>> getCardList();
//
//    @FormUrlEncoded
//    @POST("api/user/card")
//    Call<ChangePassword> addCard(@Field("stripe_token") String stripeToken);
//
//    @FormUrlEncoded
//    @POST("api/user/add/money")
//    Call<AddMoney> addMoney(@FieldMap HashMap<String, String> params);
//
//    @DELETE("api/user/card/{id}")
//    Call<ChangePassword> deleteCard(@Path("id") int id);

    @FormUrlEncoded
    @POST("api/dietitian/stripe/callback")
    Call<StripeResponse> updateBankDetails(@Field("code") String stripeToken);
}